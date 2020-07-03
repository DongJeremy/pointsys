package jp.co.nri.point.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jp.co.nri.point.annotation.OperationLog;
import jp.co.nri.point.api.service.DepartmentService;
import jp.co.nri.point.beans.PaginationRequest;
import jp.co.nri.point.beans.PaginationResponse;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.domain.Department;
import jp.co.nri.point.pagination.PaginationHandler;

@Api(tags = "部门")
@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService service;

    @ApiOperation(value = "部门列表")
    @GetMapping
    public PaginationResponse listDepartment(PaginationRequest request) {
        int offset = request.getStart() / request.getLength() + 1;
        PaginationResponse pageResponse = new PaginationHandler(req -> service.count(req.getParams()),
                req -> service.list(req.getParams(), offset, req.getLength())).handle(request);
        return pageResponse;
    }

    @OperationLog("添加部门")
    @ApiOperation(value = "添加部门")
    @PostMapping
    public ResultBean<?> addDepartment(@RequestBody Department department) {
        service.save(department);
        return ResultBean.successResult();
    }

    @OperationLog("删除部门")
    @ApiOperation(value = "删除部门")
    @DeleteMapping("/{id}")
    public ResultBean<?> deleteDepartment(@PathVariable Long id) {
        long deleteNo = service.delete(id);
        return deleteNo > 0 ? ResultBean.successResult() : ResultBean.errorResult();
    }

    @ApiOperation(value = "查找部门")
    @GetMapping("/{id}")
    public ResultBean<?> getDepartment(@PathVariable Long id) {
        return ResultBean.successResult(service.getById(id));
    }

    @OperationLog("更新部门")
    @ApiOperation(value = "更新部门")
    @PutMapping("/{id}")
    public ResultBean<?> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        department.setId(id);
        service.save(department);
        return ResultBean.successResult();
    }

    @OperationLog("删除部门")
    @ApiOperation(value = "删除部门")
    @PostMapping("/batch/{id}")
    public ResultBean<?> removeDepartment(@PathVariable("id") String[] ids) {
        for (String id : ids) {
            service.delete(Long.parseLong(id));
        }
        return ResultBean.successResult();
    }

    @ApiOperation(value = "部门列表")
    @GetMapping("/list")
    public ResultBean<?> getDepartmentList() {
        return ResultBean.successResult(service.list());
    }

}
