package jp.co.nri.point.api.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.nri.point.api.domain.Employee;
import jp.co.nri.point.base.BaseMapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
