package com.jingqueyimu.service.component;

import java.awt.Image;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingqueyimu.constant.CacheConstant;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.constant.SysConstant;
import com.jingqueyimu.exception.AppException;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;

/**
 * 图形验证码服务
 *
 * @author zhuangyilian
 */
@Service
public class CaptchaService {
    
    @Autowired
    private RedisService redisService;
    
    /**
     * 生成图形验证码
     *
     * @param token
     * @return
     */
    public Image createCaptcha(String token) {
        // 字母数字验证码
        String code = RandomStringUtils.randomAlphanumeric(4);
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(120, 50, 4, 100);
        Image image = captcha.createImage(code);
        // 缓存验证码
        redisService.set(CacheConstant.CAPTCHA_CODE + token, code, SysConstant.CAPTCHA_CODE_VALID_PERIOD);
        return image;
    }
    
    /**
     * 校验图形验证码
     *
     * @param token
     * @param captchaCode
     */
    public void checkCaptchaCode(String token, String captchaCode) {
        Object captchaCodeObj = redisService.get(CacheConstant.CAPTCHA_CODE + token);
        if (captchaCodeObj == null) {
            throw new AppException(StatusCode.ERR_BIZ, "图形验证码失效，请点击刷新");
        }
        if (!captchaCodeObj.toString().equalsIgnoreCase(captchaCode)) {
            throw new AppException(StatusCode.ERR_BIZ, "图形验证码错误");
        }
        redisService.delete(CacheConstant.CAPTCHA_CODE + token);
    }
}
