package jp.co.nri.point.api.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jp.co.nri.point.api.info.Server;
import jp.co.nri.point.api.service.ServerInfoService;
import jp.co.nri.point.beans.ResultBean;

@RestController
@RequestMapping("/api/sysInfo")
public class SysInfoController {
    
    @Resource
    private ServerInfoService serverInfoService;

    @GetMapping
    @ResponseBody
    public ResultBean<Server> getSysInfo() {
        Server server = serverInfoService.getServerInfo();
        return ResultBean.successResult(server);
    }

}