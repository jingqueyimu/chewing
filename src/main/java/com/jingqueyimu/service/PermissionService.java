package com.jingqueyimu.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.mapper.PermissionMapper;
import com.jingqueyimu.model.Permission;
import com.jingqueyimu.model.dict.PermissionGroup;
import com.jingqueyimu.model.vo.PermissionWithAccessVO;

/**
 * 权限服务
 *
 * @author zhuangyilian
 */
@Service
public class PermissionService extends BaseService<Permission> {
    
    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 根据路径保存或更新权限
     *
     * @param path
     * @param groupCode
     * @param name
     * @param description
     * @return
     */
    public Permission saveOrUpdateByPath(String path, String groupCode, String name, String description) {
        if (StringUtils.isBlank(groupCode)) {
            groupCode = PermissionGroup.OTHER.getCode();
        }
        Permission permission = new Permission();
        permission.setPath(path);
        Permission resPermission = permissionMapper.selectOne(permission);
        if (resPermission == null) {
            permission.setGroupCode(groupCode);
            permission.setName(name);
            permission.setDescription(description);
            return save(permission);
        }
        boolean isUpdate = StringUtils.isNotBlank(groupCode) && !groupCode.equals(resPermission.getGroupCode()) 
                || StringUtils.isNotBlank(name) && !name.equals(resPermission.getName())
                || StringUtils.isNotBlank(description) && !description.equals(resPermission.getDescription());
        if (!isUpdate) {
            return resPermission;
        }
        resPermission.setGroupCode(groupCode);
        resPermission.setName(name);
        resPermission.setDescription(description);
        return update(resPermission);
    }

    /**
     * 查询带访问状态的权限
     *
     * @param params
     * @return
     */
    public List<PermissionWithAccessVO> listWithAccess(JSONObject params) {
        return permissionMapper.listWithAccess(params);
    }
}
