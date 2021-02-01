package com.jingqueyimu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jingqueyimu.service.ProductService;

/**
 * 产品控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/product")
public class ProductController extends BaseController {
    
    @Autowired
    private ProductService productService;
}
