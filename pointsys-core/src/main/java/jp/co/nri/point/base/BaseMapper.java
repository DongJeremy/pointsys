package jp.co.nri.point.base;

import java.util.List;

public interface BaseMapper<T> {

    Long selectCount(T entity);

    T selectByPrimaryKey(Long id);

    List<T> selectAll();

    Long deleteByPrimaryKey(Long id);

    Long insert(T entity);

    Long insertSelective(T entity);

    Long updateByPrimaryKey(T entity);
}
