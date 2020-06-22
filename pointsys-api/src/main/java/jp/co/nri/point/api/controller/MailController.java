package jp.co.nri.point.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jp.co.nri.point.api.domain.Mail;
import jp.co.nri.point.api.domain.MailTo;
import jp.co.nri.point.api.service.MailService;
import jp.co.nri.point.pagination.PaginationHandler;
import jp.co.nri.point.pagination.PaginationRequest;
import jp.co.nri.point.pagination.PaginationResponse;

@Api(tags = "邮件")
@Controller
@RequestMapping("/api/v1/mails")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping
    @ApiOperation(value = "保存邮件")
    public Mail save(@RequestBody Mail mail) {
        String toUsers = mail.getToUsers().trim();
        if (StringUtils.isBlank(toUsers)) {
            throw new IllegalArgumentException("收件人不能为空");
        }

        toUsers = toUsers.replace(" ", "");
        toUsers = toUsers.replace("；", ";");
        String[] strings = toUsers.split(";");

        List<String> toUser = Arrays.asList(strings);
        toUser = toUser.stream().filter(u -> !StringUtils.isBlank(u)).map(u -> u.trim()).collect(Collectors.toList());
        mailService.save(mail, toUser);

        return mail;
    }

    @ApiOperation(value = "根据id获取邮件")
    @GetMapping("/{id}")
    public Mail get(@PathVariable Long id) {
        return mailService.getById(id);
    }

    @ApiOperation(value = "根据id获取邮件发送详情")
    @GetMapping("/{id}/to")
    public List<MailTo> getMailTo(@PathVariable Long id) {
        return mailService.getToUsers(id);
    }

    @ApiOperation(value = "邮件列表")
    @GetMapping
    public PaginationResponse list(PaginationRequest request) {
        return new PaginationHandler(req -> mailService.count(req.getParams()),
                req -> mailService.list(req.getParams(), req.getOffset(), req.getLimit())).handle(request);
    }
}
