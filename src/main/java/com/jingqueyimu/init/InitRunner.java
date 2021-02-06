package com.jingqueyimu.init;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.annotation.Perm;
import com.jingqueyimu.config.MyConfig;
import com.jingqueyimu.constant.BizConstant;
import com.jingqueyimu.constant.CacheConstant;
import com.jingqueyimu.model.Admin;
import com.jingqueyimu.model.Permission;
import com.jingqueyimu.service.AdminPermissionService;
import com.jingqueyimu.service.AdminService;
import com.jingqueyimu.service.DbInitService;
import com.jingqueyimu.service.PermissionService;
import com.jingqueyimu.service.component.RedisService;
import com.jingqueyimu.util.SysUtil;
import com.jingqueyimu.util.ResourceUtil;

/**
 * 实现CommandLineRunner的Component会在所有Bean初始化之后,SpringApplication.run()之前执行
 * 非常适合应用程序启动时进行一些数据初始化工作
 *
 * @author zhuangyilian
 */
@Component
// 执行顺序
@Order(1)
public class InitRunner implements CommandLineRunner {
    
    public final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private MyConfig config;
    @Autowired
    private AdminService adminService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private AdminPermissionService adminPermissionService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ResourceUtil resourceUtil;
    @Autowired
    private DbInitService dbInitService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("InitRunner start to initialize...");
        // 初始化数据库
        initDB();
        // 初始化超级管理员
        initSuperAdmin();
        // 初始化超级管理员权限
        initSuperAdminPermission();
        // 加载管理员权限
        loadAdminPermession();
    }
    
    /**
     * 初始化数据库
     */
    private void initDB() throws Exception {
        JSONArray dbInitDataArr = resourceUtil.loadJsonArrConfigs("classpath:config/dbinit.json");
        JSONObject dbInitData = null;
        for (Object dbInitObj : dbInitDataArr) {
            dbInitData = (JSONObject)dbInitObj;
            try {
                dbInitService.doDbInit(dbInitData);
            } catch (Exception e) {
                log.error("初始化数据失败：{}", dbInitData, e);
            }
        }
    }
    
    /**
     * 初始化管理员
     */
    private void initSuperAdmin() {
        Admin superAdmin = adminService.getById(BizConstant.SUPER_ADMIN_ID);
        if (superAdmin != null) {
            return;
        }
        superAdmin = new Admin();
        superAdmin.setId(BizConstant.SUPER_ADMIN_ID);
        superAdmin.setName(config.getSuperAdminName());
        superAdmin.setPassword(SysUtil.md5Crypt(config.getSuperAdminPwd()));
        superAdmin.setAvatar(BizConstant.DEFAULT_AVATAR);
        superAdmin.setRealName("超级管理员");
        superAdmin.setLockFlag(false);
        adminService.saveWithId(superAdmin);
    }
    
    /**
     * 初始化超级管理员权限
     *
     * @param superAdmin
     * @throws Exception
     */
    private void initSuperAdminPermission() throws Exception {
        // 需要控制权限的方法
        Map<String, Map<String, Object>> permissionMethods = resourceUtil.getTargetAnnotationMethodInfo(
                "classpath*:com/jingqueyimu/controller/**/*.class", Perm.class);
        String path = null;
        String groupCode = null;
        String name = null;
        String description = null;
        Permission permission = null;
        for (Entry<String, Map<String, Object>> entry : permissionMethods.entrySet()) {
            path = entry.getKey();
            groupCode = entry.getValue().get("group").toString();
            name = entry.getValue().get("name").toString();
            description = entry.getValue().get("description").toString();
            if (StringUtils.isBlank(path)) {
                continue;
            }
            // 保存或更新权限
            permission = permissionService.saveOrUpdateByPath(path, groupCode, name, description);
            // 保存超级管理员权限
            adminPermissionService.save(BizConstant.SUPER_ADMIN_ID, permission);
        }
    }
    
    /**
     * 加载管理员权限
     */
    private void loadAdminPermession() {
        List<Admin> admins = adminService.listUnlock();
        if (admins == null || admins.isEmpty()) {
            return;
        }
        List<String> adminPermissionPaths = null;
        for (Admin admin : admins) {
            adminPermissionPaths = adminPermissionService.listPathByAdminId(admin.getId());
            // 将管理员权限放入缓存
            redisService.set(CacheConstant.ADMIN_PERMISSION + admin.getId(), adminPermissionPaths);
        }
    }
}
