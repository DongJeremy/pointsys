package jp.co.nri.point.api.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jp.co.nri.point.annotation.OperationLog;
import jp.co.nri.point.api.domain.Department;
import jp.co.nri.point.api.domain.Employee;
import jp.co.nri.point.api.dto.EmployeeVO;
import jp.co.nri.point.api.service.EmployeeService;
import jp.co.nri.point.beans.PageResultBean;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.pagination.PaginationHandler;
import jp.co.nri.point.pagination.PaginationRequest;
import jp.co.nri.point.pagination.PaginationResponse;
import jp.co.nri.point.util.BeanCopyUtil;
import jp.co.nri.point.util.ExcelUtil;

@Api(tags = "雇员")
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EmployeeService service;

    @OperationLog("获取雇员列表")
    @ApiOperation("获取雇员列表")
    @GetMapping
    @ResponseBody
    public PageResultBean listEmployee(PaginationRequest request) {
        PaginationResponse pageResponse = new PaginationHandler(req -> service.count(req.getParams()),
                req -> service.list(req.getParams(), req.getOffset(), req.getLimit())).handle(request);
        return new PageResultBean(pageResponse.getRecordsTotal(), pageResponse.getData());
    }

    @ApiOperation("添加雇员")
    @PostMapping
    public @ResponseBody ResultBean<?> addEmployee(@RequestBody EmployeeVO employeeVo) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeVo, employee);
        employee.setDepartment(new Department(employeeVo.getDepartment()));
        long effectRow = service.save(employee);
        if (effectRow > 0) {
            logger.info("add employee successful.");
            return ResultBean.successResult();
        }
        return ResultBean.errorResult("add employee fail.");
    }

    @ApiOperation("删除雇员")
    @DeleteMapping("/{id}")
    public @ResponseBody ResultBean<?> deleteEmployee(@PathVariable Long id) {
        service.delete(id);
        logger.info("delete employee successful.");
        return ResultBean.successResult();
    }

    @ApiOperation("获取雇员")
    @GetMapping("/{id}")
    public @ResponseBody ResultBean<?> getEmployee(@PathVariable Long id) {
        return ResultBean.successResult(service.getById(id));
    }

    @ApiOperation("修改雇员")
    @PutMapping("/{id}")
    public @ResponseBody ResultBean<?> updateEmployee(@PathVariable Long id, @RequestBody EmployeeVO employeeVo) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeVo, employee);
        employee.setDepartment(new Department(employeeVo.getDepartment()));
        employee.setId(id);
        long effectRow = service.save(employee);
        if (effectRow > 0) {
            logger.info("update employee successful.");
            return ResultBean.successResult();
        }
        return ResultBean.errorResult("update employee fail.");
    }

    @ApiOperation("批量删除雇员")
    @PostMapping("/batch/delete")
    @ResponseBody
    public ResultBean<?> removeEmp(@RequestBody List<String> ids) {
        for (String id : ids) {
            service.delete(Long.parseLong(id));
        }
        return ResultBean.successResult();
    }

    @ApiOperation("导出雇员")
    @GetMapping("/export")
    public ResponseEntity<ByteArrayResource> exportEmployee(HttpServletRequest request) throws IOException {
        String contentType = "application/vnd.ms-excel";
        String excelFileName = "employee_";
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_MM_SS");
        excelFileName += df.format(new Date()) + ".txt";
        final List<Employee> list = service.list();
        List<EmployeeVO> voList = BeanCopyUtil.copyListProperties(list, EmployeeVO::new, (emp, empVo) -> {
            empVo.setDepartment(emp.getDepartment().getId());
        });
        logger.info("export excel file.");
        ByteArrayResource bytes = ExcelUtil.exportToFile(voList);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + excelFileName + "\"").body(bytes);
    }

    /**
     * 
     * @param file
     * @return
     * @throws IOException
     * @throws Exception
     */
    @ApiOperation("导入雇员")
    @PostMapping("/import")
    public @ResponseBody ResultBean<?> importEmployee(@RequestParam("file") MultipartFile file)
            throws IOException, Exception {

        final List<EmployeeVO> listObjects = ExcelUtil.importFromFile(file, EmployeeVO.class);

        List<Employee> empList = BeanCopyUtil.copyListProperties(listObjects, Employee::new, (empVo, emp) -> {
            emp.setDepartment(new Department(empVo.getDepartment()));
        });
        long ret = service.batchSaveEmployee(empList);
        if (ret == 1L) {
            logger.info("import excel file successful.");
            return ResultBean.successResult();
        }
        logger.error("import excel file fail.");
        return ResultBean.errorResult("import error");
    }

}
