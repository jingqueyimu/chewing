package com.jingqueyimu.model.vo;

import java.util.Date;

import com.jingqueyimu.model.BaseModel;

/**
 * 意见反馈VO
 *
 * @author zhuangyilian
 */
public class FeedbackVO extends BaseModel {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 内容
     */
    private String content;
    
    /**
     * 回复
     */
    private String reply;
    
    /**
     * 类型(字典: FeedbackType)
     */
    private Integer type;
    
    /**
     * 状态(字典: FeedbackStatus)
     */
    private Integer status;
    
    private Date gmtCreate;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
