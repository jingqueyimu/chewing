package com.jingqueyimu.model.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 调度任务状态
 *
 * @author zhuangyilian
 */
public enum ScheduleJobStatus {

    OPEN(0, "开启"),
    
    CLOSE(1, "关闭");
    
    private int code;
    
    private String value;
    
    ScheduleJobStatus(Integer code, String value) {
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
    public static ScheduleJobStatus getEnum(int code) {
        ScheduleJobStatus[] enums = ScheduleJobStatus.values();
        for (ScheduleJobStatus e : enums) {
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
        ScheduleJobStatus[] enums = ScheduleJobStatus.values();
        for (ScheduleJobStatus e : enums) {
            map.put(e.getCode(), e.getValue());
        }
        return map;
    }
}
