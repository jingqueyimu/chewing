package com.jingqueyimu.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.jingqueyimu.mapper.sql.DynamicSql;

/**
 * 原生SQL执行接口(注解方式)
 *
 * @author zhuangyilian
 */
public interface SqlMapper2 {

    @InsertProvider(type = DynamicSql.class, method = "sql")
    int insert(String sql);
    
    @DeleteProvider(type = DynamicSql.class, method = "sql")
    int delete(String sql);
    
    @UpdateProvider(type = DynamicSql.class, method = "sql")
    int update(String sql);
    
    @SelectProvider(type = DynamicSql.class, method = "sql")
    List<Map<String, Object>> select(String sql);
    
    @SelectProvider(type = DynamicSql.class, method = "sql")
    int count(String sql);
}
