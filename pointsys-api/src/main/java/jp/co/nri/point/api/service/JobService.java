package jp.co.nri.point.api.service;

import org.quartz.JobDataMap;
import org.quartz.SchedulerException;

import jp.co.nri.point.base.BaseService;
import jp.co.nri.point.domain.JobModel;

public interface JobService extends BaseService<JobModel> {

    void saveJob(JobModel jobModel);

    void doJob(JobDataMap jobDataMap);

    void deleteJob(Long id) throws SchedulerException;

    JobModel getByName(String jobName);
}
