package jp.co.nri.point.api.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jp.co.nri.point.annotation.OperationLog;
import jp.co.nri.point.beans.PageResultBean;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.domain.SysLog;
import jp.co.nri.point.service.SysLogService;

@RestController
@RequestMapping("/api/syslog")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    @GetMapping("/list")
    @ResponseBody
    public PageResultBean<SysLog> listSysLogs(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<SysLog> loginLogs = sysLogService.getAll(page, limit);
        long count = sysLogService.getCount(new SysLog());
        return new PageResultBean<SysLog>(count, loginLogs);
    }

    @OperationLog("清空操作日志")
    @PostMapping("/clear")
    @ResponseBody
    public ResultBean<?> clearSysLogs() {
        sysLogService.clearLogs();
        return ResultBean.successResult();
    }

}