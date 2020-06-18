package jp.co.nri.point.api.service.impl;

import org.springframework.stereotype.Service;

import jp.co.nri.point.base.BaseServiceImpl;
import jp.co.nri.point.api.domain.Department;
import jp.co.nri.point.api.mapper.DepartmentMapper;
import jp.co.nri.point.api.service.DepartmentService;

@Service
public class DepartmentServiceImpl extends BaseServiceImpl<DepartmentMapper, Department> implements DepartmentService {

}
