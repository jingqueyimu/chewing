package com.jingqueyimu.model.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限分组
 *
 * @author zhuangyilian
 */
public enum PermissionGroup {

    HOME("home", "首页"),
    
    USER("user", "用户管理"),
    
    ADMIN("admin", "管理员管理"),
    
    PRODUCT("product", "产品管理"),
    
    ORDER("order", "订单管理"),
    
    FEEDBACK("feedback", "反馈管理"),
    
    SYSTEM("system", "系统管理"),
    
    OTHER("other", "其他");
    
    private String code;
    
    private String value;
    
    PermissionGroup(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
    public static PermissionGroup getEnum(String code) {
        PermissionGroup[] enums = PermissionGroup.values();
        for (PermissionGroup e : enums) {
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
    public static Map<String, String> getEnumInfo() {
        Map<String, String> map = new HashMap<String, String>();
        PermissionGroup[] enums = PermissionGroup.values();
        for (PermissionGroup e : enums) {
            map.put(e.getCode(), e.getValue());
        }
        return map;
    }
}
