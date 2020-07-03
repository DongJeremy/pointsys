package jp.co.nri.point.api.service;

import java.util.List;

import jp.co.nri.point.base.BaseService;
import jp.co.nri.point.domain.Employee;

public interface EmployeeService extends BaseService<Employee> {

    Long batchSaveEmployee(List<Employee> list);
}
