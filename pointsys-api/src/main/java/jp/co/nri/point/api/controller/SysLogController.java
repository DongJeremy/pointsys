package jp.co.nri.point.api.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jp.co.nri.point.annotation.OperationLog;
import jp.co.nri.point.beans.PageResultBean;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.pagination.PaginationHandler;
import jp.co.nri.point.pagination.PaginationRequest;
import jp.co.nri.point.pagination.PaginationResponse;
import jp.co.nri.point.service.SysLogService;

@Api(tags = "系统Log")
@RestController
@RequestMapping("/api/syslog")
public class SysLogController {

    @Resource
    private SysLogService service;

    @ApiOperation(value = "获取操作日志")
    @GetMapping("/list")
    @ResponseBody
    public PageResultBean listEmployee(PaginationRequest request) {
        PaginationResponse pageResponse = new PaginationHandler(req -> service.count(req.getParams()),
                req -> service.list(req.getParams(), req.getOffset(), req.getLimit())).handle(request);
        return new PageResultBean(pageResponse.getRecordsTotal(), pageResponse.getData());
    }

    @ApiOperation(value = "清空操作日志")
    @OperationLog("清空操作日志")
    @PostMapping("/clear")
    @ResponseBody
    public ResultBean<?> clearSysLogs() {
        service.clearLogs();
        return ResultBean.successResult();
    }

}