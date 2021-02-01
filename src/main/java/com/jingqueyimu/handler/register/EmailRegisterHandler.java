package com.jingqueyimu.handler.register;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.model.User;
import com.jingqueyimu.model.dict.RegisterType;
import com.jingqueyimu.util.SysUtil;

/**
 * 邮箱注册处理器
 *
 * @author zhuangyilian
 */
@Component
public class EmailRegisterHandler extends RegisterHandler {
    
    @Override
    protected int getType() {
        return RegisterType.EMAIL.getCode();
    }

    @Override
    protected void checkParams(JSONObject params) {
        SysUtil.checkParam(params, "username", "请输入用户名");
        SysUtil.checkParam(params, "email", "请输入邮箱");
        SysUtil.checkParam(params, "emailCode", "请输入邮箱验证码");
        SysUtil.checkParam(params, "password", "请输入密码");
        SysUtil.checkParam(params, "rePassword", "请输入确认密码");
        
        String username = params.getString("username");
        String email = params.getString("email");
        String emailCode = params.getString("emailCode");
        String password = params.getString("password");
        String rePassword = params.getString("rePassword");
        
        if (!SysUtil.checkUsername(username)) {
            throw new AppException(StatusCode.ERR_PARAM, "用户名必须为1-16个字符");
        }
        if (email.length() > 32 || !SysUtil.checkEmail(email)) {
            throw new AppException(StatusCode.ERR_PARAM, "邮箱格式不正确");
        }
        if (!password.equals(rePassword)) {
            throw new AppException(StatusCode.ERR_PARAM, "两次输入密码不一致");
        }
        if (!SysUtil.checkWeakPassword(password)) {
            throw new AppException(StatusCode.ERR_PARAM, "密码长度必须为6-16位");
        }
        User user = userService.getByUsername(username);
        if (user != null) {
            throw new AppException(StatusCode.ERR_BIZ, "用户名已存在");
        }
        user = userService.getByEmail(email);
        if (user != null) {
            throw new AppException(StatusCode.ERR_BIZ, "邮箱已存在");
        }
        // 校验邮箱验证码
        mailService.checkEmailCode(email, emailCode);
    }
}
