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
 * 订单实体类
 *
 * @author zhuangyilian
 */
@Entity()
@Table(name="t_order", indexes={
        @Index(name="uk_biz_no", columnList="biz_no", unique=true)})
public class Order extends BaseModel {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 用户ID
     */
    @Column(name="user_id")
    private Long userId;
    
    /**
     * 订单金额
     */
    @Column(name="amount", columnDefinition="decimal(20,2)")
    private Double amount;
    
    /**
     * 平台业务号
     */
    @Column(name="biz_no", length=64, nullable=false)
    private String bizNo;
    
    /**
     * 第三方预支付订单号
     */
    @Column(name="pre_pay_no", length=64)
    private String prePayNo;
    
    /**
     * 第三方支付订单号
     */
    @Column(name="pay_no", length=64)
    private String payNo;
    
    /**
     * 第三方交易号
     */
    @Column(name="trade_no", length=64)
    private String tradeNo;
    
    /**
     * 付款方式(字典: PayType)
     */
    @Column(name="pay_type", columnDefinition="tinyint")
    private Integer payType;
    
    /**
     * 订单状态(字典: OrderStatus)
     */
    @Column(name="status", columnDefinition="tinyint")
    private Integer status;
    
    /**
     * 付款时间
     */
    @Column(name="pay_time")
    private Date payTime;
    
    /**
     * 完成时间
     */
    @Column(name="complete_time")
    private Date completeTime;
    
    /**
     * 备注
     */
    @Column(name="remark")
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getPrePayNo() {
        return prePayNo;
    }

    public void setPrePayNo(String prePayNo) {
        this.prePayNo = prePayNo;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
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