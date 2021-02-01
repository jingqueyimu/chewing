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
 * 类目实体类
 *
 * @author zhuangyilian
 */
@Entity()
@Table(name="t_category", indexes={
        @Index(name="idx_name", columnList="name")})
public class Category extends BaseModel {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 类目名
     */
    @Column(name="name", length=16, nullable=false)
    private String name;
    
    /**
     * 父类目ID
     */
    @Column(name="parent_id")
    private Long parentId;

    /**
     * 备注
     */
    @Column(name="remark", length=255)
    private String remark;
    
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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