package jp.co.nri.point.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.nri.point.beans.PaginationResponse;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.web.util.HttpClientUtil;

@Controller
public class SysLogController extends BaseController {

    @GetMapping("/syslog/list")
    @ResponseBody
    public PaginationResponse listSysLog(@RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "length", defaultValue = "10") int length) {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.set("start", String.valueOf(start));
        paramsMap.set("length", String.valueOf(length));
        return HttpClientUtil.doGetPageResultBean(restTemplate, getTokenString(), getUrlString("/api/v1/syslogs/list"),
                paramsMap);
    }
    
    @PostMapping("/syslog/clear")
    @ResponseBody
    public ResultBean<?> clearSysLogs() {
        return HttpClientUtil.doPostResultBean(restTemplate, getTokenString(), getUrlString("/api/v1/syslogs/clear"),
                null, Object.class);
    }

}
