package com.jingqueyimu.service;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

/**
 * 抽象基础服务
 *
 * @author zhuangyilian
 */
public abstract class BaseService<T> {
    
    public final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    public Mapper<T> mapper;
    
    private Class<T> entityClass;
    
    private T entity;
    
    public BaseService() {
        try {
            this.entityClass = getEntityClass();
            this.entity = this.entityClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 保存实体
     *
     * @param t
     * @return
     */
    public T save(T t) {
        try {
            Method getId = entityClass.getDeclaredMethod("getId");
            Object idObj = getId.invoke(t);
            if (idObj != null) {
                throw new RuntimeException("保存实体时，ID必须为空！");
            }
            // 设置创建时间
            Method setGmtCreate = entityClass.getDeclaredMethod("setGmtCreate", Date.class);
            setGmtCreate.invoke(t, new Date());
            mapper.insert(t);
            return t;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 保存实体,并自定义ID
     *
     * @param t
     * @return
     */
    public T saveWithId(T t) {
        try {
            Method getId = entityClass.getDeclaredMethod("getId");
            Object idObj = getId.invoke(t);
            if (idObj != null) {
                T t2 = mapper.selectByPrimaryKey(idObj);
                if (t2 != null) {
                    throw new RuntimeException("ID已存在！");
                }
            }
            // 设置创建时间
            Method setGmtCreate = entityClass.getDeclaredMethod("setGmtCreate", Date.class);
            setGmtCreate.invoke(t, new Date());
            mapper.insert(t);
            return t;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 更新实体
     *
     * @param t
     * @return
     */
    public T update(T t) {
        try {
            Method getId = entityClass.getDeclaredMethod("getId");
            Object idObj = getId.invoke(t);
            if (idObj == null) {
                throw new RuntimeException("更新实体时，ID不能为空！");
            }
            // 设置修改时间
            Method setGmtModify = entityClass.getDeclaredMethod("setGmtModify", Date.class);
            setGmtModify.invoke(t, new Date());
            mapper.updateByPrimaryKey(t);
            return t;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 更新实体(只更新不为null的值)
     *
     * @param t
     * @return
     */
    public T updateSelective(T t) {
        try {
            Method getId = entityClass.getDeclaredMethod("getId");
            Object idObj = getId.invoke(t);
            if (idObj == null) {
                throw new RuntimeException("更新实体时，ID不能为空！");
            }
            // 设置修改时间
            Method setGmtModify = entityClass.getDeclaredMethod("setGmtModify", Date.class);
            setGmtModify.invoke(t, new Date());
            mapper.updateByPrimaryKeySelective(t);
            return t;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 根据ID删除一个实体
     *
     * @param id
     * @return
     */
    public int deleteById(long id) {
        return mapper.deleteByPrimaryKey(id);
    }
    
    /**
     * 删除实体
     *
     * @param t
     * @return
     */
    public T delete(T t) {
        mapper.delete(t);
        return t;
    }
    
    /**
     * 根据ID查询一个实体
     *
     * @param id
     * @return
     */
    public T getById(long id) {
        return mapper.selectByPrimaryKey(id);
    }
    
    /**
     * 查询一个实体
     *
     * @param params
     * @return
     */
    public T get(JSONObject params) {
        Example example = buildExample(params);
        return mapper.selectOneByExample(example);
    }
    
    /**
     * 查询所有实体
     *
     * @return
     */
    public List<T> listAll() {
        return mapper.selectAll();
    }
    
    /**
     * 查询多个实体
     *
     * @param params
     * @return
     */
    public List<T> list(JSONObject params) {
        return list(params, null, true);
    }
    
    /**
     * 查询多个实体,并指定默认排序
     *
     * @param params
     * @param orderField
     * @param isAsc
     * @return
     */
    public List<T> list(JSONObject params, String orderField, boolean isAsc) {
        Example example = buildExample(params, orderField, isAsc);
        return mapper.selectByExample(example);
    }
    
    /**
     * 统计所有实体数量
     *
     * @return
     */
    public int countAll() {
        return mapper.selectCount(entity);
    }
    
    /**
     * 统计实体数量
     *
     * @param params
     * @return
     */
    public int count(JSONObject params) {
        Example example = buildExample(params);
        return mapper.selectCountByExample(example);
    }
    
    /**
     * 分页查询实体
     *
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    public PageInfo<T> page(int pageNum, int pageSize, JSONObject params) {
        return page(pageNum, pageSize, params, null, true);
    }
    
    /**
     * 分页查询实体,并指定默认排序
     *
     * @author zhuangyilian
     * @param pageNum
     * @param pageSize
     * @param params
     * @param orderField
     * @param isAsc
     * @return
     */
    public PageInfo<T> page(int pageNum, int pageSize, JSONObject params, String orderField, boolean isAsc) {
        if (pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        Example example = buildExample(params, orderField, isAsc);
        List<T> list = mapper.selectByExample(example);
        PageInfo<T> page = new PageInfo<T>(list);
        return page;
    }
    
    /**
     * 组装查询条件
     *
     * @param params
     * @return
     */
    private Example buildExample(JSONObject params) {
        return buildExample(params, null, true);
    }
    
    /**
     * 组装查询条件
     * 属性名后缀匹配查询条件
     * xxx_begin 大于等于
     * xxx_end 小于等于
     * xxx_in IN查询
     * xxx_like 模糊查询
     * xxx_llike 左模糊查询
     * xxx_rlike 右模糊查询
     * _orderby 排序子句
     * 其他 等于
     * 属性值为空时,不参与查询
     *
     * @param params
     * @param orderField
     * @param isAsc
     * @return
     */
    private Example buildExample(JSONObject params, String orderField, boolean isAsc) {
        if (params == null) {
            params = new JSONObject();
        }
        Example example = new Example(entityClass);
        Example.Criteria criteria = example.createCriteria();
        // 排除属性
        List<String> excludeAttrs = Arrays.asList("_orderby");
        for (String key : params.keySet()) {
            if (excludeAttrs.contains(key)) {
                continue;
            }
            if (StringUtils.isBlank(params.getString(key))) {
                continue;
            }
            if (key.endsWith("_begin")) {
                criteria.andGreaterThanOrEqualTo(key.substring(0, key.lastIndexOf("_")), params.get(key));
            } else if (key.endsWith("_end")) {
                criteria.andLessThanOrEqualTo(key.substring(0, key.lastIndexOf("_")), params.get(key));
            } else if (key.endsWith("_in")) {
                criteria.andIn(key.substring(0, key.lastIndexOf("_")), params.getJSONArray(key));
            } else if (key.endsWith("_like")) {
                criteria.andLike(key.substring(0, key.lastIndexOf("_")), "%" + params.getString(key) + "%");
            } else if (key.endsWith("_llike")) {
                criteria.andLike(key.substring(0, key.lastIndexOf("_")), "%" + params.getString(key));
            } else if (key.endsWith("_rlike")) {
                criteria.andLike(key.substring(0, key.lastIndexOf("_")), params.getString(key) + "%");
            } else {
                criteria.andEqualTo(key, params.get(key));
            }
        }
        // 排序
        String orderByClause = params.getString("_orderby");
        if (StringUtils.isNotBlank(orderByClause)) {
            example.setOrderByClause(formatOrderByClause(orderByClause));
        } else if (StringUtils.isNotBlank(orderField)) {
            if (isAsc) {
                example.orderBy(orderField).asc();
            } else {
                example.orderBy(orderField).desc();
            }
        }
        return example;
    }
    
    /**
     * 格式化排序子句
     * 对象属性转表字段(驼峰转下划线)
     *
     * @param orderByClause
     * @return
     */
    private static String formatOrderByClause(String orderByClause) {
        // 先将ASC/DESC转为小写,避免被格式化为下划线
        orderByClause = orderByClause.replaceAll("(?i)asc", "asc").replaceAll("(?i)desc", "desc");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < orderByClause.length(); i++) {
            char c = orderByClause.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                sb.append('_').append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString().replaceAll("(?i)asc", "ASC").replaceAll("(?i)desc", "DESC");
    }
    
    /**
     * 获取实体class
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    private Class<T> getEntityClass() {
        // 获得当前类带有泛型类型的父类
        ParameterizedType superClass = (ParameterizedType) this.getClass().getGenericSuperclass();
        // 获得运行期的泛型类型
        Class<T> clazz = (Class<T>) superClass.getActualTypeArguments()[0];
        return clazz;
    }
}
