package com.jingqueyimu.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 公告实体类
 *
 * @author zhuangyilian
 */
@Entity()
@Table(name="t_notice")
public class Notice extends BaseModel {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 标题
     */
    @Column(name="title", length=255)
    private String title;
    
    /**
     * 内容
     */
    @Column(name="content", columnDefinition="longtext")
    private String content;
    
    /**
     * 摘要
     */
    @Column(name="summary", length=255)
    private String summary;
    
    /**
     * 图片
     */
    @Column(name="image", columnDefinition="longtext")
    private String image;
    
    /**
     * 阅读数
     */
    @Column(name="read_count")
    private Integer readCount;
    
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
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