package jp.co.nri.point.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import jp.co.nri.point.base.BaseServiceImpl;
import jp.co.nri.point.domain.SysLog;
import jp.co.nri.point.mapper.SysLogMapper;
import jp.co.nri.point.service.SysLogService;

@Service("sysLogService")
public class SysLogServiceImpl extends BaseServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

    @Override
    public void clearLogs() {
        sysLogMapper.clearLogs();
    }

}
