package com.jingqueyimu.model.vo;

import com.jingqueyimu.model.BaseModel;

/**
 * 权限VO
 *
 * @author zhuangyilian
 */
public class PermissionWithAccessVO extends BaseModel {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    
    /**
     * 权限路径
     */
    private String path;
    
    /**
     * 权限分组
     */
    private String groupCode;
    
    /**
     * 权限名称
     */
    private String name;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 管理员ID
     */
    private Long adminId;

    /**
     * 访问状态
     */
    private Boolean accessFlag;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Boolean getAccessFlag() {
        return accessFlag;
    }

    public void setAccessFlag(Boolean accessFlag) {
        this.accessFlag = accessFlag;
    }
}