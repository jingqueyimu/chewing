package com.jingqueyimu.service;

import org.quartz.Job;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.mapper.ScheduleJobMapper;
import com.jingqueyimu.model.ScheduleJob;
import com.jingqueyimu.model.dict.ScheduleJobExecuteStatus;
import com.jingqueyimu.model.dict.ScheduleJobStatus;
import com.jingqueyimu.service.component.SchedulerService;

/**
 * 调度任务服务
 *
 * @author zhuangyilian
 */
@Service
public class ScheduleJobService extends BaseService<ScheduleJob> {
    
    @Autowired
    private ScheduleJobMapper scheduleJobMapper;
    @Autowired
    private SchedulerService schedulerService;
    
    /**
     * 执行调度任务
     *
     * @param job
     * @throws SchedulerException
     */
    public void runJob(ScheduleJob job) throws SchedulerException {
        if (job.getStatus() != ScheduleJobStatus.OPEN.getCode()) {
            throw new AppException(StatusCode.ERR_BIZ, "任务未开启");
        }
        schedulerService.runJob(String.valueOf(job.getId()));
    }
    
    /**
     * 添加调度任务
     *
     * @param job
     * @throws Exception
     */
    public void addJob(ScheduleJob job) throws Exception {
        Class<?> clazz = Class.forName(job.getJobClass());
        Job quartzJob = (Job) clazz.newInstance();
        schedulerService.addJob(quartzJob.getClass(), String.valueOf(job.getId()), job.getCron());
    }
    
    /**
     * 删除调度任务
     *
     * @param job
     * @throws SchedulerException
     */
    public void deleteJob(ScheduleJob job) throws SchedulerException {
        schedulerService.deleteJob(String.valueOf(job.getId()));
    }
    
    /**
     * 暂停调度任务
     *
     * @param job
     * @throws SchedulerException
     */
    public void pauseJob(ScheduleJob job) throws SchedulerException {
        schedulerService.pauseJob(String.valueOf(job.getId()));
    }
    
    /**
     * 恢复调度任务
     *
     * @param job
     * @throws SchedulerException
     */
    public void resumeJob(ScheduleJob job) throws SchedulerException {
        schedulerService.resumeJob(String.valueOf(job.getId()));
    }

    /**
     * 根据任务作业class名称查询调度任务
     *
     * @param jobClass
     * @return
     */
    public ScheduleJob getByJobClass(String jobClass) {
        ScheduleJob job = new ScheduleJob();
        job.setJobClass(jobClass);
        return scheduleJobMapper.selectOne(job);
    }
    
    /**
     * 创建调度任务
     *
     * @param jobName
     * @param jobClass
     * @param jobGroup
     * @param cron
     * @return
     */
    public ScheduleJob createScheduleJob(String jobName, String jobClass, String jobGroup, String cron) {
        ScheduleJob job = getByJobClass(jobClass);
        if (job != null) {
            return job;
        }
        job = new ScheduleJob();
        job.setJobName(jobName);
        job.setJobClass(jobClass);
        job.setJobGroup(jobGroup);
        job.setCron(cron);
        job.setStatus(ScheduleJobStatus.OPEN.getCode());
        job.setLastTakeTime(0L);
        job.setExecuteStatus(ScheduleJobExecuteStatus.NO_EXECUTE.getCode());
        return save(job);
    }

    /**
     * 更新调度任务状态
     *
     * @param id
     * @param status
     * @return
     * @throws Exception
     */
    public ScheduleJob updateStatus(long id, int status) throws Exception {
        ScheduleJob job = getById(id);
        if (job == null) {
            throw new AppException("调度任务不存在");
        }
        switch (ScheduleJobStatus.getEnum(status)) {
        case OPEN:
            addJob(job);
            break;
        case CLOSE:
            deleteJob(job);
            break;
        default:
            throw new AppException("无效的任务状态");
        }
        job.setStatus(status);
        return update(job);        
    }

    /**
     * 修改调度频率
     *
     * @param id
     * @param cron
     * @return
     * @throws SchedulerException
     */
    public ScheduleJob updateCron(long id, String cron) throws SchedulerException {
        ScheduleJob job = getById(id);
        if (job == null) {
            throw new AppException("调度任务不存在");
        }
        schedulerService.updateCron(String.valueOf(id), cron);
        job.setCron(cron);
        return update(job);          
    }
}
