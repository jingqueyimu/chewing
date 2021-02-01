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
import com.jingqueyimu.model.Feedback;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.FeedbackService;
import com.jingqueyimu.util.SysUtil;

/**
 * 意见反馈控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/console/feedback")
public class ConsoleFeedbackController extends BaseController {
    
    @Autowired
    private FeedbackService feedbackService;
    
    /**
     * 分页查询意见反馈
     *
     * @param params
     * @return
     */
    @Perm(group="feedback", name="分页查询意见反馈", description="反馈管理-意见反馈列表-分页查询意见反馈")
    @PostMapping("/page")
    public ResultData page(@RequestBody JSONObject params) {
        int pageNum = params.getIntValue("pageNum");
        int pageSize = params.getIntValue("pageSize");
        PageInfo<Feedback> page = feedbackService.page(pageNum, pageSize, params);
        return ResultData.succ(page);
    }
    
    /**
     * 回复意见反馈
     *
     * @param params
     * @return
     */
    @Perm(group="feedback", name="回复意见反馈", description="反馈管理-意见反馈列表-回复意见反馈")
    @PostMapping("/reply")
    public ResultData reply(@RequestBody JSONObject params) {
        SysUtil.checkParam(params, "id", "意见反馈ID不能为空");
        SysUtil.checkParam(params, "reply", "回复内容不能为空");
        long id = params.getLongValue("id");
        String reply = params.getString("reply");
        Feedback feedback = feedbackService.reply(id, reply);
        return ResultData.succ(feedback);
    }
}
