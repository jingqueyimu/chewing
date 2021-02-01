package com.jingqueyimu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * 项目启动类
 * 使用外部tomcat时,必须继承SpringBootServletInitializer,并实现configure
 *
 * @author zhuangyilian
 */
@SpringBootApplication
// 使用通用mapper时,要导入tk.mybatis.spring.annotation.MapperScan
@MapperScan("com.jingqueyimu.mapper")
// 开启缓存
@EnableCaching
// 开启定时任务
@EnableScheduling
// 开启异步调用
@EnableAsync
public class MyApplication extends SpringBootServletInitializer {
    
    public static final Logger log = LoggerFactory.getLogger(MyApplication.class);

    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        SpringApplication.run(MyApplication.class, args);
        long end = System.currentTimeMillis();
        log.info("The service has been fully started, taking {} seconds.", (end - begin) / 1000D);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MyApplication.class);
    }
}
