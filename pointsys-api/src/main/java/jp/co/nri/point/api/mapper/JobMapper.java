package jp.co.nri.point.api.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.nri.point.api.domain.JobModel;
import jp.co.nri.point.base.BaseMapper;

@Mapper
public interface JobMapper extends BaseMapper<JobModel> {

    int save(JobModel jobModel);

    JobModel getById(Long id);

    JobModel getByName(String jobName);

    int update(JobModel jobModel);

    int count(@Param("params") Map<String, Object> params);

    List<JobModel> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
            @Param("limit") Integer limit);
}
