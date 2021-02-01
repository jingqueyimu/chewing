package com.jingqueyimu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.model.User;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.UserService;

/**
 * 用户控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 注册
     *
     * @param params
     * @return
     */
    @PostMapping(value="/register")
    public ResultData register(@RequestBody JSONObject params) {
        User user = userService.register(params);
        if (user == null) {
            throw new AppException(StatusCode.ERR_BIZ, "注册失败");
        }
        // 保存当前登录用户
        userContext.saveCurrUser(user, false);
        return ResultData.succ();
    }
    
    /**
     * 登录
     *
     * @param params
     * @return
     */
    @PostMapping(value="/login")
    public ResultData login(@RequestBody JSONObject params) {
        // 记住账号密码
        boolean rememberMe = params.getBooleanValue("rememberMe");
        User user = userService.login(params);
        if (user == null) {
            throw new AppException(StatusCode.ERR_AUTH, "登录失败");
        }
        // 保存当前登录用户
        userContext.saveCurrUser(user, rememberMe);
        return ResultData.succ();
    }
    
    /**
     * 登出
     *
     * @return
     */
    @RequestMapping("/logout")
    public ResultData logout() {
        userContext.clearCurrUser();
        return ResultData.succ();
    }
}
