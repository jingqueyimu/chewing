package com.jingqueyimu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.component.MailService;
import com.jingqueyimu.util.SysUtil;

/**
 * 邮箱控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/email")
public class EmailController extends BaseController {
    
    @Autowired
    private MailService mailService;
    
    /**
     * 发送邮箱验证码
     *
     * @param params
     * @return
     */
    @PostMapping(value="/send_email_code")
    public ResultData sendEmailCode(@RequestBody JSONObject params) {
        SysUtil.checkParam(params, "email", "请输入邮箱");
        String email = params.getString("email");
        if (email.length() > 32 || !SysUtil.checkEmail(email)) {
            throw new AppException(StatusCode.ERR_PARAM, "邮箱格式不正确");
        }
        // 发送邮箱验证码
        mailService.sendEmailCode(email);
        return ResultData.succ();
    }
}
