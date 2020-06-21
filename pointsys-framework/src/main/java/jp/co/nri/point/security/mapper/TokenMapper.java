package jp.co.nri.point.security.mapper;

import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import jp.co.nri.point.domain.TokenModel;

@Mapper
public interface TokenMapper {
    @Insert("insert into t_token(id, val, expireTime, createTime, updateTime) values (#{id}, #{val}, #{expireTime}, #{createTime}, #{updateTime})")
    int save(TokenModel model);

    @Select("select * from t_token t where t.id = #{id}")
    TokenModel getById(String id);
    
    @Select("select distinct id from t_token t")
    Set<String> getAll();

    @Update("update t_token t set t.val = #{val}, t.expireTime = #{expireTime}, t.updateTime = #{updateTime} where t.id = #{id}")
    int update(TokenModel model);

    @Delete("delete from t_token where id = #{id}")
    int delete(String id);
}
