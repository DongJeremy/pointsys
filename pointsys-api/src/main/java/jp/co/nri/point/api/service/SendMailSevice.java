package jp.co.nri.point.api.service;

import java.util.List;

import javax.mail.MessagingException;

public interface SendMailSevice {

    void sendMail(List<String> toUser, String subject, String text);

    void sendMail(String toUser, String subject, String text) throws MessagingException;
}
