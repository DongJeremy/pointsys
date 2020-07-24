package jp.co.nri.point.api.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jp.co.nri.point.annotation.OperationLog;
import jp.co.nri.point.api.service.SysLogService;
import jp.co.nri.point.beans.PaginationRequest;
import jp.co.nri.point.beans.PaginationResponse;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.pagination.PaginationHandler;

@Tag(name = "系统Log")
@RestController
@RequestMapping("/api/v1/syslogs")
public class SysLogController {

    @Resource
    private SysLogService service;

    @Operation(summary = "获取操作日志")
    @GetMapping("/list")
    public PaginationResponse listEmployee(PaginationRequest request) {
        int offset = request.getStart() / request.getLength() + 1;
        PaginationResponse pageResponse = new PaginationHandler(
                req -> service.list(req.getParams(), offset, req.getLength()).getTotal(),
                req -> service.list(req.getParams(), offset, req.getLength()).getList()).handle(request);
        return pageResponse;
    }

    @Operation(summary = "清空操作日志")
    @OperationLog("清空操作日志")
    @PostMapping("/clear")
    public ResultBean<?> clearSysLogs() {
        service.clearLogs();
        return ResultBean.successResult();
    }

}