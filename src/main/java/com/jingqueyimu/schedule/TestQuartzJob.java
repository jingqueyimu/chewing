package com.jingqueyimu.schedule;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import com.jingqueyimu.util.DateUtil;

/**
 * Quartz任务测试
 *
 * @author zhuangyilian
 */
@Component
public class TestQuartzJob extends BaseJob {

    @Override
    protected void doExecute(JobExecutionContext context) {
        System.out.println("TestQuartzJob: " + DateUtil.format(new Date(), DateUtil.FORMAT_DATE_TIME));
    }

    @Override
    public String name() {
        return "Quartz任务测试";
    }

    @Override
    public String group() {
        return "test";
    }

    @Override
    public String cron() {
        return "0 */5 * * * ?";
    }
}
