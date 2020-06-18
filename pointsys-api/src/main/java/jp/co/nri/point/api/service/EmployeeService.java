package jp.co.nri.point.api.service;

import java.util.List;

import jp.co.nri.point.base.BaseService;
import jp.co.nri.point.api.domain.Employee;

public interface EmployeeService extends BaseService<Employee> {
    List<Employee> getAllByCondition(String username, String deptName, int pageNum, int pageSize);

    Long getCountByCondition(String username, String deptName);
    
    Long batchSaveEmployee(List<Employee> list);
}
