package jp.co.nri.point.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BaseMapper<T> {

    Long selectCount();

    T selectByPrimaryKey(Long id);

    List<T> selectAll();

    Long deleteByPrimaryKey(Long id);

    Long insert(T entity);

    Long insertSelective(T entity);

    Long updateByPrimaryKey(T entity);

    List<T> selectAllByParams(@Param("params") Map<String, Object> params);
}
