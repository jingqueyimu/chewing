package com.jingqueyimu.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 自定义配置
 * 将配置文件属性值,映射到组件中
 *
 * @author zhuangyilian
 */
// 只有容器中的组件,才能使用@ConfigurationProperties提供的功能
@Component
// 属性前缀
@ConfigurationProperties(prefix="test")
// 指定配置文件(默认读取主配置文件),会从src/test/resources寻找
@PropertySource(value="classpath:config/test.properties", ignoreResourceNotFound=true, encoding="UTF-8")
public class TestConfig {
    
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}