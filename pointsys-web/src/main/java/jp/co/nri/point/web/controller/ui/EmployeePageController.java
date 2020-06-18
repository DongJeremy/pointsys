package jp.co.nri.point.web.controller.ui;

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

import jp.co.nri.point.beans.PageResultBean;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.web.domain.Employee;
import jp.co.nri.point.web.domain.EmployeeVO;
import jp.co.nri.point.web.dto.DeptList;
import jp.co.nri.point.web.util.HttpClientUtil;

@Controller
public class EmployeePageController extends BaseController {

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
    public PageResultBean<Employee> empList(@RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.set("page", String.valueOf(page));
        paramsMap.set("limit", String.valueOf(limit));
        if (username != null) {
            paramsMap.set("username", username);
        }
        if (department != null) {
            paramsMap.set("department", department);
        }
        PageResultBean<Employee> server = HttpClientUtil.doGetPageResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/employees"), paramsMap, Employee.class);
        return server;
    }

    @PostMapping("/employee/add")
    @ResponseBody
    public ResultBean<?> addEmployee(@RequestBody EmployeeVO employeeVo) {
        return HttpClientUtil.doPostResultBean(restTemplate, getTokenString(), getUrlString("/api/v1/employees"),
                employeeVo, EmployeeVO.class);
    }

    @GetMapping(value = { "/employee/get/{id}", "/employee/get" })
    public String getEmployee(@PathVariable(value = "id", required = false) Long id, Model model) {
        if(id!=null) {
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

    @GetMapping("/main/empDetailsView/{id}")
    public String empDetailsView(ModelMap model, @PathVariable("id") Long id) throws Exception {
        return "pages/emp/emp-details";
    }

//    @GetMapping("/empView/excel/download")
//    public void empViewDownload(HttpServletResponse response) throws IOException {
//        String excelFileName = "employee";
//        final List<Employee> list = employeeService.getAll();
//        List<Employee> voList = new ArrayList<>();
//        BeanUtils.copyProperties(list, voList);
//        logger.info("download excel file to local.");
//        ExcelUtil.exportToFile(voList, excelFileName, response);
//    }
//
//    @PostMapping("/empImport")
//    public @ResponseBody ResultBean<?> importEmployee(HttpServletRequest request) throws IOException, Exception {
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//        MultipartFile file = multipartRequest.getFile("file");
//        final List<EmployeeVO> listObjects = ExcelUtil.importFromFile(file, EmployeeVO.class);
//        List<Employee> employeeList = new ArrayList<>();
//        BeanUtils.copyProperties(listObjects, employeeList);
//        long ret = employeeService.batchSaveEmployee(employeeList);
//        if (ret == 1L) {
//            logger.info("upload excel file successful.");
//            return ResultBean.successResult();
//        }
//        logger.error("upload excel file fail.");
//        return ResultBean.errorResult("import error");
//    }
}