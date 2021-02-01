package com.jingqueyimu.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 调度任务实体类
 *
 * @author zhuangyilian
 */
@Entity()
@Table(name="t_schedule_job")
public class ScheduleJob extends BaseModel {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 任务名
     */
    @Column(name="job_name", length=128)
    private String jobName;
    
    /**
     * 任务类
     */
    @Column(name="job_class", length=255)
    private String jobClass;
    
    /**
     * 任务分组
     */
    @Column(name="job_group", length=64)
    private String jobGroup;
    
    /**
     * cron表达式 
     */
    @Column(name="cron", length=255)
    private String cron;
    
    /**
     * 状态(字典: ScheduleJobStatus)
     */
    @Column(name="status")
    private Integer status;
    
    /**
     * 最后执行时间
     */
    @Column(name="last_execute_time")
    private Date lastExecuteTime;
    
    /**
     * 最后执行耗时
     */
    @Column(name="last_take_time")
    private Long lastTakeTime;
    
    /**
     * 开始时间
     */
    @Column(name="begin_time")
    private Date beginTime;
    
    /**
     * 执行状态(字典: ScheduleJobExecuteStatus)
     */
    @Column(name="execute_status")
    private Integer executeStatus;
    
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

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLastExecuteTime() {
        return lastExecuteTime;
    }

    public void setLastExecuteTime(Date lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }

    public Long getLastTakeTime() {
        return lastTakeTime;
    }

    public void setLastTakeTime(Long lastTakeTime) {
        this.lastTakeTime = lastTakeTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Integer getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(Integer executeStatus) {
        this.executeStatus = executeStatus;
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