package com.jingqueyimu.service.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingqueyimu.config.MyConfig;
import com.jingqueyimu.constant.CacheConstant;
import com.jingqueyimu.constant.RunEnv;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.constant.SysConstant;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.util.BizUtil;

/**
 * 短信验证码服务
 *
 * @author zhuangyilian
 */
@Service
public class SmsService {
    
    @Autowired
    private RedisService redisService;
    @Autowired
    private MyConfig config;
    
    /**
     * 发送短信验证码
     *
     * @param mobile
     * @return
     */
    public void sendSmsCode(String mobile) {
        // 是否已过发送间隔时间
        Object smsCodeObj = redisService.get(CacheConstant.LAST_SMS_CODE_TIME + mobile);
        if (smsCodeObj != null) {
            throw new AppException(StatusCode.OK_REPEAT, "短信验证码已发送");
        }
        // 短信验证码
        String smsCode = BizUtil.createVerificationCode();
        // 是否开发环境
        if (RunEnv.DEV.equals(config.getRunEnv())) {
            smsCode = "123456";
        }
        String content = BizUtil.buildSmsCodeContent(config.getSiteName(), smsCode);
        // TODO 发送短信
        
        // 缓存短信验证码
        redisService.set(CacheConstant.SMS_CODE + mobile, smsCode, SysConstant.SMS_CODE_VALID_PERIOD);
        redisService.set(CacheConstant.LAST_SMS_CODE_TIME + mobile, System.currentTimeMillis(), SysConstant.SEND_SMS_CODE_INTERVAL);
    }
    
    /**
     * 校验短信验证码
     *
     * @param mobile
     * @param smsCode
     */
    public void checkSmsCode(String mobile, String smsCode) {
        Object smsCodeObj = redisService.get(CacheConstant.SMS_CODE + mobile);
        if (smsCodeObj == null) {
            throw new AppException(StatusCode.ERR_BIZ, "短信验证码失效，请重新获取");
        }
        if (!smsCodeObj.toString().equals(smsCode)) {
            throw new AppException(StatusCode.ERR_BIZ, "短信验证码错误");
        }
        redisService.delete(CacheConstant.SMS_CODE + mobile);
        redisService.delete(CacheConstant.LAST_SMS_CODE_TIME + mobile);
    }
}
