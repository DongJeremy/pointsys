package jp.co.nri.point.base;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {

    Long selectCount();

    T selectByPrimaryKey(Long id);

    List<T> selectAll();

    Long deleteByPrimaryKey(Long id);

    Long insert(T entity);

    Long insertSelective(T entity);

    Long updateByPrimaryKey(T entity);

    Long selectCountByParams(Map<String, Object> params);

    List<T> selectAllByParams(Map<String, Object> params);
}
