package jp.co.nri.point.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.nri.point.base.BaseMapper;
import jp.co.nri.point.domain.SysLog;

@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {

    void clearLogs();

}
