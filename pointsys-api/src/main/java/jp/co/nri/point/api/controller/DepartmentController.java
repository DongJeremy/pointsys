package jp.co.nri.point.api.controller;

import java.util.List;

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
import jp.co.nri.point.beans.PageResultBean;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.api.domain.Department;
import jp.co.nri.point.api.service.DepartmentService;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService service;

    @GetMapping
    public @ResponseBody PageResultBean<Department> listDepartment(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<Department> depts = service.getAll(page, limit);
        long count = service.getCount(new Department());
        return new PageResultBean<Department>(count, depts);
    }

    @OperationLog("添加部门")
    @PostMapping
    public @ResponseBody ResultBean<?> addDepartment(@RequestBody Department department) {
        service.save(department);
        return ResultBean.successResult();
    }

    @OperationLog("删除部门")
    @DeleteMapping("/{id}")
    public @ResponseBody ResultBean<?> deleteDepartment(@PathVariable Long id) {
        long deleteNo = service.deleteById(id);
        return deleteNo > 0 ? ResultBean.successResult() : ResultBean.errorResult();
    }

    @GetMapping("/{id}")
    public @ResponseBody ResultBean<?> getDepartment(@PathVariable Long id) {
        return ResultBean.successResult(service.getById(id).orElse(new Department()));
    }

    @OperationLog("更新部门")
    @PutMapping("/{id}")
    public @ResponseBody ResultBean<?> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        department.setId(id);
        service.save(department);
        return ResultBean.successResult();
    }

    @OperationLog("删除部门")
    @PostMapping("/batch/{id}")
    @ResponseBody
    public ResultBean<?> removeDepartment(@PathVariable("id") String[] ids) {
        for (String id : ids) {
            service.deleteById(Long.parseLong(id));
        }
        return ResultBean.successResult();
    }

    @GetMapping("/list")
    public @ResponseBody ResultBean<?> getDepartmentList() {
        return ResultBean.successResult(service.getAll());
    }

}
