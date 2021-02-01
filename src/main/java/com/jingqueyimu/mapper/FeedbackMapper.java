package com.jingqueyimu.mapper;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.model.Feedback;
import com.jingqueyimu.model.vo.FeedbackVO;

import tk.mybatis.mapper.common.Mapper;

/**
 * 意见反馈数据映射
 *
 * @author zhuangyilian
 */
public interface FeedbackMapper extends Mapper<Feedback> {

    /**
     * 查询意见反馈列表
     *
     * @param params
     * @return
     */
    List<FeedbackVO> listFeedback(JSONObject params);
}
