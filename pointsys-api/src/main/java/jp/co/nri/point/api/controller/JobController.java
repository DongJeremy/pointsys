package jp.co.nri.point.api.controller;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jp.co.nri.point.annotation.JobTask;
import jp.co.nri.point.annotation.OperationLog;
import jp.co.nri.point.api.service.JobService;
import jp.co.nri.point.beans.PaginationRequest;
import jp.co.nri.point.beans.PaginationResponse;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.domain.JobModel;
import jp.co.nri.point.pagination.PaginationHandler;

@Tag(name = "定时任务")
@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping
    @OperationLog("获取雇员列表")
    @Operation(summary = "定时任务列表")
    public PaginationResponse list(PaginationRequest request) {
        int offset = request.getStart() / request.getLength() + 1;
        PaginationResponse pageResponse = new PaginationHandler(
                req -> jobService.list(req.getParams(), offset, req.getLength()).getTotal(),
                req -> jobService.list(req.getParams(), offset, req.getLength()).getList()).handle(request);
        return pageResponse;
    }

    @Operation(summary = "新建定时任务")
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

    @Operation(summary = "修改定时任务")
    @PutMapping("/{id}")
    public ResultBean<?> update(@PathVariable Long id, @RequestBody JobModel jobModel) {
        jobModel.setStatus(1);
        jobService.saveJob(jobModel);
        return ResultBean.successResult();
    }

    @Operation(summary = "删除定时任务")
    @DeleteMapping("/{id}")
    public ResultBean<?> delete(@PathVariable Long id) throws SchedulerException {
        jobService.deleteJob(id);
        return ResultBean.successResult();
    }

    @Operation(summary = "根据id获取定时任务")
    @GetMapping("/{id}")
    public ResultBean<?> getById(@PathVariable Long id) {
        return ResultBean.successResult(jobService.getById(id));
    }

    @Operation(summary = "校验cron表达式")
    @GetMapping("/cronCheck")
    public ResultBean<?> checkCron(@RequestParam("cron") String cron) {
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

    @Operation(summary = "springBean名字")
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
            if (clazz.isAnnotationPresent(JobTask.class) && str.toLowerCase().contains("task")) {
                list.add(str);
            }
        }
        Collections.sort(list);// 2018.07.26修改排序

        return ResultBean.successResult(list);
    }

    @Operation(summary = "springBean的无参方法")
    @GetMapping("/beans/{name}")
    public ResultBean<?> listMethodName(@PathVariable String name) {
        Class<?> clazz = getClass(name);
        Method[] methods = clazz.getDeclaredMethods();

        Set<String> names = new HashSet<>();
        Arrays.asList(methods).forEach(m -> {
            int b = m.getModifiers();
            if (Modifier.isPublic(b)) { // 2018.07.26修改public方法的判断
                Class<?>[] classes = m.getParameterTypes();
                if (classes.length == 0) {
                    names.add(m.getName());
                }
            }
        });

        return ResultBean.successResult(names);
    }
}
