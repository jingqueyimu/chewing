package com.jingqueyimu.model.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录方式
 *
 * @author zhuangyilian
 */
public enum LoginType {

    PASSWORD(1, "密码"),
    
    SMS(2, "短信"),
    
    EMAIL(3, "邮箱"),
    
    OAUTH(4, "授权");
    
    private int code;
    
    private String value;
    
    LoginType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    /**
     * 获取枚举
     *
     * @param code
     * @return
     */
    public static LoginType getEnum(int code) {
        LoginType[] enums = LoginType.values();
        for (LoginType e : enums) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }
    
    /**
     * 获取枚举信息
     *
     * @return
     */
    public static Map<Integer, String> getEnumInfo() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        LoginType[] enums = LoginType.values();
        for (LoginType e : enums) {
            map.put(e.getCode(), e.getValue());
        }
        return map;
    }
}
