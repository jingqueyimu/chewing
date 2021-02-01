package com.jingqueyimu.constant;

/**
 * 系统通用常量类
 *
 * @author zhuangyilian
 */
public class SysConstant {

    /**
     * 字母表
     */
    public static final String[] LETTER_LIST = {
            "A", "B", "C", "D", "E", "F", "G", 
            "H", "I", "J", "K", "L", "M", "N", 
            "O", "P", "Q", "R", "S", "T", 
            "U", "V", "W", "X", "Y", "Z"};
    
    /**
     * 邮箱验证码有效期(10分钟)
     */
    public static final int EMAIL_CODE_VALID_PERIOD = 60 * 10;
    
    /**
     * 发送邮箱验证码间隔时间(1分钟)
     */
    public static final int SEND_EMAIL_CODE_INTERVAL = 60;
    
    /**
     * 短信验证码有效期(10分钟)
     */
    public static final int SMS_CODE_VALID_PERIOD = 60 * 10;
    
    /**
     * 发送短信验证码间隔时间(1分钟)
     */
    public static final int SEND_SMS_CODE_INTERVAL = 60;
    
    /**
     * 图形验证码有效期(30分钟)
     */
    public static final int CAPTCHA_CODE_VALID_PERIOD = 60 * 30;
}
