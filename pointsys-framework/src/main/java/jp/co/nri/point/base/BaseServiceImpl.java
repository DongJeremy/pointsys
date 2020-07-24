package jp.co.nri.point.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jp.co.nri.point.mapper.BaseMapper;

public class BaseServiceImpl<M extends BaseMapper<T>, T> implements BaseService<T> {

    protected Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    private M mapper;

    @Override
    public Long count() {
        return mapper.selectCount();
    }

    @Override
    public PageInfo<T> list(Map<String, Object> params, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(mapper.selectAllByParams(params));
    }

    @Override
    public T getById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> list() {
        return mapper.selectAll();
    }

    @Override
    public Long save(T entity) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            try {
                Method method = cls.getMethod("getId");
                Object idVal = method.invoke(entity);
                if (null != idVal) {
                    return mapper.updateByPrimaryKey(entity);
                } else {
                    return mapper.insert(entity);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return 0L;
    }

    @Override
    public Long delete(Long id) {
        return mapper.deleteByPrimaryKey(id);
    }

}
