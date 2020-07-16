package jp.co.nri.point.api.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jp.co.nri.point.api.info.Server;
import jp.co.nri.point.api.service.ServerInfoService;
import jp.co.nri.point.beans.ResultBean;

@Tag(name = "系统信息")
@RestController
@RequestMapping("/api/v1/sysInfo")
public class SysInfoController {
    
    @Resource
    private ServerInfoService serverInfoService;

    @Operation(summary = "获取系统信息")
    @GetMapping
    public ResultBean<Server> getSysInfo() {
        Server server = serverInfoService.getServerInfo();
        return ResultBean.successResult(server);
    }

}