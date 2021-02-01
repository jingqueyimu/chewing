package com.jingqueyimu;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.model.User;
import com.jingqueyimu.service.UserService;

/**
 * 业务测试
 *
 * @author zhuangyilian
 */
public class BizTest extends BaseTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    public void test() {
        JSONObject params = new JSONObject();
        params.put("username_like", "test");
        List<User> user = userService.list(params);
        System.out.println(user);
    }
    
    @Test
    public void test2() {
        JSONObject params = new JSONObject();
        params.put("username_in", Arrays.asList("test"));
        List<User> user = userService.list(params);
        System.out.println(user);
    }
}
