package jp.co.nri.point.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.nri.point.beans.PageResultBean;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.domain.SysUser;
import jp.co.nri.point.dto.PasswordBean;
import jp.co.nri.point.web.dto.SessionInfo;
import jp.co.nri.point.web.util.HttpClientUtil;

@Controller
public class UserController extends BaseController {

    /**
     * 跳转到个人信息页面
     */
    @GetMapping("/main/userInfo")
    public String toUserInfo(Model model) {
        return "pages/user/user-info";
    }

    @GetMapping("/main/editpass")
    public String editPassword(ModelMap model) {
        return "pages/user/edit-passwd";
    }

    @GetMapping("/main/userOnline")
    public String userOnline(ModelMap model) throws Exception {
        return "pages/user/user-online-list";
    }

    @GetMapping("/main/userView")
    public String userView(ModelMap model) throws Exception {
        return "pages/user/user-list";
    }

    @GetMapping("/main/user/{id}/reset")
    public String resetPassword(@PathVariable("id") Integer id, ModelMap model) {
        model.addAttribute("id", id);
        return "pages/user/user-reset";
    }

    @GetMapping("/user/current")
    @ResponseBody
    public SysUser currentUser() {
        return HttpClientUtil.doGet(restTemplate, getTokenString(), getUrlString("/api/user/current"), SysUser.class);
    }

    @GetMapping("/user/list")
    @ResponseBody
    public PageResultBean listUser(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.set("page", String.valueOf(page));
        paramsMap.set("limit", String.valueOf(limit));
        return HttpClientUtil.doGetPageResultBean(restTemplate, getTokenString(), getUrlString("/api/user/list"),
                paramsMap);
    }

    @GetMapping("/user/onlinelist")
    @ResponseBody
    public PageResultBean listUsers(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.set("page", String.valueOf(page));
        paramsMap.set("limit", String.valueOf(limit));
        return HttpClientUtil.doGetPageResultBean(restTemplate, getTokenString(), getUrlString("/api/user/onlinelist"),
                paramsMap);
    }

    @PostMapping("/user/kickout")
    @ResponseBody
    public ResultBean<?> forceLogout(@RequestBody SessionInfo sessionInfo) {
        return HttpClientUtil.doPostResultBean(restTemplate, getTokenString(),
                getUrlString("/api/user/kickout/" + sessionInfo.getSessionId()), null, SessionInfo.class);
    }

    @PostMapping("/user/userInfo")
    @ResponseBody
    public ResultBean<?> userInfo(@RequestBody SysUser user) {
        return HttpClientUtil.doPostResultBean(restTemplate, getTokenString(),
                getUrlString("/api/user/userInfo"), user, SysUser.class);
    }
    
    @PostMapping("/user/{id}/reset")
    @ResponseBody
    public ResultBean<?> resetPassword(@PathVariable("id") Long id, @RequestBody PasswordBean passwordBean) {
        return HttpClientUtil.doPostResultBean(restTemplate, getTokenString(),
                getUrlString("/api/user/" + id + "/reset"), passwordBean, PasswordBean.class);
    }
}
