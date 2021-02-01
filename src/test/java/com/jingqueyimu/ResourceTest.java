package com.jingqueyimu;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.jingqueyimu.util.ResourceUtil;

/**
 * 资源测试
 *
 * @author zhuangyilian
 */
public class ResourceTest extends BaseTest {

    @Autowired
    private ResourceUtil resourceUtil;
    
    @Test
    public void test() {
        try {
            Map<String, String> map = resourceUtil.loadMapConfigs("classpath:config/test.properties");
            System.out.println("--------loadMapConfigs--------:" + map);
            JSONArray jsonArr = resourceUtil.loadJsonArrConfigs("classpath:config/test.json");
            System.out.println("--------loadJsonArrConfigs--------:" + jsonArr);
            List<String> sqls = resourceUtil.loadSqlConfig("classpath:config/test.sql");
            System.out.println("--------loadSqlConfig--------:" + sqls);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
