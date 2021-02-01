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
import com.jingqueyimu.model.Admin;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.AdminService;

/**
 * 管理员控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/console/admin")
public class ConsoleAdminController extends BaseController {
    
    @Autowired
    private AdminService adminService;
    
    /**
     * 登录
     *
     * @param params
     * @return
     */
    @PostMapping(value="/login")
    public ResultData login(@RequestBody JSONObject params) {
        // 记住账号密码
        boolean rememberMe = params.getBooleanValue("rememberMe");
        Admin admin = adminService.login(params);
        // 保存当前登录管理员
        adminContext.saveCurrAdmin(admin, rememberMe);
        return ResultData.succ();
    }
    
    /**
     * 登出
     *
     * @return
     */
    @RequestMapping("/logout")
    public ResultData logout() {
        adminContext.clearCurrAdmin();
        return ResultData.succ();
    }
    
    /**
     * 分页查询管理员
     *
     * @param params
     * @return
     */
    @Perm(group="admin", name="分页查询管理员", description="管理员管理-管理员列表-分页查询管理员")
    @PostMapping("/page")
    public ResultData page(@RequestBody JSONObject params) {
        int pageNum = params.getIntValue("pageNum");
        int pageSize = params.getIntValue("pageSize");
        PageInfo<Admin> page = adminService.page(pageNum, pageSize, params);
        return ResultData.succ(page);
    }
    
    /**
     * 管理员详情
     *
     * @param params
     * @return
     */
    @Perm(group="admin", name="管理员详情", description="管理员管理-管理员列表-管理员详情")
    @PostMapping("/get")
    public ResultData detail(@RequestBody JSONObject params) {
        long id = params.getLongValue("id");
        Admin admin = adminService.getById(id);
        return ResultData.succ(admin);
    }
    
    /**
     * 添加管理员
     *
     * @param params
     * @return
     */
    @Perm(group="admin", name="添加管理员", description="管理员管理-管理员列表-添加管理员")
    @PostMapping("/save")
    public ResultData save(@RequestBody JSONObject params) {
        adminService.createAdmin(params);
        return ResultData.succ();
    }
    
    /**
     * 更新管理员信息
     *
     * @param params
     * @return
     */
    @Perm(group="admin", name="更新管理员信息", description="管理员管理-管理员列表-更新管理员信息")
    @PostMapping("/update_admin_info")
    public ResultData updateAdminInfo(@RequestBody JSONObject params) {
        long id = params.getLongValue("id");
        Admin admin = adminService.updateAdminInfo(id, params);
        return ResultData.succ(admin);
    }
    
    /**
     * 更新管理员头像
     *
     * @param params
     * @return
     */
    @Perm(group="admin", name="更新管理员头像", description="管理员管理-管理员列表-更新管理员头像")
    @PostMapping("/update_admin_avatar")
    public ResultData updateAdminAvatar(@RequestBody JSONObject params) {
        long adminId = adminContext.getCurrAdmin().getId();
        String avatar = params.getString("avatar");
        Admin admin = adminService.updateAdminAvatar(adminId, avatar);
        adminContext.updateCurrAdmin(admin);
        return ResultData.succ();
    }
    
    /**
     * 更新管理员密码
     *
     * @param params
     * @return
     */
    @Perm(group="admin", name="更新管理员密码", description="管理员管理-管理员列表-更新管理员密码")
    @PostMapping("/update_admin_password")
    public ResultData updateAdminPassword(@RequestBody JSONObject params) {
        long adminId = adminContext.getCurrAdmin().getId();
        adminService.updateAdminPassword(adminId, params);
        return ResultData.succ();
    }
}
