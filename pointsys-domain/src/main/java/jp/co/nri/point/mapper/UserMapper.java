package jp.co.nri.point.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import jp.co.nri.point.domain.SysUser;
import jp.co.nri.point.dto.UserDto;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {

    void update(UserDto userDto);

    int changePassword(@Param("id") Long id, @Param("password") String password);

    @Select("select * from sys_user t where t.username = #{username}")
    SysUser getUser(@Param("username") String username);

    void deleteUserRole(Long userId);

    void saveUserRoles(Long userId, List<Long> roleIds);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT into sys_user(username, password, nickname, headImgUrl, phone, telephone, email, birthday, sex, "
            + "status, createTime, updateTime) values(#{username}, #{password}, #{nickname}, #{headImgUrl}, #{phone}, "
            + "#{telephone}, #{email}, #{birthday}, #{sex}, #{status}, now(), now())")
    int save(SysUser user);

    SysUser getById(Long id);

    SysUser selectUserInfoByUsername(String username);

    void updatePasswordByUserId(Long id, String encryptPassword);

    void updateUserInfoByPrimaryKey(SysUser user);

}
