package jp.co.nri.point.api.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jp.co.nri.point.annotation.OperationLog;
import jp.co.nri.point.api.service.SysLogService;
import jp.co.nri.point.beans.PaginationRequest;
import jp.co.nri.point.beans.PaginationResponse;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.pagination.PaginationHandler;

@Api(tags = "系统Log")
@RestController
@RequestMapping("/api/syslog")
public class SysLogController {

    @Resource
    private SysLogService service;

    @ApiOperation(value = "获取操作日志")
    @GetMapping("/list")
    public PaginationResponse listEmployee(PaginationRequest request) {
        int offset = request.getStart() / request.getLength() + 1;
        PaginationResponse pageResponse = new PaginationHandler(req -> service.count(req.getParams()),
                req -> service.list(req.getParams(), offset, req.getLength())).handle(request);
        return pageResponse;
    }

    @ApiOperation(value = "清空操作日志")
    @OperationLog("清空操作日志")
    @PostMapping("/clear")
    public ResultBean<?> clearSysLogs() {
        service.clearLogs();
        return ResultBean.successResult();
    }

}