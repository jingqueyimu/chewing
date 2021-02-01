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
import com.jingqueyimu.model.Permission;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.PermissionService;

/**
 * 权限控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/console/permission")
public class ConsolePermissionController extends BaseController {
    
    @Autowired
    private PermissionService permissionService;
    
    /**
     * 分页查询权限
     *
     * @param params
     * @return
     */
    @Perm(group="admin", name="分页查询权限", description="管理员管理-管理员列表-分页查询权限")
    @PostMapping("/page")
    public ResultData page(@RequestBody JSONObject params) {
        int pageNum = params.getIntValue("pageNum");
        int pageSize = params.getIntValue("pageSize");
        PageInfo<Permission> page = permissionService.page(pageNum, pageSize, params);
        return ResultData.succ(page);
    }
}
