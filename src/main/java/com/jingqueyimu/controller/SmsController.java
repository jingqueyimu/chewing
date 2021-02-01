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
import com.jingqueyimu.service.component.SmsService;
import com.jingqueyimu.util.SysUtil;

/**
 * 短信控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/sms")
public class SmsController extends BaseController {
    
    @Autowired
    private SmsService smsService;
    
    /**
     * 发送短信验证码
     *
     * @param params
     * @return
     */
    @PostMapping(value="/send_sms_code")
    public ResultData sendSmsCode(@RequestBody JSONObject params) {
        SysUtil.checkParam(params, "mobile", "请输入手机号");
        String mobile = params.getString("mobile");
        if (!SysUtil.checkMobile(mobile)) {
            throw new AppException(StatusCode.ERR_PARAM, "手机号格式不正确");
        }
        smsService.sendSmsCode(mobile);
        return ResultData.succ();
    }
}
