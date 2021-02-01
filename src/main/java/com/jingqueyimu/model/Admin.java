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
 * 管理员实体类
 *
 * @author zhuangyilian
 */
@Entity()
@Table(name="t_admin", indexes={
        @Index(name="uk_name", columnList="name", unique=true)})
public class Admin extends BaseModel {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 账户名
     */
    @Column(name="name", length=32, nullable=false)
    private String name;
    
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
     * 是否锁定
     */
    @Column(name="lock_flag", columnDefinition="bit(1) default 0", nullable=false)
    private Boolean lockFlag;
    
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(Boolean lockFlag) {
        this.lockFlag = lockFlag;
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