package com.jingqueyimu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jingqueyimu.service.CategoryService;

/**
 * 类目控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController {
    
    @Autowired
    private CategoryService categoryService;
}
