package com.jingqueyimu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingqueyimu.mapper.ProductMapper;
import com.jingqueyimu.model.Product;

/**
 * 产品服务
 *
 * @author zhuangyilian
 */
@Service
public class ProductService extends BaseService<Product> {
    
    @Autowired
    private ProductMapper productMapper;
}
