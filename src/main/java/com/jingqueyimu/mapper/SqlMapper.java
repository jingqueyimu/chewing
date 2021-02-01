package com.jingqueyimu.mapper;

import java.util.List;
import java.util.Map;

/**
 * 原生SQL执行接口(配置文件方式)
 *
 * @author zhuangyilian
 */
public interface SqlMapper {

    int insert(String sql);
    
    int delete(String sql);
    
    int update(String sql);
    
    List<Map<String, Object>> select(String sql);
    
    int count(String sql);
}
