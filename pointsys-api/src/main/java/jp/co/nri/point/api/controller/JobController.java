package jp.co.nri.point.api.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jp.co.nri.point.annotation.OperationLog;
import jp.co.nri.point.api.domain.JobModel;
import jp.co.nri.point.api.service.JobService;
import jp.co.nri.point.beans.PaginationRequest;
import jp.co.nri.point.beans.PaginationResponse;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.pagination.PaginationHandler;

@Api(tags = "定时任务")
@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping
    @OperationLog("获取雇员列表")
    @ApiOperation(value = "定时任务列表")
    public PaginationResponse list(PaginationRequest request) {
        int offset = request.getStart() / request.getLength() + 1;
        PaginationResponse pageResponse = new PaginationHandler(req -> jobService.count(req.getParams()),
                req -> jobService.list(req.getParams(), offset, req.getLength())).handle(request);
        return pageResponse;
    }

    @ApiOperation("新建定时任务")
    @PostMapping
    public ResultBean<?> add(@RequestBody JobModel jobModel) {
        JobModel model = jobService.getByName(jobModel.getJobName());
        if (model != null) {
            throw new IllegalArgumentException(jobModel.getJobName() + "已存在");
        }

        jobModel.setIsSysJob(false);
        jobService.saveJob(jobModel);
        return ResultBean.successResult();
    }

    @ApiOperation("修改定时任务")
    @PutMapping
    public ResultBean<?> update(@RequestBody JobModel jobModel) {
        jobModel.setStatus(1);
        jobService.saveJob(jobModel);
        return ResultBean.successResult();
    }

    @ApiOperation("删除定时任务")
    @DeleteMapping("/{id}")
    public ResultBean<?> delete(@PathVariable Long id) throws SchedulerException {
        jobService.deleteJob(id);
        return ResultBean.successResult();
    }

    @ApiOperation("根据id获取定时任务")
    @GetMapping("/{id}")
    public ResultBean<?> getById(@PathVariable Long id) {
        return ResultBean.successResult(jobService.getById(id));
    }

    @ApiOperation(value = "校验cron表达式")
    @GetMapping(params = "cron")
    public ResultBean<?> checkCron(String cron) {
        return ResultBean.successResult(CronExpression.isValidExpression(cron));
    }

    private Class<?> getClass(String name) {
        Object object = applicationContext.getBean(name);
        Class<?> clazz = object.getClass();
        if (AopUtils.isAopProxy(object)) {
            clazz = clazz.getSuperclass();
        }

        return clazz;
    }

    @ApiOperation(value = "springBean名字")
    @GetMapping("/beans")
    public ResultBean<?> listAllBeanName() {
        String[] strings = applicationContext.getBeanDefinitionNames();
        List<String> list = new ArrayList<>();
        for (String str : strings) {
            if (str.contains(".")) {
                continue;
            }

            Class<?> clazz = getClass(str);

            // 2018.07.26修改 上面注释的add添加了太多不认识的bean，改为下面的判断我们只添加service，bean少了不少
            if (clazz.isAnnotationPresent(Service.class) && str.toLowerCase().contains("service")) {
                list.add(str);
            }
        }
        Collections.sort(list);// 2018.07.26修改排序

        return ResultBean.successResult(list);
    }
}
