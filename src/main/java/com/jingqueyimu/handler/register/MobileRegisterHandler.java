package com.jingqueyimu.handler.register;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.model.User;
import com.jingqueyimu.model.dict.RegisterType;
import com.jingqueyimu.util.SysUtil;

/**
 * 手机注册处理器
 *
 * @author zhuangyilian
 */
@Component
public class MobileRegisterHandler extends RegisterHandler {
    
    @Override
    protected int getType() {
        return RegisterType.MOBILE.getCode();
    }

    @Override
    protected void checkParams(JSONObject params) {
        SysUtil.checkParam(params, "username", "请输入用户名");
        SysUtil.checkParam(params, "mobile", "请输入手机号");
        SysUtil.checkParam(params, "smsCode", "请输入短信验证码");
        SysUtil.checkParam(params, "password", "请输入密码");
        SysUtil.checkParam(params, "rePassword", "请输入确认密码");
        
        String username = params.getString("username");
        String mobile = params.getString("mobile");
        String smsCode = params.getString("smsCode");
        String password = params.getString("password");
        String rePassword = params.getString("rePassword");
        
        if (!SysUtil.checkUsername(username)) {
            throw new AppException(StatusCode.ERR_PARAM, "用户名必须为1-16个字符");
        }
        if (!SysUtil.checkMobile(mobile)) {
            throw new AppException(StatusCode.ERR_PARAM, "手机号格式不正确");
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
        user = userService.getByMobile(mobile);
        if (user != null) {
            throw new AppException(StatusCode.ERR_BIZ, "手机号已存在");
        }
        // 校验短信验证码
        smsService.checkSmsCode(mobile, smsCode);
    }
}
