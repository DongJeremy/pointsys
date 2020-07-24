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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jp.co.nri.point.api.service.MailService;
import jp.co.nri.point.beans.PaginationRequest;
import jp.co.nri.point.beans.PaginationResponse;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.domain.Mail;
import jp.co.nri.point.domain.MailTo;
import jp.co.nri.point.pagination.PaginationHandler;

@Tag(name = "邮件")
@RestController
@RequestMapping("/api/v1/mails")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping
    @Operation(summary = "保存邮件")
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

    @Operation(summary = "根据id获取邮件")
    @GetMapping("/{id}")
    public ResultBean<?> get(@PathVariable Long id) {
        return ResultBean.successResult(mailService.getById(id));
    }

    @Operation(summary = "根据id获取邮件发送详情")
    @GetMapping("/{id}/to")
    public ResultBean<?> getMailTo(@PathVariable Long id) {
        List<MailTo> mailTos = mailService.getToUsers(id);
        return ResultBean.successResult(mailTos);
    }

    @Operation(summary = "邮件列表")
    @GetMapping
    public PaginationResponse list(PaginationRequest request) {
        int offset = request.getStart() / request.getLength() + 1;
        PaginationResponse pageResponse = new PaginationHandler(
                req -> mailService.list(req.getParams(), offset, req.getLength()).getTotal(),
                req -> mailService.list(req.getParams(), offset, req.getLength()).getList()).handle(request);
        return pageResponse;
    }
}
