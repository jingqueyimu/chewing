package com.jingqueyimu.schedule;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jingqueyimu.util.DateUtil;

/**
 * 调度任务测试
 *
 * @author zhuangyilian
 */
@Component
public class TestScheduleJob {
    
    @Scheduled(cron="0 */5 * * * ?")
    public void printTime() {
        System.out.println("TestScheduleJob: " + DateUtil.format(new Date(), DateUtil.FORMAT_DATE_TIME));
    }
}
