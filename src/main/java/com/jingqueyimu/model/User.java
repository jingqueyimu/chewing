package com.jingqueyimu.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 用户实体类
 *
 * @author zhuangyilian
 */
@Entity()
@Table(name="t_user", indexes={
        @Index(name="uk_username", columnList="username", unique=true), 
        @Index(name="idx_register_time", columnList="register_time")})
public class User extends BaseModel {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 用户名
     */
    @Column(name="username", length=32, nullable=false)
    private String username;
    
    /**
     * 密码
     */
    @Column(name="password", length=64)
    private String password;
    
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
    @Column(name="mobile", length=11)
    private String mobile;
    
    /**
     * 邮箱
     */
    @Column(name="email", length=64)
    private String email;
    
    /**
     * 生日
     */
    @Column(name="birthday")
    private Date birthday;
    
    /**
     * 注册方式(字典: RegisterType)
     */
    @Column(name="register_type")
    private Integer registerType;
    
    /**
     * 注册时间
     */
    @Column(name="register_time")
    private Date registerTime;
    
    /**
     * 上次登录时间
     */
    @Column(name="last_login_time")
    private Date lastLoginTime;
    
    /**
     * 是否VIP
     */
    @Column(name="vip_flag", columnDefinition="bit(1) default 0", nullable=false)
    private Boolean vipFlag;
    
    /**
     * VIP到期时间
     */
    @Column(name="vip_expire_time")
    private Date vipExpireTime;
    
    /**
     * 创建时间
     */
    @Column(name="gmt_create", nullable=false)
    private Date gmtCreate;
    
    /**
     * 修改时间
     */
    @Column(name="gmt_modify")
    private Date gmtModify;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }
}