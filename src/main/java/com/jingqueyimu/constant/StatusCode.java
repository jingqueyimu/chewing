package com.jingqueyimu.constant;

/**
 * 状态码
 * 大于0: 成功
 * 等于0: 失败-未知错误
 * 小于0: 失败
 *
 * @author zhuangyilian
 */
public class StatusCode {
    
    /*********************************** 成功状态码 ***********************************/

    /**
     * 成功
     */
    public static final int OK = 1;
    /**
     * 成功-重复执行
     */
    public static final int OK_REPEAT = 2;
    
    /*********************************** 失败状态码 ***********************************/

    /**
     * 未知错误
     */
    public static final int ERR_UNKNOWN = 0;
    
    /**
     * 系统错误
     */
    public static final int ERR_SYS = -1000;
    /**
     * 系统错误-锁
     */
    public static final int ERR_SYS_LOCK = -1001;
    
    /**
     * 权限错误
     */
    public static final int ERR_AUTH = -2000;
    /**
     * 权限错误-未登录
     */
    public static final int ERR_AUTH_NO_LOGIN = -2001;
    /**
     * 权限错误-账号密码错误
     */
    public static final int ERR_AUTH_ACCT_PWD = -2002;
    
    /**
     * 参数错误
     */
    public static final int ERR_PARAM = -3000;
    
    /**
     * 参数错误-参数为空
     */
    public static final int ERR_PARAM_EMPTY = -3001;
    
    /**
     * 业务错误
     */
    public static final int ERR_BIZ = -4000;
    /**
     * 业务错误-重复执行
     */
    public static final int ERR_BIZ_REPEAT = -4001;
    
    /**
     * 第三方错误
     */
    public static final int ERR_THIRD = -5000;
}
