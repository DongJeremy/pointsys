package jp.co.nri.point.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.nri.point.domain.JobModel;

@Mapper
public interface JobMapper extends BaseMapper<JobModel> {

    JobModel getByName(@Param("jobName") String jobName);

}
