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
 * 产品实体类
 *
 * @author zhuangyilian
 */
@Entity()
@Table(name="t_product", indexes={
        @Index(name="idx_name", columnList="name")})
public class Product extends BaseModel {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 产品名称
     */
    @Column(name="name", nullable=false)
    private String name;
    
    /**
     * 产品主图
     */
    @Column(name="image_url", length=1024)
    private String imageUrl;
    
    /**
     * 产品附图
     */
    @Column(name="sub_image_url", columnDefinition="text")
    private String subImageUrl;
       
    /**
     * 产品价格
     */
    @Column(name="price", columnDefinition="decimal(20,2)")
    private Double price;
    
    /**
     * 库存数量
     */
    @Column(name="stock_count")
    private Integer stockCount;
    
    /**
     * 产品描述
     */
    @Column(name="description")
    private String description;
    
    /**
     * 产品状态(字典: ProductStatus)
     */
    @Column(name="status", columnDefinition="tinyint")
    private Integer status;
    
    /**
     * 排序
     */
    @Column(name="sort")
    private Integer sort;
    
    /**
     * 类目ID
     */
    @Column(name="category_id")
    private Long categoryId;
    
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSubImageUrl() {
        return subImageUrl;
    }

    public void setSubImageUrl(String subImageUrl) {
        this.subImageUrl = subImageUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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