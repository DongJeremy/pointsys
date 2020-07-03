package jp.co.nri.point.base;

import java.util.List;
import java.util.Map;

public interface BaseService<T> {

    Long count();

    Long count(Map<String, Object> params);

    T getById(Long id);

    List<T> list();

    List<T> list(Map<String, Object> params, int pageNum, int pageSize);

    Long save(T entity);

    Long delete(Long id);
}
