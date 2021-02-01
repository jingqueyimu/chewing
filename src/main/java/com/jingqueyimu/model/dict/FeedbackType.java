package com.jingqueyimu.model.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 反馈类型
 *
 * @author zhuangyilian
 */
public enum FeedbackType {

    PROBLEM(1, "问题反馈"),
    
    SUGGESTION(2, "功能建议"),
    
    OTHER(99, "其他");
    
    private int code;
    
    private String value;
    
    FeedbackType(int code, String value) {
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
    public static FeedbackType getEnum(int code) {
        FeedbackType[] enums = FeedbackType.values();
        for (FeedbackType e : enums) {
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
        FeedbackType[] enums = FeedbackType.values();
        for (FeedbackType e : enums) {
            map.put(e.getCode(), e.getValue());
        }
        return map;
    }
}
