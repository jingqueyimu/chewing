package com.jingqueyimu.exception;

import com.jingqueyimu.constant.StatusCode;

/**
 * 应用异常类
 *
 * @author zhuangyilian
 */
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private int code = StatusCode.ERR_SYS;
    
    public AppException() {}
    
    public AppException(String msg) {
        super(msg);
    }
    
    public AppException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
