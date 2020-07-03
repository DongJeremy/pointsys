package jp.co.nri.point.api.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.nri.point.api.service.EmployeeService;
import jp.co.nri.point.base.BaseServiceImpl;
import jp.co.nri.point.domain.Employee;
import jp.co.nri.point.mapper.EmployeeMapper;

@Service
public class EmployeeServiceImpl extends BaseServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public Long batchSaveEmployee(List<Employee> list) {
        SqlSession batchSqlSession = null;
        try {
            long beginTime = System.currentTimeMillis();
            batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            int batchCount = 500;
            for (int index = 0; index < list.size(); index++) {
                Employee employee = list.get(index);
                batchSqlSession.getMapper(EmployeeMapper.class).insert(employee);
                if (index != 0 && index % batchCount == 0) {
                    batchSqlSession.commit();
                }
            }
            batchSqlSession.commit();
            long endTime = System.currentTimeMillis();
            logger.info("插入完成， 耗时 " + (endTime - beginTime) + " 毫秒！");
        } finally {
            if (batchSqlSession != null) {
                batchSqlSession.close();
            }
        }
        return 1L;
    }

}
