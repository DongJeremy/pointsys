package jp.co.nri.point.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.web.info.Server;
import jp.co.nri.point.web.util.HttpClientUtil;

@Controller
public class SysInfoController extends BaseController {

    @GetMapping("/sysInfo/current")
    public String getSysInfo(Model model) {
        ResultBean<Server> server = HttpClientUtil.doGetResultBean(restTemplate, getTokenString(),
                getUrlString("/api/sysInfo"), Server.class);
        model.addAttribute("server", server.getData());
        return "pages/system/sysinfo-list::serverInfo";
    }

}
