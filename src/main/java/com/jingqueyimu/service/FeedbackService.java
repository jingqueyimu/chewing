package com.jingqueyimu.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jingqueyimu.mapper.FeedbackMapper;
import com.jingqueyimu.model.Feedback;
import com.jingqueyimu.model.dict.FeedbackStatus;
import com.jingqueyimu.model.vo.FeedbackVO;
import com.jingqueyimu.util.SysUtil;

/**
 * 意见反馈服务
 *
 * @author zhuangyilian
 */
@Service
public class FeedbackService extends BaseService<Feedback> {
    
    @Autowired
    private FeedbackMapper feedbackMapper;

    /**
     * 提交反馈
     *
     * @param params
     * @return
     */
    public Feedback submit(JSONObject params) {
        SysUtil.checkParam(params, "userId", "获取用户ID异常");
        SysUtil.checkParam(params, "type", "请选择反馈类型");
        SysUtil.checkParam(params, "content", "请输入反馈内容");
        long userId = params.getLongValue("userId");
        Integer type = params.getInteger("type");
        String content = params.getString("content");
        
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setContent(content);
        feedback.setType(type);
        feedback.setStatus(FeedbackStatus.WAIT.getCode());
        return save(feedback);
    }

    /**
     * 分页查询意见反馈
     *
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    public PageInfo<FeedbackVO> pageFeedback(int pageNum, int pageSize, JSONObject params) {
        if (pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<FeedbackVO> list = feedbackMapper.listFeedback(params);
        PageInfo<FeedbackVO> page = new PageInfo<FeedbackVO>(list);
        return page;
    }

    /**
     * 回复意见反馈
     *
     * @param id
     * @param reply
     * @return
     */
    public Feedback reply(long id, String reply) {
        Feedback feedback = getById(id);
        if (feedback == null) {
            throw new RuntimeException("意见反馈记录不存在");
        }
        if (feedback.getStatus() != FeedbackStatus.WAIT.getCode()) {
            throw new RuntimeException("意见反馈已处理");
        }
        feedback.setReply(reply);
        feedback.setStatus(FeedbackStatus.DONE.getCode());
        feedback.setHandleTime(new Date());
        feedback = update(feedback);
        // TODO 站内信
        return feedback;
    }
}
