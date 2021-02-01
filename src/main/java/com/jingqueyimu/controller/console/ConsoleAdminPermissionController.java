package com.jingqueyimu.controller.console;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.annotation.Perm;
import com.jingqueyimu.controller.BaseController;
import com.jingqueyimu.model.AdminPermission;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.AdminPermissionService;
import com.jingqueyimu.util.SysUtil;

/**
 * 管理员权限控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/console/admin_permission")
public class ConsoleAdminPermissionController extends BaseController {
    
    @Autowired
    private AdminPermissionService adminPermissionService;
    
    /**
     * 查询管理员权限
     *
     * @param params
     * @return
     */
    @Perm(group="admin", name="查询管理员权限", description="管理员管理-管理员列表-查询管理员权限")
    @PostMapping("/list")
    public ResultData list(@RequestBody JSONObject params) {
        SysUtil.checkParams(params, "adminId");
        long adminId = params.getLongValue("adminId");
        List<AdminPermission> list = adminPermissionService.listByAdminId(adminId);
        return ResultData.succ(list);
    }
    
    /**
     * 查询管理员权限地址
     *
     * @param params
     * @return
     */
    @Perm(group="admin", name="查询管理员权限地址", description="管理员管理-管理员列表-查询管理员权限地址")
    @PostMapping("/list_path")
    public ResultData listPath(@RequestBody JSONObject params) {
        SysUtil.checkParams(params, "adminId");
        long adminId = params.getLongValue("adminId");
        List<String> list = adminPermissionService.listPathByAdminId(adminId);
        return ResultData.succ(list);
    }
    
    /**
     * 更新管理员权限
     *
     * @param params
     * @return
     */
    @Perm(group="admin", name="更新管理员权限", description="管理员管理-管理员列表-更新管理员权限")
    @PostMapping("/update_admin_permission")
    public ResultData updateAdminPermission(@RequestBody JSONObject params) {
        SysUtil.checkParams(params, "adminId");
        long adminId = params.getLongValue("adminId");
        List<JSONObject> permissions = JSONObject.parseArray(params.getString("permissions"), JSONObject.class);
        adminPermissionService.updateAdminPermission(adminId, permissions);
        return ResultData.succ();
    }
}
