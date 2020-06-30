package jp.co.nri.point.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.nri.point.beans.PaginationResponse;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.web.util.HttpClientUtil;

@Controller
public class JobController extends BaseController {

    @GetMapping("/main/jobView")
    public String mailListView() {
        return "pages/job/job-list";
    }

    @GetMapping("/main/jobAdd")
    public String jobAddView() {
        return "pages/job/job-add";
    }

    @GetMapping("/jobs/list")
    @ResponseBody
    public PaginationResponse jobList(@RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "length", defaultValue = "10") int length) {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.set("start", String.valueOf(start));
        paramsMap.set("length", String.valueOf(length));
        if (status != null) {
            paramsMap.set("status", status);
        }
        if (description != null) {
            paramsMap.set("description", description);
        }
        PaginationResponse response = HttpClientUtil.doGetPageResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/jobs"), paramsMap);
        return response;
    }

    @GetMapping("/jobs/beans")
    @ResponseBody
    public ResultBean<?> jobBeans() {
        ResultBean<?> response = HttpClientUtil.doGetResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/jobs/beans"), List.class);
        return response;
    }

}
