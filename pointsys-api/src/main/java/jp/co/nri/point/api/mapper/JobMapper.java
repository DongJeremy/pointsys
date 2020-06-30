package jp.co.nri.point.api.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.nri.point.api.domain.JobModel;
import jp.co.nri.point.base.BaseMapper;

@Mapper
public interface JobMapper extends BaseMapper<JobModel> {

    JobModel getByName(String jobName);

}
