package com.jingqueyimu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingqueyimu.mapper.CategoryMapper;
import com.jingqueyimu.model.Category;

/**
 * 类目服务
 *
 * @author zhuangyilian
 */
@Service
public class CategoryService extends BaseService<Category> {
    
    @Autowired
    private CategoryMapper categoryMapper;
}
