package jp.co.nri.point.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jp.co.nri.point.annotation.OperationLog;
import jp.co.nri.point.beans.PageResultBean;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.domain.SysUser;
import jp.co.nri.point.dto.PasswordBean;
import jp.co.nri.point.dto.UserOnline;
import jp.co.nri.point.security.service.TokenService;
import jp.co.nri.point.security.service.UserService;
import jp.co.nri.point.util.UserUtil;

@Api(tags = "用户")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @ApiOperation(value = "根据用户id获取用户")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:query')")
    public SysUser user(@PathVariable Long id) {
        return userService.getById(id).orElse(new SysUser());
    }

    @ApiOperation(value = "当前登录用户")
    @GetMapping("/current")
    public SysUser currentUser(String token) {
        if (token != null) {
            return tokenService.getLoginUser(token);
        }
        return UserUtil.getLoginUser();
    }

    @OperationLog("获取在线用户列表")
    @GetMapping("/onlinelist")
    @ResponseBody
    public PageResultBean<UserOnline> listUsers(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<UserOnline> userOnlines = userService.getOnlineUsers(page, limit);
        long count = userService.getOnlineUserCount();
        return new PageResultBean<UserOnline>(count, userOnlines);
    }

    @PostMapping("/kickout/{sessionId}")
    @ResponseBody
    public ResultBean<?> forceLogout(@PathVariable("sessionId") String sessionId) {
        userService.forceLogout(sessionId);
        return ResultBean.successResult();
    }

    @OperationLog("获取用户列表")
    @GetMapping("/list")
    @ResponseBody
    public PageResultBean<SysUser> listUser(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<SysUser> emps = userService.getAll(page, limit);
        long count = userService.getCount(new SysUser());
        return new PageResultBean<SysUser>(count, emps);
    }

    @OperationLog("添加用户")
    @PostMapping
    @ResponseBody
    public ResultBean<Long> addUser(@RequestBody SysUser user) {
        return ResultBean.successResult(userService.save(user));
    }

    @OperationLog("编辑用户")
    @PutMapping
    @ResponseBody
    public ResultBean<?> updateUser(@RequestBody SysUser user) {
        userService.save(user);
        return ResultBean.successResult();
    }

    @PostMapping("/userInfo")
    @ResponseBody
    public ResultBean<?> userInfo(@RequestBody SysUser user) {
        // 保存数据
        userService.updateUserInfoByPrimaryKey(user);
        return ResultBean.successResult();
    }

    @OperationLog("刪除用户")
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResultBean<?> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResultBean.successResult();
    }

    @OperationLog("删除账号")
    @PostMapping("/{id}/disable")
    @ResponseBody
    public ResultBean<Boolean> disable(@PathVariable("id") Long id) {
        return ResultBean.successResult(userService.disableUserByID(id));
    }

    @OperationLog("激活账号")
    @PostMapping("/{id}/enable")
    @ResponseBody
    public ResultBean<Boolean> enable(@PathVariable("id") Long id) {
        return ResultBean.successResult(userService.enableUserByID(id));
    }

    @OperationLog("重置密码")
    @PostMapping("/{id}/reset")
    @ResponseBody
    public ResultBean<?> resetPassword(@PathVariable("id") Long id, @RequestBody PasswordBean passwordBean) {
        System.out.println(passwordBean);
        boolean resetPass = userService.updatePasswordByUserId(id, passwordBean.getOriginal(),
                passwordBean.getPassword());
        if (!resetPass) {
            return ResultBean.errorResult("原密码错误，重置密码失败");
        }
        return ResultBean.successResult();
    }
}
