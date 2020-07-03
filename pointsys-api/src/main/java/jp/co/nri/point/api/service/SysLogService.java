package jp.co.nri.point.api.service;

import jp.co.nri.point.base.BaseService;
import jp.co.nri.point.domain.SysLog;

public interface SysLogService extends BaseService<SysLog> {
    public void clearLogs();
}
