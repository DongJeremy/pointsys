package jp.co.nri.point.api.service;

import java.util.List;
import java.util.Map;

import jp.co.nri.point.api.domain.Mail;
import jp.co.nri.point.api.domain.MailTo;
import jp.co.nri.point.base.BaseService;

public interface MailService extends BaseService<Mail> {

    void save(Mail mail, List<String> toUser);

    List<MailTo> getToUsers(Long id);

    List<MailTo> list(Map<String, Object> params, Integer offset, Integer limit);

}
