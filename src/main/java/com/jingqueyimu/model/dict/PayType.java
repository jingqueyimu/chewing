package com.jingqueyimu.model.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 付款方式
 *
 * @author zhuangyilian
 */
public enum PayType {
    
    OFFLINE(1, "线下付款"),

    WECHAT(2, "微信付款"),
    
    ALIPAY(3, "支付宝付款");
    
    private int code;
    
    private String value;
    
    PayType(int code, String value) {
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
    public static PayType getEnum(int code) {
        PayType[] enums = PayType.values();
        for (PayType e : enums) {
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
        PayType[] enums = PayType.values();
        for (PayType e : enums) {
            map.put(e.getCode(), e.getValue());
        }
        return map;
    }
}
