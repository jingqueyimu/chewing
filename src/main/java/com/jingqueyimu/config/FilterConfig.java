package com.jingqueyimu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jingqueyimu.filter.AuthFilter;
import com.jingqueyimu.filter.LogFilter;

/**
 * 过滤器配置
 *
 * @author zhuangyilian
 */
// 相当于xml的beans
@Configuration
public class FilterConfig {
    
    @Autowired
    private LogFilter logFilter;
    @Autowired
    private AuthFilter authFilter;

    /**
     * 注册日志过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<LogFilter> registerLogFilter() {
        FilterRegistrationBean<LogFilter> registration = new FilterRegistrationBean<LogFilter>();
        // 设置过滤器
        registration.setFilter(logFilter);
        // 过滤路径
        registration.addUrlPatterns("/*");
        // 过滤器名
        registration.setName("logFilter");
        // 过滤器执行顺序
        registration.setOrder(1);    
        return registration;
    }
    
    /**
     * 注册权限过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<AuthFilter> registerAuthFilter() {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<AuthFilter>();
        // 设置过滤器
        registration.setFilter(authFilter);
        // 过滤路径
        registration.addUrlPatterns("/*");
        // 过滤器名
        registration.setName("authFilter");
        // 过滤器执行顺序
        registration.setOrder(2);    
        return registration;
    }
}
