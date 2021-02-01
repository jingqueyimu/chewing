package com.jingqueyimu.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.constant.BizConstant;
import com.jingqueyimu.constant.CacheConstant;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.context.AdminContext;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.mapper.AdminPermissionMapper;
import com.jingqueyimu.model.AdminPermission;
import com.jingqueyimu.model.Permission;
import com.jingqueyimu.service.component.RedisService;

/**
 * 管理员权限服务
 *
 * @author zhuangyilian
 */
@Service
public class AdminPermissionService extends BaseService<AdminPermission> {
    
    @Autowired
    private AdminPermissionMapper adminPermissionMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private AdminContext adminContext;
    
    /**
     * 保存管理员权限
     *
     * @param adminId
     * @param permission
     */
    public AdminPermission save(long adminId, Permission permission) {
        AdminPermission adminPermission = new AdminPermission();
        adminPermission.setAdminId(adminId);
        adminPermission.setPermissionId(permission.getId());
        AdminPermission resAdminPermission = adminPermissionMapper.selectOne(adminPermission);
        if (resAdminPermission != null) {
            return resAdminPermission;
        } else {
            adminPermission.setPermissionPath(permission.getPath());
            return save(adminPermission);
        }
    }
    
    /**
     * 查询管理员权限
     *
     * @param adminId
     * @return
     */
    public List<AdminPermission> listByAdminId(long adminId) {
        JSONObject params = new JSONObject();
        params.put("adminId", adminId);
        return list(params);
    }
    
    /**
     * 查询管理员权限地址
     *
     * @param adminId
     * @return
     */
    public List<String> listPathByAdminId(long adminId) {
        List<AdminPermission> list = listByAdminId(adminId);
        List<String> paths = new ArrayList<>();
        for (AdminPermission adminPermission : list) {
            paths.add(adminPermission.getPermissionPath());
        }
        return paths;
    }
    
    /**
     * 更新管理员权限
     *
     * @param adminId
     * @param permissions
     */
    public void updateAdminPermission(long adminId, List<JSONObject> permissions) {
        if (adminId == BizConstant.SUPER_ADMIN_ID) {
            throw new AppException(StatusCode.ERR_BIZ, "不允许编辑超级管理员的权限");
        }
        if (adminId == adminContext.getCurrAdmin().getId()) {
            throw new AppException(StatusCode.ERR_BIZ, "不允许编辑自己的权限");
        }
        // 旧权限ID
        List<Long> oldPermissionIds = new ArrayList<>();
        List<AdminPermission> oldAdminPermissions = listByAdminId(adminId);
        for (AdminPermission adminPermission : oldAdminPermissions) {
            oldPermissionIds.add(adminPermission.getPermissionId());
        }
        Long permissionId = 0L;
        String permissionPath = null;
        AdminPermission adminPermisson = null;
        for (JSONObject json : permissions) {
            permissionId = json.getLong("id");
            permissionPath = json.getString("path");
            // 已有该权限
            if (oldPermissionIds.contains(permissionId)) {
                oldPermissionIds.remove(permissionId);
            } else {
                // 原来没有,现在有,则新增
                adminPermisson = new AdminPermission();
                adminPermisson.setAdminId(adminId);
                adminPermisson.setPermissionId(permissionId);
                adminPermisson.setPermissionPath(permissionPath);
                save(adminPermisson);
            }
        }
        // 原来有,现在没有,则删除
        for (Long oldPermissionId : oldPermissionIds) {
            adminPermisson = new AdminPermission();
            adminPermisson.setAdminId(adminId);
            adminPermisson.setPermissionId(oldPermissionId);
            delete(adminPermisson);
        }
        // 更新缓存中的管理员权限
        redisService.set(CacheConstant.ADMIN_PERMISSION + adminId, listPathByAdminId(adminId));
    }
}
