package com.jingqueyimu.mapper;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.model.User;

import tk.mybatis.mapper.common.Mapper;

/**
 * 用户数据映射
 *
 * @author zhuangyilian
 */
public interface UserMapper extends Mapper<User>{

    /**
     * 根据账号和密码查询用户
     *
     * @param params
     * @return
     */
    User getByAccountAndPassword(JSONObject params);
}
