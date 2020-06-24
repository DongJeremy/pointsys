package jp.co.nri.point.web.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jp.co.nri.point.beans.PaginationResponse;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.util.FileUtil;
import jp.co.nri.point.web.domain.Employee;
import jp.co.nri.point.web.domain.EmployeeVO;
import jp.co.nri.point.web.dto.DeptList;
import jp.co.nri.point.web.util.HttpClientUtil;

@Controller
public class EmployeeController extends BaseController {

    @Resource
    private HttpServletResponse response;

    @GetMapping("/main/empView")
    public String empListView() {
        return "pages/emp/emp-list";
    }

    @GetMapping(value = { "/main/empChangeView/{id}", "/main/empChangeView" })
    public String empUpdatePage(ModelMap model, @PathVariable(value = "id", required = false) Long id)
            throws Exception {
        return "pages/emp/emp-add";
    }

    @GetMapping("/employee/list")
    @ResponseBody
    public PaginationResponse empList(@RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "length", defaultValue = "10") int length) {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.set("start", String.valueOf(start));
        paramsMap.set("length", String.valueOf(length));
        if (username != null) {
            paramsMap.set("username", username);
        }
        if (department != null) {
            paramsMap.set("department", department);
        }
        PaginationResponse response = HttpClientUtil.doGetPageResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/employees"), paramsMap);
        return response;
    }

    @PostMapping("/employee/add")
    @ResponseBody
    public ResultBean<?> addEmployee(@RequestBody EmployeeVO employeeVo) {
        return HttpClientUtil.doPostResultBean(restTemplate, getTokenString(), getUrlString("/api/v1/employees"),
                employeeVo, EmployeeVO.class);
    }

    @GetMapping(value = { "/employee/get/{id}", "/employee/get" })
    public String getEmployee(@PathVariable(value = "id", required = false) Long id, Model model) {
        if (id != null) {
            ResultBean<Employee> employeeData = HttpClientUtil.doGetResultBean(restTemplate, getTokenString(),
                    getUrlString("/api/v1/employees/" + id), Employee.class);
            model.addAttribute("employee", employeeData.getData());
        }
        ResultBean<DeptList> departmentData = HttpClientUtil.doGetResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/departments/list"), DeptList.class);
        model.addAttribute("departmentList", departmentData.getData());
        return "pages/emp/emp-add::empInfo";
    }

    @GetMapping("/employeeDetails/{id}")
    public String getEmployeeDetails(@PathVariable Long id, Model model) {
        ResultBean<Employee> empData = HttpClientUtil.doGetResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/employees/" + id), Employee.class);
        model.addAttribute("employee", empData.getData());
        return "pages/emp/emp-details::empInfo";
    }

    @GetMapping("/employee/getDeptList")
    public String getDepartmentList(Model model) {
        ResultBean<DeptList> departmentData = HttpClientUtil.doGetResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/departments/list"), DeptList.class);
        model.addAttribute("departmentList", departmentData.getData());
        return "pages/emp/emp-list::deptInfo";
    }

    @PutMapping("/employee/update/{id}")
    @ResponseBody
    public ResultBean<?> updateEmployee(@PathVariable Long id, @RequestBody EmployeeVO employee) {
        return HttpClientUtil.doUpdateResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/employees/" + id), employee, EmployeeVO.class);
    }

    @DeleteMapping("/employee/delete/{id}")
    @ResponseBody
    public ResultBean<?> deleteEmployee(@PathVariable Long id) {
        return HttpClientUtil.doDeleteResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/employees/" + id), Employee.class);
    }

    @PostMapping("/employee/batch/delete")
    @ResponseBody
    public ResultBean<?> deleteBatchEmployee(@RequestBody List<String> ids) {
        return HttpClientUtil.doPostListResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/employees/batch/delete"), ids, String.class);
    }

    @GetMapping("/main/empDetailsView/{id}")
    public String empDetailsView(ModelMap model, @PathVariable("id") Long id) throws Exception {
        return "pages/emp/emp-details";
    }

    @GetMapping("/employee/excel/download")
    @ResponseBody
    public ResultBean<?> exportEmployee(HttpServletResponse response) throws Exception {
        String excelFileName = "employee";
        try {
            ByteArrayResource result = HttpClientUtil.doExportFile(getTokenString(),
                    getUrlString("/api/v1/employees/export"), ByteArrayResource.class);
            FileUtil.saveExcelToFile(result.getInputStream(), response, excelFileName);
        } catch (Exception e) {
            throw e;
        }
        return ResultBean.successResult();
    }

    @PostMapping("/employee/excel/upload")
    public @ResponseBody ResultBean<?> importEmployee(@RequestParam("file") MultipartFile file)
            throws IOException, Exception {

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        parts.add("file", file.getResource());

        return HttpClientUtil.doImportFile(getTokenString(), getUrlString("/api/v1/employees/import"), parts,
                String.class);
    }

}