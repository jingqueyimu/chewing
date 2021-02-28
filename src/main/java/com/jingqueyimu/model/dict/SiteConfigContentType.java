package com.jingqueyimu.model.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 网站配置内容类型
 *
 * @author zhuangyilian
 */
public enum SiteConfigContentType {

    TEXT(1, "文本"),
    
    IMAGE(2, "图片");
    
    private int code;
    
    private String value;
    
    SiteConfigContentType(int code, String value) {
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
    public static SiteConfigContentType getEnum(int code) {
        SiteConfigContentType[] enums = SiteConfigContentType.values();
        for (SiteConfigContentType e : enums) {
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
        SiteConfigContentType[] enums = SiteConfigContentType.values();
        for (SiteConfigContentType e : enums) {
            map.put(e.getCode(), e.getValue());
        }
        return map;
    }
}
