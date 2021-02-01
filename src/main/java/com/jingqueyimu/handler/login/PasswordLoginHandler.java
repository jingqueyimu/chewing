package com.jingqueyimu.handler.login;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.model.User;
import com.jingqueyimu.model.dict.LoginType;
import com.jingqueyimu.util.SysUtil;

/**
 * 密码登录处理器
 *
 * @author zhuangyilian
 */
@Component
public class PasswordLoginHandler extends LoginHandler {
    
    @Override
    protected int getType() {
        return LoginType.PASSWORD.getCode();
    }

    @Override
    public User getLoginUser(JSONObject params) {
        SysUtil.checkParam(params, "account", "请输入账号");
        SysUtil.checkParam(params, "password", "请输入密码");
        String account = params.getString("account");
        String password = params.getString("password");
        User user = userService.getByAccountAndPassword(account, password);
        if (user == null) {
            throw new AppException(StatusCode.ERR_AUTH_ACCT_PWD, "账号密码错误");
        }
        return user;
    }
}
