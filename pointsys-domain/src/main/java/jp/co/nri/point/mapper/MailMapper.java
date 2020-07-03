package jp.co.nri.point.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import jp.co.nri.point.domain.Mail;
import jp.co.nri.point.domain.MailTo;

@Mapper
public interface MailMapper extends BaseMapper<Mail> {

    @Insert("insert into t_mail_to(mailId, toUser, status) values(#{mailId}, #{toUser}, #{status})")
    int saveToUser(@Param("mailId") Long mailId, @Param("toUser") String toUser, @Param("status") int status);

    @Select("select t.* from t_mail_to t where t.mailId = #{mailId}")
    List<MailTo> getToUsers(Long mailId);

}
