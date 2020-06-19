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
import jp.co.nri.point.web.domain.Department;
import jp.co.nri.point.web.util.HttpClientUtil;

@Controller
public class DepartmentController extends BaseController {

    @GetMapping("/main/deptView")
    public String deptListView(ModelMap model) throws Exception {
        return "pages/dept/dept-list";
    }

    @GetMapping(value = { "/main/deptChangeView/{id}", "/main/deptChangeView" })
    public String deptUpdatePage(ModelMap model, @PathVariable(value = "id", required = false) Long id)
            throws Exception {
        return "pages/dept/dept-add";
    }

    @GetMapping("/department/list")
    @ResponseBody
    public PageResultBean<Department> listDepartment(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.set("page", String.valueOf(page));
        paramsMap.set("limit", String.valueOf(limit));
        return HttpClientUtil.doGetPageResultBean(restTemplate, getTokenString(), getUrlString("/api/v1/departments"),
                paramsMap, Department.class);
    }

    @PostMapping("/department/add")
    @ResponseBody
    public ResultBean<?> addDepartment(@RequestBody Department department) {
        return HttpClientUtil.doPostResultBean(restTemplate, getTokenString(), getUrlString("/api/v1/departments"),
                department, Department.class);
    }

    @GetMapping("/department/get/{id}")
    public String getDepartment(@PathVariable Long id, Model model) {
        ResultBean<Department> departmentData = HttpClientUtil.doGetResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/departments/" + id), Department.class);
        model.addAttribute("department", departmentData.getData());
        return "pages/dept/dept-add::deptInfo";
    }

    @PutMapping("/department/update/{id}")
    @ResponseBody
    public ResultBean<?> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return HttpClientUtil.doUpdateResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/departments/" + id), department, Department.class);
    }

    @DeleteMapping("/department/delete/{id}")
    @ResponseBody
    public ResultBean<?> deleteDepartment(@PathVariable Long id) {
        return HttpClientUtil.doDeleteResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/departments/" + id), Department.class);
    }

}
