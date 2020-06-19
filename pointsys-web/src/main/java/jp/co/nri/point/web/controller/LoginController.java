package jp.co.nri.point.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import jp.co.nri.point.beans.AuthenticationBean;
import jp.co.nri.point.web.util.HttpClientUtil;

@Controller
public class LoginController extends BaseController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public RedirectView loginPage() {
        return new RedirectView("/login");
    }

    @PostMapping("/login")
    @ResponseBody
    public String loginPost(@RequestParam("username") String username, @RequestParam("password") String password) {
        String urlString = httpClientProperties.getUrl() + "/login";
        return HttpClientUtil.doPost(restTemplate, urlString, new AuthenticationBean(username, password), String.class);
    }

    @PostMapping("/logout")
    @ResponseBody
    public String logout() {
        return HttpClientUtil.doPost(restTemplate, getTokenString(), getUrlString("/logout"), null, String.class);
    }

}
