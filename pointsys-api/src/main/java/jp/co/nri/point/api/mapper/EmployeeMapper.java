package jp.co.nri.point.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import jp.co.nri.point.base.BaseMapper;
import jp.co.nri.point.api.domain.Employee;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    List<Employee> selectAllByCondition(@Param("name") String name, @Param("departmentName") String departmentName);

    Long selectCountByCondition(@Param("name") String name, @Param("departmentName") String departmentName);
}
