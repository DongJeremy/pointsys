package jp.co.nri.point.api.service.impl;

import org.springframework.stereotype.Service;

import jp.co.nri.point.api.service.DepartmentService;
import jp.co.nri.point.base.BaseServiceImpl;
import jp.co.nri.point.domain.Department;
import jp.co.nri.point.mapper.DepartmentMapper;

@Service
public class DepartmentServiceImpl extends BaseServiceImpl<DepartmentMapper, Department> implements DepartmentService {

}
