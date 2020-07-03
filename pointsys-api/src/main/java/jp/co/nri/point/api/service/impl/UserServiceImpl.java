package jp.co.nri.point.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;

import jp.co.nri.point.api.service.UserService;
import jp.co.nri.point.base.BaseServiceImpl;
import jp.co.nri.point.constant.Constants;
import jp.co.nri.point.domain.SysUser;
import jp.co.nri.point.domain.SysUser.Status;
import jp.co.nri.point.dto.LoginUser;
import jp.co.nri.point.dto.UserDto;
import jp.co.nri.point.dto.UserOnline;
import jp.co.nri.point.mapper.UserMapper;
import jp.co.nri.point.security.service.TokenService;
import jp.co.nri.point.util.EncryptPasswordUtil;
import jp.co.nri.point.util.IPUtils;
import jp.co.nri.point.util.UserUtil;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, SysUser> implements UserService {

    private static final Logger log = LoggerFactory.getLogger("adminLogger");

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Override
    @Transactional
    public SysUser saveUser(UserDto userDto) {
        SysUser user = userDto;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.VALID);
        userMapper.save(user);
        saveUserRoles(user.getId(), userDto.getRoleIds());

        log.debug("新增用户{}", user.getUsername());
        return user;
    }

    private void saveUserRoles(Long userId, List<Long> roleIds) {
        if (roleIds != null) {
            userMapper.deleteUserRole(userId);
            if (!CollectionUtils.isEmpty(roleIds)) {
                userMapper.saveUserRoles(userId, roleIds);
            }
        }
    }

    @Override
    public SysUser getUser(String username) {
        return userMapper.getUser(username);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        SysUser u = userMapper.getUser(username);
        if (u == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        if (!passwordEncoder.matches(oldPassword, u.getPassword())) {
            throw new IllegalArgumentException("旧密码错误");
        }

        userMapper.changePassword(u.getId(), passwordEncoder.encode(newPassword));

        log.debug("修改{}的密码", username);
    }

    @Override
    @Transactional
    public SysUser updateUser(UserDto userDto) {
        userMapper.update(userDto);
        saveUserRoles(userDto.getId(), userDto.getRoleIds());

        return userDto;
    }

    @Override
    public SysUser findUserInfoByUsername(String username) {
        return userMapper.selectUserInfoByUsername(username);
    }

    @Override
    public List<UserOnline> getOnlineUsers() {
        List<UserOnline> list = new ArrayList<>();
        Set<String> tokenSet = tokenService.getAllTokenList(Constants.REDIS_TOKEN_KEY + "*");
        for (String token : tokenSet) {
            UserOnline userOnline = new UserOnline();
            if (token.startsWith(Constants.REDIS_TOKEN_KEY)) {
                token = token.substring(7);
            }
            LoginUser user = tokenService.getLoginUser(token);
            if (user == null) {
                continue;
            }
            userOnline.setId(user.getToken());
            userOnline.setToken(token);
            userOnline.setUsername(user.getUsername());
            userOnline.setStartTimestamp(new Date(user.getLoginTime()));
            userOnline.setLastAccessTime(new Date(user.getLoginTime()));
            userOnline.setStatus("在线");
            userOnline.setIp(IPUtils.getIpAddr());
            list.add(userOnline);
        }
        return list;
    }

    @Override
    public List<UserOnline> getOnlineUsers(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return getOnlineUsers();
    }

    @Override
    public long getOnlineUserCount() {
        return getOnlineUsers().size();
    }

    @Override
    public void forceLogout(String sessionId) {
        LoginUser currentUser = UserUtil.getLoginUser();
        if (!currentUser.getToken().equals(sessionId)) {
            tokenService.deleteUUIDToken(sessionId);
        }
    }

    @Override
    public void updateUserInfoByPrimaryKey(SysUser user) {
        userMapper.updateUserInfoByPrimaryKey(user);

    }

    @Override
    public boolean updatePasswordByUserId(Long id, String password0, String password1) {
        SysUser u = userMapper.selectByPrimaryKey(id);
        if (u == null)
            return false;
        boolean passMatch = EncryptPasswordUtil.passwordsMatch(password0, u.getPassword());
        if (!passMatch) {
            return false;
        }
        String encryptPassword = EncryptPasswordUtil.encrypt(password1);
        userMapper.updatePasswordByUserId(id, encryptPassword);
        return true;
    }

    @Override
    public boolean enableUserByID(Long id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean disableUserByID(Long id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public SysUser findUserInfo() {
        LoginUser user = UserUtil.getLoginUser();
        return userMapper.selectUserInfoByUsername(user.getUsername());
    }

}
