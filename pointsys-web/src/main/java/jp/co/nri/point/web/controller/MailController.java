package jp.co.nri.point.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.nri.point.beans.PageResultBean;
import jp.co.nri.point.web.util.HttpClientUtil;

@Controller
public class MailController extends BaseController {

    @GetMapping("/main/mailList")
    public String mailListView() {
        return "pages/mail/mail-list";
    }

    @GetMapping("/mail/list")
    @ResponseBody
    public PageResultBean empList(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.set("page", String.valueOf(page));
        paramsMap.set("limit", String.valueOf(limit));
        PageResultBean server = HttpClientUtil.doGetPageResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/mails"), paramsMap);
        return server;
    }

}
