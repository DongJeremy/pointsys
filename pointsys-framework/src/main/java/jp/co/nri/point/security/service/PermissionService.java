package jp.co.nri.point.security.service;

import jp.co.nri.point.domain.Permission;

public interface PermissionService {

    void save(Permission permission);

    void update(Permission permission);

    void delete(Long id);
}
