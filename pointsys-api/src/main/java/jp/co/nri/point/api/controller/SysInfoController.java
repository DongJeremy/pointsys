package jp.co.nri.point.api.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jp.co.nri.point.api.info.Server;
import jp.co.nri.point.api.service.ServerInfoService;
import jp.co.nri.point.beans.ResultBean;

@Api(tags = "系统信息")
@RestController
@RequestMapping("/api/sysInfo")
public class SysInfoController {
    
    @Resource
    private ServerInfoService serverInfoService;

    @ApiOperation(value = "获取系统信息")
    @GetMapping
    @ResponseBody
    public ResultBean<Server> getSysInfo() {
        Server server = serverInfoService.getServerInfo();
        return ResultBean.successResult(server);
    }

}