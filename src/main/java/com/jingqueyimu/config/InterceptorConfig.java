package com.jingqueyimu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jingqueyimu.interceptor.GlobalInterceptor;

/**
 * 拦截器配置
 * 最好实现WebMvcConfigurer,而不要继承WebMvcConfigurationSupport
 * 继承WebMvcConfigurationSupport,会导致springboot自动配置失效(会导致访问不到默认的静态资源路径)
 *
 * @author zhuangyilian
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    
    @Autowired
    private GlobalInterceptor globalInterceptor;

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 可以添加多个拦截器
        InterceptorRegistration ig = registry.addInterceptor(globalInterceptor);
        // 排除拦截路径
//        ig.excludePathPatterns("/file/**");
        // 添加拦截路径
        ig.addPathPatterns("/**");
    }
    
    /**
     * 添加静态资源路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/teststatic/**").addResourceLocations("classpath:/teststatic/");
    }
}