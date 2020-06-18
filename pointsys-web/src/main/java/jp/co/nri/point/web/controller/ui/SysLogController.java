package jp.co.nri.point.web.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.nri.point.beans.PageResultBean;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.domain.SysLog;
import jp.co.nri.point.web.util.HttpClientUtil;

@Controller
public class SysLogController extends BaseController {

    @GetMapping("/syslog/list")
    @ResponseBody
    public PageResultBean<SysLog> listSysLog(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.set("page", String.valueOf(page));
        paramsMap.set("limit", String.valueOf(limit));
        return HttpClientUtil.doGetPageResultBean(restTemplate, getTokenString(), getUrlString("/api/syslog/list"),
                paramsMap, SysLog.class);
    }
    
    @PostMapping("/syslog/clear")
    @ResponseBody
    public ResultBean<?> clearSysLogs() {
        return HttpClientUtil.doPostResultBean(restTemplate, getTokenString(), getUrlString("/api/syslog/clear"),
                null, Object.class);
    }

}
