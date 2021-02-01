package com.jingqueyimu.handler.register;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.model.User;
import com.jingqueyimu.service.UserService;
import com.jingqueyimu.service.component.CaptchaService;
import com.jingqueyimu.service.component.MailService;
import com.jingqueyimu.service.component.RedisService;
import com.jingqueyimu.service.component.SmsService;

/**
 * 注册处理器抽象类
 *
 * @author zhuangyilian
 */
public abstract class RegisterHandler {
    
    private static final Map<Integer, RegisterHandler> HANDLERS = new HashMap<>();
    
    @Autowired
    protected RedisService redisService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected MailService mailService;
    @Autowired
    protected SmsService smsService;
    @Autowired
    protected CaptchaService captchaService;
    
    protected abstract int getType();
    
    protected abstract void checkParams(JSONObject params);
    
    @PostConstruct
    protected void init() {
        HANDLERS.put(getType(), this);
    }

    public User handle(JSONObject params) {
        // 校验参数
        checkParams(params);
        String username = params.getString("username");
        String mobile = params.getString("mobile");
        String email = params.getString("email");
        String password = params.getString("password");
        int registerType = params.getInteger("registerType");
        // 创建注册用户
        return userService.createRegisterUser(username, password, mobile, email, registerType);
    }
    
    public static RegisterHandler getHandler(int type) {
        return HANDLERS.get(type);
    }
}
