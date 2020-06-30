package jp.co.nri.point.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jp.co.nri.point.api.domain.Mail;
import jp.co.nri.point.api.domain.MailTo;
import jp.co.nri.point.api.service.MailService;
import jp.co.nri.point.beans.PaginationRequest;
import jp.co.nri.point.beans.PaginationResponse;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.pagination.PaginationHandler;

@Api(tags = "邮件")
@RestController
@RequestMapping("/api/v1/mails")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping
    @ApiOperation(value = "保存邮件")
    public ResultBean<?> save(@RequestBody Mail mail) {
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

        return ResultBean.successResult();
    }

    @ApiOperation(value = "根据id获取邮件")
    @GetMapping("/{id}")
    public ResultBean<?> get(@PathVariable Long id) {
        return ResultBean.successResult(mailService.getById(id));
    }

    @ApiOperation(value = "根据id获取邮件发送详情")
    @GetMapping("/{id}/to")
    public ResultBean<?> getMailTo(@PathVariable Long id) {
        List<MailTo> mailTos = mailService.getToUsers(id);
        return ResultBean.successResult(mailTos);
    }

    @ApiOperation(value = "邮件列表")
    @GetMapping
    public PaginationResponse list(PaginationRequest request) {
        int offset = request.getStart() / request.getLength() + 1;
        PaginationResponse pageResponse = new PaginationHandler(req -> mailService.count(req.getParams()),
                req -> mailService.list(req.getParams(), offset, req.getLength())).handle(request);
        return pageResponse;
    }
}
