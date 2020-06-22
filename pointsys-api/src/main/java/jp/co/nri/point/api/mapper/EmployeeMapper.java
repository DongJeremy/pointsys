package jp.co.nri.point.api.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import jp.co.nri.point.base.BaseMapper;
import jp.co.nri.point.api.domain.Employee;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    List<Employee> selectAllByParams(@Param("params") Map<String, Object> params);

    Long selectCountByParams(@Param("params") Map<String, Object> params);
}
