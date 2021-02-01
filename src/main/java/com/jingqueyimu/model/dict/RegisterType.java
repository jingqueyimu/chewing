package com.jingqueyimu.model.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册方式
 *
 * @author zhuangyilian
 */
public enum RegisterType {

    MOBILE(1, "手机号"),
    
    EMAIL(2, "邮箱"),
    
    QQ(3, "QQ"),
    
    WX(4, "微信");
    
    private int code;
    
    private String value;
    
    RegisterType(int code, String value) {
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
    public static RegisterType getEnum(int code) {
        RegisterType[] enums = RegisterType.values();
        for (RegisterType e : enums) {
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
        RegisterType[] enums = RegisterType.values();
        for (RegisterType e : enums) {
            map.put(e.getCode(), e.getValue());
        }
        return map;
    }
}
