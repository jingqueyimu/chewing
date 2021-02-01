package com.jingqueyimu.service.component;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 调度器服务
 *
 * @author zhuangyilian
 */
@Service
public class SchedulerService {
    
    @Autowired
    private Scheduler scheduler;
    
    /**
     * 执行调度任务
     *
     * @param jobId
     * @throws SchedulerException
     */
    public void runJob(String jobId) throws SchedulerException {
        scheduler.triggerJob(JobKey.jobKey(jobId));
    }
    
    /**
     * 添加调度任务
     *
     * @param jobClass
     * @param jobId
     * @param cron
     * @throws SchedulerException
     */
    public void addJob(Class<? extends Job> jobClass, String jobId, String cron) throws SchedulerException {
        if (hasJob(jobId)) {
            return;
        }
        // 构建任务作业实例
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobId).build();
        // cron表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        // cron表达式触发器
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobId).withSchedule(scheduleBuilder).build();
        // 添加任务作业实例到调度器中,并关联一个指定的触发器
        scheduler.scheduleJob(jobDetail, trigger);
    }
    
    /**
     * 删除调度任务
     *
     * @param jobId
     * @throws SchedulerException
     */
    public void deleteJob(String jobId) throws SchedulerException {
        scheduler.deleteJob(JobKey.jobKey(jobId));
    }
    
    /**
     * 暂停调度任务
     *
     * @param jobId
     * @throws SchedulerException
     */
    public void pauseJob(String jobId) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(jobId));
    }
    
    /**
     * 恢复调度任务
     *
     * @param jobId
     * @throws SchedulerException
     */
    public void resumeJob(String jobId) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(jobId));
    }
    
    /**
     * 更新调度频率
     *
     * @param jobId
     * @param cron
     * @throws SchedulerException 
     */
    public void updateCron(String jobId, String cron) throws SchedulerException {
        if (!hasJob(jobId)) {
            return;
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(jobId);
        // 获取cron表达式触发器
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, trigger);
    }
    
    /**
     * 是否有指定任务
     *
     * @param jobId
     * @return
     * @throws SchedulerException
     */
    public boolean hasJob(String jobId) throws SchedulerException {
        JobKey key = JobKey.jobKey(jobId);
        return scheduler.getJobDetail(key) != null;
    }
}
