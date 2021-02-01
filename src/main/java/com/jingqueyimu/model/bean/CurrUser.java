package com.jingqueyimu.model.bean;

import java.util.Date;

import javax.persistence.Column;

import com.jingqueyimu.model.BaseModel;

/**
 * 当前登录用户
 *
 * @author zhuangyilian
 */
public class CurrUser extends BaseModel {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 头像
     */
    @Column(name="avatar", length=1024)
    private String avatar;
    
    /**
     * 真实姓名
     */
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
     * 生日
     */
    private Date birthday;
    
    /**
     * 注册方式(字典: RegisterType)
     */
    private Integer registerType;
    
    /**
     * 注册时间
     */
    private Date registerTime;
    
    /**
     * 上次登录时间
     */
    private Date lastLoginTime;
    
    /**
     * 是否VIP
     */
    private Boolean vipFlag;
    
    /**
     * VIP到期时间
     */
    private Date vipExpireTime;
    
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getRegisterType() {
        return registerType;
    }

    public void setRegisterType(Integer registerType) {
        this.registerType = registerType;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Boolean getVipFlag() {
        return vipFlag;
    }

    public void setVipFlag(Boolean vipFlag) {
        this.vipFlag = vipFlag;
    }

    public Date getVipExpireTime() {
        return vipExpireTime;
    }

    public void setVipExpireTime(Date vipExpireTime) {
        this.vipExpireTime = vipExpireTime;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}