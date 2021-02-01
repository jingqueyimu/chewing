package com.jingqueyimu.model.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 调度任务执行状态
 *
 * @author zhuangyilian
 */
public enum ScheduleJobExecuteStatus {

    NO_EXECUTE(0, "未执行"),
    
    IN_EXECUTE(1, "执行中"),
    
    WAIT_EXECUTE(2, "待下次执行");
    
    private int code;
    
    private String value;
    
    ScheduleJobExecuteStatus(Integer code, String value) {
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
    public static ScheduleJobExecuteStatus getEnum(int code) {
        ScheduleJobExecuteStatus[] enums = ScheduleJobExecuteStatus.values();
        for (ScheduleJobExecuteStatus e : enums) {
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
        ScheduleJobExecuteStatus[] enums = ScheduleJobExecuteStatus.values();
        for (ScheduleJobExecuteStatus e : enums) {
            map.put(e.getCode(), e.getValue());
        }
        return map;
    }
}
