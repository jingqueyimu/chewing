package com.jingqueyimu.schedule;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.jingqueyimu.model.ScheduleJob;
import com.jingqueyimu.model.dict.ScheduleJobExecuteStatus;
import com.jingqueyimu.model.dict.ScheduleJobStatus;
import com.jingqueyimu.service.ScheduleJobService;

/**
 * 基类任务
 *
 * @author zhuangyilian
 */
@DisallowConcurrentExecution
public abstract class BaseJob extends QuartzJobBean {
    
    public final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private static boolean isInit = false;
    
    @Autowired
    private ScheduleJobService scheduleJobService;
    
    /**
     * 初始化调度任务
     * @throws SchedulerException 
     */
    @PostConstruct
    protected void init() throws Exception {
        if (isInit) {
            return;
        }
        isInit = true;
        Class<? extends BaseJob> jobClass = this.getClass();
        // 创建调度任务
        ScheduleJob job = scheduleJobService.createScheduleJob(name(), jobClass.getName(), group(), cron());
        // 初始化任务状态
        switch (ScheduleJobStatus.getEnum(job.getStatus())) {
        case OPEN:
            scheduleJobService.addJob(job);
            break;
        case CLOSE:
            break;
        default:
            break;
        }
    }
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Class<? extends BaseJob> jobClass = this.getClass();
        ScheduleJob job = scheduleJobService.getByJobClass(jobClass.getName());
        if (job == null) {
            return;
        }
        long beginTime = System.currentTimeMillis();
        job.setBeginTime(new Date(beginTime));
        job.setExecuteStatus(ScheduleJobExecuteStatus.IN_EXECUTE.getCode());
        job = scheduleJobService.update(job);
        try {
            doExecute(context);
        } catch (Exception e) {
            log.error("调度任务执行失败", e);
        }
        job.setLastExecuteTime(job.getBeginTime());
        job.setLastTakeTime(System.currentTimeMillis() - beginTime);
        job.setBeginTime(null);
        job.setExecuteStatus(ScheduleJobExecuteStatus.WAIT_EXECUTE.getCode());
        scheduleJobService.update(job);
    }
    
    /**
     * 执行任务
     *
     * @param context
     */
    protected abstract void doExecute(JobExecutionContext context);
    
    /**
     * 任务名称
     *
     * @return
     */
    public abstract String name();
    
    /**
     * 任务分组
     *
     * @return
     */
    public abstract String group();
    
    /**
     * cron表达式
     *
     * @return
     */
    public abstract String cron();
}
