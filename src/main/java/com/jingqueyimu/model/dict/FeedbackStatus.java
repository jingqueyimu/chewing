package com.jingqueyimu.model.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 反馈类型
 *
 * @author zhuangyilian
 */
public enum FeedbackStatus {

    WAIT(0, "待处理"),
    
    DONE(1, "已处理");
    
    private int code;
    
    private String value;
    
    FeedbackStatus(int code, String value) {
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
    public static FeedbackStatus getEnum(int code) {
        FeedbackStatus[] enums = FeedbackStatus.values();
        for (FeedbackStatus e : enums) {
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
        FeedbackStatus[] enums = FeedbackStatus.values();
        for (FeedbackStatus e : enums) {
            map.put(e.getCode(), e.getValue());
        }
        return map;
    }
}
