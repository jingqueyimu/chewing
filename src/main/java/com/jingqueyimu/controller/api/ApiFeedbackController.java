package com.jingqueyimu.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.controller.BaseController;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.FeedbackService;

/**
 * 意见反馈控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/api/feedback")
public class ApiFeedbackController extends BaseController {
    
    @Autowired
    private FeedbackService feedbackService;
    
    /**
     * 提交反馈
     *
     * @param params
     * @return
     */
    @PostMapping(value="/submit")
    public ResultData submit(@RequestBody JSONObject params) {
        params.put("userId", userContext.getCurrUser().getId());
        feedbackService.submit(params);
        return ResultData.succ();
    }
}
