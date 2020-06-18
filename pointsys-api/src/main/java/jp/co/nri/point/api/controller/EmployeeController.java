package jp.co.nri.point.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import jp.co.nri.point.annotation.OperationLog;
import jp.co.nri.point.api.domain.Department;
import jp.co.nri.point.api.domain.Employee;
import jp.co.nri.point.api.dto.EmployeeVO;
import jp.co.nri.point.api.service.EmployeeService;
import jp.co.nri.point.beans.PageResultBean;
import jp.co.nri.point.beans.ResultBean;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EmployeeService service;

    @OperationLog("获取用户列表")
    @GetMapping()
    public @ResponseBody PageResultBean<Employee> listEmployee(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<Employee> emps = null;
        long count = 0;
        if ((department == null) && (username == null)) {
            emps = service.getAll(page, limit);
            count = service.getCount(new Employee());
        } else {
            System.out.println(username);
            emps = service.getAllByCondition(username, department, page, limit);
            count = service.getCountByCondition(username, department);
        }
        return new PageResultBean<Employee>(count, emps);
    }

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

    @DeleteMapping("/{id}")
    public @ResponseBody ResultBean<?> deleteEmployee(@PathVariable Long id) {
        service.deleteById(id);
        logger.info("delete employee successful.");
        return ResultBean.successResult();
    }

    @GetMapping("/{id}")
    public @ResponseBody ResultBean<?> getEmployee(@PathVariable Long id) {
        return ResultBean.successResult(service.getById(id).orElse(new Employee()));
    }

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

    @DeleteMapping("/batch/{id}")
    @ResponseBody
    public ResultBean<?> removeEmp(@PathVariable("id") String[] userIds) {
        for (String id : userIds) {
            service.deleteById(Long.parseLong(id));
        }
        return ResultBean.successResult();
    }

}
