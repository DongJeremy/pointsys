package jp.co.nri.point.api.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.nri.point.api.domain.Mail;
import jp.co.nri.point.api.domain.MailTo;
import jp.co.nri.point.api.mapper.MailMapper;
import jp.co.nri.point.api.service.MailService;
import jp.co.nri.point.api.service.SendMailSevice;
import jp.co.nri.point.base.BaseServiceImpl;
import jp.co.nri.point.util.UserUtil;

@Service
public class MailServiceImpl extends BaseServiceImpl<MailMapper, Mail> implements MailService {

    private static final Logger log = LoggerFactory.getLogger("adminLogger");

    @Autowired
    private SendMailSevice sendMailSevice;

    @Autowired
    private MailMapper mailMapper;

    @Override
    @Transactional
    public void save(Mail mail, List<String> toUser) {
        mail.setUserId(UserUtil.getLoginUser().getId());
        mailMapper.insert(mail);

        toUser.forEach(u -> {
            int status = 1;
            try {
                sendMailSevice.sendMail(u, mail.getSubject(), mail.getContent());
            } catch (Exception e) {
                log.error("发送邮件失败", e);
                status = 0;
            }

            mailMapper.saveToUser(mail.getId(), u, status);
        });

    }

    @Override
    public List<MailTo> getToUsers(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
