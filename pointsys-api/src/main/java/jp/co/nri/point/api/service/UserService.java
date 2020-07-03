package jp.co.nri.point.api.service;

import java.util.List;

import jp.co.nri.point.base.BaseService;
import jp.co.nri.point.domain.SysUser;
import jp.co.nri.point.dto.UserDto;
import jp.co.nri.point.dto.UserOnline;

public interface UserService extends BaseService<SysUser> {

    SysUser saveUser(UserDto userDto);

    SysUser updateUser(UserDto userDto);

    SysUser getUser(String username);

    void changePassword(String username, String oldPassword, String newPassword);

    SysUser findUserInfoByUsername(String username);

    List<UserOnline> getOnlineUsers();

    List<UserOnline> getOnlineUsers(int page, int limit);

    long getOnlineUserCount();

    void forceLogout(String sessionId);

    void updateUserInfoByPrimaryKey(SysUser user);

    boolean updatePasswordByUserId(Long id, String original, String password);

    boolean enableUserByID(Long id);

    boolean disableUserByID(Long id);

    SysUser findUserInfo();

}
