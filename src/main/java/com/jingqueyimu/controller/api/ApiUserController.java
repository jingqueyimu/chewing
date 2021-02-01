package com.jingqueyimu.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.controller.BaseController;
import com.jingqueyimu.model.User;
import com.jingqueyimu.model.bean.CurrUser;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.UserService;

/**
 * 用户控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/api/user")
public class ApiUserController extends BaseController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 获取当前登录用户
     *
     * @param params
     * @return
     */
    @PostMapping(value="/get_curr_user")
    public ResultData getCurrUser(@RequestBody JSONObject params) {
        CurrUser currUser = userContext.getCurrUser();
        return ResultData.succ(currUser);
    }
    
    /**
     * 更新用户信息
     *
     * @param params
     * @return
     */
    @PostMapping(value="/update_user_info")
    public ResultData updateUserInfo(@RequestBody JSONObject params) {
        CurrUser currUser = userContext.getCurrUser();
        User user = userService.updateUserInfo(currUser.getId(), params);
        currUser = userContext.updateCurrUser(user);
        return ResultData.succ(currUser);
    }
    
    /**
     * 更新用户密码
     *
     * @param params
     * @return
     */
    @PostMapping(value="/update_user_password")
    public ResultData updateUserPassword(@RequestBody JSONObject params) {
        CurrUser currUser = userContext.getCurrUser();
        userService.updateUserPassword(currUser.getId(), params);
        return ResultData.succ();
    }
    
    /**
     * 更新用户邮箱
     *
     * @param params
     * @return
     */
    @PostMapping(value="/update_user_email")
    public ResultData updateUserEmail(@RequestBody JSONObject params) {
        CurrUser currUser = userContext.getCurrUser();
        User user = userService.updateUserEmail(currUser.getId(), params);
        currUser = userContext.updateCurrUser(user);
        return ResultData.succ(currUser);
    }
    
    /**
     * 更新用户手机号
     *
     * @param params
     * @return
     */
    @PostMapping(value="/update_user_mobile")
    public ResultData updateUserMobile(@RequestBody JSONObject params) {
        CurrUser currUser = userContext.getCurrUser();
        User user = userService.updateUserMobile(currUser.getId(), params);
        currUser = userContext.updateCurrUser(user);
        return ResultData.succ(currUser);
    }
}
