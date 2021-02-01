package com.jingqueyimu;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jingqueyimu.config.TestConfig;
import com.jingqueyimu.config.TestYmlConfig;

/**
 * 配置测试
 *
 * @author zhuangyilian
 */
public class ConfigTest extends BaseTest {
    
    @Autowired
    private TestConfig testConfig;
    @Autowired
    private TestYmlConfig testYamlConfig;
    
    @Test
    public void test() {
        System.out.println("test config: " + testConfig.getValue());
    }
    
    @Test
    public void test2() {
        System.out.println("test yaml config: " + testYamlConfig.toString());
    }
}
