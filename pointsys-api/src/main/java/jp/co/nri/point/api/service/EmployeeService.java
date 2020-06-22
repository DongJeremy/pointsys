package jp.co.nri.point.api.service;

import java.util.List;

import jp.co.nri.point.api.domain.Employee;
import jp.co.nri.point.base.BaseService;

public interface EmployeeService extends BaseService<Employee> {

    Long batchSaveEmployee(List<Employee> list);
}
