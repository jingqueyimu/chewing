package com.jingqueyimu.controller.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jingqueyimu.annotation.Perm;
import com.jingqueyimu.controller.BaseController;
import com.jingqueyimu.model.ScheduleJob;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.ScheduleJobService;
import com.jingqueyimu.util.SysUtil;

/**
 * 调度任务控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/console/schedule_job")
public class ConsoleScheduleJobController extends BaseController {
    
    @Autowired
    private ScheduleJobService scheduleJobService;
    
    /**
     * 分页查询任务
     *
     * @param params
     * @return
     */
    @Perm(group="system", name="分页查询任务", description="系统管理-调度列表-分页查询任务")
    @PostMapping("/page")
    public ResultData page(@RequestBody JSONObject params) {
        int pageNum = params.getIntValue("pageNum");
        int pageSize = params.getIntValue("pageSize");
        PageInfo<ScheduleJob> page = scheduleJobService.page(pageNum, pageSize, params);
        return ResultData.succ(page);
    }
    
    /**
     * 获取调度任务
     *
     * @param params
     * @return
     */
    @Perm(group="system", name="获取调度任务", description="系统管理-调度列表-获取调度任务")
    @RequestMapping("/get")
    public ResultData get(@RequestBody JSONObject params) {
        if (params.isEmpty()) {
            throw new RuntimeException("查询参数不能为空");
        }
        ScheduleJob job = scheduleJobService.get(params);
        return ResultData.succ(job);
    }
    
    /**
     * 执行调度任务
     *
     * @param params
     * @return
     * @throws Exception 
     */
    @Perm(group="system", name="执行调度任务", description="系统管理-调度列表-执行调度任务")
    @RequestMapping("/run")
    public ResultData run(@RequestBody JSONObject params) throws Exception{
        long id = params.getLongValue("id");
        ScheduleJob job = scheduleJobService.getById(id);
        scheduleJobService.runJob(job);
        return ResultData.succ();
    }
    
    /**
     * 修改任务状态
     *
     * @param params
     * @return
     * @throws Exception 
     */
    @Perm(group="system", name="修改任务状态", description="系统管理-调度列表-修改任务状态")
    @RequestMapping("/update_status")
    public ResultData updateStatus(@RequestBody JSONObject params) throws Exception{
        SysUtil.checkParam(params, "id", "请选择任务");
        SysUtil.checkParam(params, "status", "请选择任务状态");
        long id = params.getLongValue("id");
        int status = params.getIntValue("status");
        ScheduleJob job = scheduleJobService.updateStatus(id, status);
        return ResultData.succ(job);
    }
    
    /**
     * 修改调度频率
     *
     * @param params
     * @return
     * @throws Exception 
     */
    @Perm(group="system", name="修改调度频率", description="系统管理-调度列表-修改调度频率")
    @RequestMapping("/update_cron")
    public ResultData updateCron(@RequestBody JSONObject params) throws Exception{
        SysUtil.checkParam(params, "id", "请选择任务");
        SysUtil.checkParam(params, "cron", "请输入调度频率");
        long id = params.getLongValue("id");
        String cron = params.getString("cron");
        ScheduleJob job = scheduleJobService.updateCron(id, cron);
        return ResultData.succ(job);
    }
}
