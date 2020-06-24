package jp.co.nri.point.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.nri.point.beans.PaginationResponse;
import jp.co.nri.point.web.util.HttpClientUtil;

@Controller
public class MailController extends BaseController {

    @GetMapping("/main/mailList")
    public String mailListView() {
        return "pages/mail/mail-list";
    }

    @GetMapping("/mail/list")
    @ResponseBody
    public PaginationResponse empList(@RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "length", defaultValue = "10") int length) {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.set("start", String.valueOf(start));
        paramsMap.set("length", String.valueOf(length));
        PaginationResponse response = HttpClientUtil.doGetPageResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/mails"), paramsMap);
        return response;
    }

}
