package jp.co.nri.point.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.nri.point.domain.Employee;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
