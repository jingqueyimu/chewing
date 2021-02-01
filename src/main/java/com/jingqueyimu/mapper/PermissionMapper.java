package com.jingqueyimu.mapper;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.model.Permission;
import com.jingqueyimu.model.vo.PermissionWithAccessVO;

import tk.mybatis.mapper.common.Mapper;

/**
 * 权限数据映射
 *
 * @author zhuangyilian
 */
public interface PermissionMapper extends Mapper<Permission> {

    /**
     * 查询带访问状态的权限
     *
     * @param params
     * @return
     */
    List<PermissionWithAccessVO> listWithAccess(JSONObject params);
    
}
