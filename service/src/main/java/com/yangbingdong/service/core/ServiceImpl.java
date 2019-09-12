package com.yangbingdong.service.core;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.youngbingdong.redisoper.core.GenericRedisoper;
import com.youngbingdong.redisoper.core.RedisoperAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ybd
 * @date 2019/9/3
 * @contact yangbingdong1994@gmail.com
 */
@SuppressWarnings("unchecked")
@Slf4j
public class ServiceImpl<M extends CustomBaseMapper<T>, T> implements Service<T>, RedisoperAware<T> {

    protected Log logger = LogFactory.getLog(getClass());

    @Resource
    protected M baseMapper;

    private Class<T> entityClass;
    private GenericRedisoper<T> redisoper;

    @Override
    public M getBaseMapper() {
        return baseMapper;
    }

    /**
     * 判断数据库操作是否成功
     *
     * @param result 数据库操作返回影响条数
     * @return boolean
     */
    protected boolean retBool(Integer result) {
        return SqlHelper.retBool(result);
    }

    protected Class<T> currentModelClass() {
        return entityClass;
    }

    /**
     * 批量操作 SqlSession
     */
    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(currentModelClass());
    }

    /**
     * 释放sqlSession
     *
     * @param sqlSession session
     */
    protected void closeSqlSession(SqlSession sqlSession) {
        SqlSessionUtils.closeSqlSession(sqlSession, GlobalConfigUtils.currentSessionFactory(currentModelClass()));
    }

    /**
     * 获取 SqlStatement
     *
     * @param sqlMethod ignore
     * @return ignore
     */
    protected String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(currentModelClass()).getSqlStatement(sqlMethod.getMethod());
    }


    @Override
    public boolean save(T entity) {
        try {
            injectId(entity);
            boolean retBool = retBool(baseMapper.insert(entity));
            if (retBool) {
                redisoper.set2Redis(entity);
            }
            return retBool;
        } catch (DuplicateKeyException e) {
            Long maxId = baseMapper.selectMaxId() + 1;
            injectId(entity, maxId);
            boolean retBool = retBool(baseMapper.insert(entity));
            if (retBool) {
                redisoper.set2Redis(entity);
                redisoper.resetIncrId(maxId);
            }
            return retBool;
        }
    }

    private void injectId(T entity) {
        Long entityId = (Long) getEntityId(entity);
        if (entityId == null) {
            entityId = getMaxId();
            injectId(entity, entityId);
        }
    }

    private Serializable getEntityId(T t) {
        return (Serializable) redisoper.getEntityMetadata().getPrimaryValue(t);
    }

    @Override
    public Long getMaxId() {
        return redisoper.incrId(() -> baseMapper.selectMaxId() + 1);
    }

    private void injectId(T entity, Long id) {
        redisoper.getEntityMetadata().injectPrimaryValue(entity, id);
    }

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (T anEntityList : entityList) {
                injectId(anEntityList);
                batchSqlSession.insert(sqlStatement, anEntityList);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        redisoper.batchSet2Redis(new ArrayList<>(entityList));
        return true;
    }

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdate(T entity) {
        if (null != entity) {
            Object idVal = getEntityId(entity);
            return idVal == null ? save(entity) : updateById(entity);
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        /*Assert.notEmpty(entityList, "error: entityList must not be empty");
        Class<?> cls = currentModelClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (T entity : entityList) {
                Object idVal = ReflectionKit.getMethodValue(cls, entity, keyProperty);
                if (StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal))) {
                    batchSqlSession.insert(sqlStatement(SqlMethod.INSERT_ONE), entity);
                } else {
                    MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                    param.put(Constants.ENTITY, entity);
                    batchSqlSession.update(sqlStatement(SqlMethod.UPDATE_BY_ID), param);
                }
                // 不知道以后会不会有人说更新失败了还要执行插入 😂😂😂
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;*/
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeById(Serializable id) {
        T toBeDelete = getById(id);
        boolean b = SqlHelper.retBool(baseMapper.deleteById(id));
        if (b) {
            redisoper.deleteInRedis(toBeDelete);
        }
        return b;
    }

    @Override
    public boolean updateById(T entity) {
        boolean retBool = retBool(baseMapper.updateById(entity));
        if (retBool) {
            Serializable entityId = getEntityId(entity);
            org.springframework.util.Assert.notNull(entity, "Id could not be null");
            redisoper.updateInRedis(() -> baseMapper.selectById(entityId), entity);
        }
        return retBool;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatchById(Collection<T> entityList) {
        for (T t : entityList) {
            updateById(t);
        }
        return true;
    }

    @Override
    public T getById(Serializable id) {
        return redisoper.getByKey(() -> baseMapper.selectById(id), id);
    }

    @Override
    public T getOne(Wrapper<T> queryWrapper, boolean throwEx) {
        if (throwEx) {
            return baseMapper.selectOne(queryWrapper);
        }
        return SqlHelper.getObject(logger, baseMapper.selectList(queryWrapper));
    }

    @Override
    public Map<String, Object> getMap(Wrapper<T> queryWrapper) {
        return SqlHelper.getObject(logger, baseMapper.selectMaps(queryWrapper));
    }

    @Override
    public int count(Wrapper<T> queryWrapper) {
        return SqlHelper.retCount(baseMapper.selectCount(queryWrapper));
    }

    @Override
    public List<T> list(Wrapper<T> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper) {
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper) {
        return baseMapper.selectMaps(queryWrapper);
    }

    @Override
    public <V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return baseMapper.selectObjs(queryWrapper).stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
    }

    @Override
    public IPage<Map<String, Object>> pageMaps(IPage<T> page, Wrapper<T> queryWrapper) {
        return baseMapper.selectMapsPage(page, queryWrapper);
    }

    @Override
    public <V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return SqlHelper.getObject(logger, listObjs(queryWrapper, mapper));
    }

    @Override
    public void setRedisoper(GenericRedisoper<T> redisoper) {
        this.redisoper = redisoper;
        this.entityClass = redisoper.getEntityMetadata().getEntityClass();
    }
}
