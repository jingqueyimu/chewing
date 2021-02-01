package com.jingqueyimu.model.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 产品状态
 *
 * @author zhuangyilian
 */
public enum ProductStatus {

    UP(0, "上架"),
    
    DOWN(1, "下架");
    
    private int code;
    
    private String value;
    
    ProductStatus(Integer code, String value) {
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
    public static ProductStatus getEnum(int code) {
        ProductStatus[] enums = ProductStatus.values();
        for (ProductStatus e : enums) {
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
        ProductStatus[] enums = ProductStatus.values();
        for (ProductStatus e : enums) {
            map.put(e.getCode(), e.getValue());
        }
        return map;
    }
}
