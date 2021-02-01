package com.jingqueyimu.model.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单状态
 *
 * @author zhuangyilian
 */
public enum OrderStatus {

    WAIT(0, "待付款"),
    
    DOING(1, "付款中"),
    
    SUCC(2, "付款成功"),
    
    FAIL(3, "付款失败"),
    
    CANCEL(4, "放弃付款");
    
    private int code;
    
    private String value;
    
    OrderStatus(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
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
    public static OrderStatus getEnum(int code) {
        OrderStatus[] enums = OrderStatus.values();
        for (OrderStatus e : enums) {
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
        OrderStatus[] enums = OrderStatus.values();
        for (OrderStatus e : enums) {
            map.put(e.getCode(), e.getValue());
        }
        return map;
    }
}
