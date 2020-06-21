package jp.co.nri.point.api.service;

import org.quartz.JobDataMap;
import org.quartz.SchedulerException;

import jp.co.nri.point.api.domain.JobModel;
import jp.co.nri.point.base.BaseService;

public interface JobService extends BaseService<JobModel> {

    void saveJob(JobModel jobModel);

    void doJob(JobDataMap jobDataMap);

    void deleteJob(Long id) throws SchedulerException;

    JobModel getByName(String jobName);
}
