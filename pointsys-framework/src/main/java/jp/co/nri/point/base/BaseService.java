package jp.co.nri.point.base;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

public interface BaseService<T> {

    Long count();

    T getById(Long id);

    List<T> list();

    PageInfo<T> list(Map<String, Object> params, int pageNum, int pageSize);

    Long save(T entity);

    Long delete(Long id);
}
