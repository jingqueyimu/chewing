package com.jingqueyimu.model.bean;

import javax.persistence.Column;

import com.jingqueyimu.model.BaseModel;

/**
 * 当前登录管理员
 *
 * @author zhuangyilian
 */
public class CurrAdmin extends BaseModel {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    
    /**
     * 账户名
     */
    private String name;
    
    /**
     * 头像
     */
    @Column(name="avatar", length=1024)
    private String avatar;
    
    /**
     * 真实姓名
     */
    @Column(name="real_name", length=32)
    private String realName;
    
    /**
     * 手机号
     */
    private String mobile;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 是否锁定
     */
    private Boolean lockFlag;
    
    /**
     * 记住我
     */
    private Boolean rememberMe;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(Boolean lockFlag) {
        this.lockFlag = lockFlag;
    }
    
    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}