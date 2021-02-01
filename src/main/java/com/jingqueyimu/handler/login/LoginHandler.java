package com.jingqueyimu.handler.login;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.model.User;
import com.jingqueyimu.service.UserService;
import com.jingqueyimu.service.component.RedisService;

/**
 * 登录处理器抽象类
 *
 * @author zhuangyilian
 */
public abstract class LoginHandler {
    
    private static final Map<Integer, LoginHandler> HANDLERS = new HashMap<>();
    
    @Autowired
    protected UserService userService;
    @Autowired
    protected RedisService redisService;
    
    protected abstract int getType();
    
    protected abstract User getLoginUser(JSONObject params);
    
    @PostConstruct
    protected void init() {
        HANDLERS.put(getType(), this);
    }

    public User handle(JSONObject params) {
        User user = getLoginUser(params);
        if (user == null) {
            throw new AppException(StatusCode.ERR_AUTH, "登录失败");
        }
        user.setLastLoginTime(new Date());
        return userService.update(user);
    }
    
    public static LoginHandler getHandler(int type) {
        return HANDLERS.get(type);
    }
}
