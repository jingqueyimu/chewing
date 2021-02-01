package com.jingqueyimu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.jingqueyimu.config.MyConfig;
import com.jingqueyimu.context.AdminContext;
import com.jingqueyimu.context.UserContext;

/**
 * 抽象基础控制器
 *
 * @author zhuangyilian
 */
public abstract class BaseController {
    
    public final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Lazy
    @Autowired
    public MyConfig config;
    @Lazy
    @Autowired
    public UserContext userContext;
    @Lazy
    @Autowired
    public AdminContext adminContext;
}
