package com.jingqueyimu.config;

import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.MapWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.autoconfigure.ConfigurationCustomizer;

/**
 * MyBatis配置
 *
 * @author zhuangyilian
 */
@Configuration
public class MyBatisConfig {

    /**
     * 配置MyBatis的分页插件PageHelper
     *
     * @return
     */
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("dialect", "mysql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
    
    /**
     * 自定义配置
     *
     * @return
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {

            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                // 配置返回Map时,下划线风格转驼峰风格
                configuration.setObjectWrapperFactory(new MapWrapperFactory());
            }
        };
    }
    
    /**
     * Map包装工厂
     *
     * @author zhuangyilian
     */
    static class MapWrapperFactory implements ObjectWrapperFactory {
        
        @Override
        public boolean hasWrapperFor(Object object) {
            return object != null && object instanceof Map;
        }
 
        @SuppressWarnings("unchecked")
        @Override
        public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
            return new MyMapWrapper(metaObject, (Map<String, Object>) object);
        }
    }
 
    /**
     * Map包装类
     *
     * @author zhuangyilian
     */
    static class MyMapWrapper extends MapWrapper {
        
        MyMapWrapper(MetaObject metaObject, Map<String, Object> map) {
            super(metaObject, map);
        }
 
        @Override
        public String findProperty(String name, boolean useCamelCaseMapping) {
            if (useCamelCaseMapping && ((name.charAt(0) >= 'A' && name.charAt(0) <= 'Z') || name.contains("_"))) {
                return underlineToCamelhump(name);
            }
            return name;
        }
 
        /**
         * 下划线风格转驼峰风格
         *
         * @param inputString
         * @return
         */
        private String underlineToCamelhump(String inputString) {
            StringBuilder sb = new StringBuilder();
            boolean nextUpperCase = false;
            for (int i = 0; i < inputString.length(); i++) {
                char c = inputString.charAt(i);
                if (c == '_') {
                    if (sb.length() > 0) {
                        nextUpperCase = true;
                    }
                } else {
                    if (nextUpperCase) {
                        sb.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                    } else {
                        sb.append(Character.toLowerCase(c));
                    }
                }
            }
            return sb.toString();
        }
    }
}
