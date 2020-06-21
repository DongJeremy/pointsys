package jp.co.nri.point.api.controller;

import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import jp.co.nri.point.api.domain.JobModel;
import jp.co.nri.point.api.service.JobService;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    @Autowired
    private JobService jobService;

    @PostMapping
    public void add(@RequestBody JobModel jobModel) {
        JobModel model = jobService.getByName(jobModel.getJobName());
        if (model != null) {
            throw new IllegalArgumentException(jobModel.getJobName() + "已存在");
        }

        jobModel.setIsSysJob(false);
        jobService.saveJob(jobModel);
    }

    @ApiOperation("修改定时任务")
    @PutMapping
    public void update(@RequestBody JobModel jobModel) {
        jobModel.setStatus(1);
        jobService.saveJob(jobModel);
    }

    @ApiOperation("删除定时任务")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws SchedulerException {
        jobService.deleteJob(id);
    }

    @ApiOperation("根据id获取定时任务")
    @GetMapping("/{id}")
    public JobModel getById(@PathVariable Long id) {
        return jobService.getById(id).orElse(new JobModel());
    }

    @ApiOperation(value = "校验cron表达式")
    @GetMapping(params = "cron")
    public boolean checkCron(String cron) {
        return CronExpression.isValidExpression(cron);
    }
}
