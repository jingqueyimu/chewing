package com.jingqueyimu.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.constant.BizConstant;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.mapper.AdminMapper;
import com.jingqueyimu.model.Admin;
import com.jingqueyimu.service.component.CaptchaService;
import com.jingqueyimu.service.component.MailService;
import com.jingqueyimu.util.SysUtil;

/**
 * 管理员服务
 *
 * @author zhuangyilian
 */
@Service
public class AdminService extends BaseService<Admin> {
    
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private MailService mailService;
    @Autowired
    protected CaptchaService captchaService;
    
    /**
     * 登录
     *
     * @param params
     * @return
     */
    public Admin login(JSONObject params) {
        SysUtil.checkParam(params, "name", "请输入用户名");
        SysUtil.checkParam(params, "password", "请输入密码");
        SysUtil.checkParam(params, "captchaToken", "图形验证码Token不能为空");
        SysUtil.checkParam(params, "captchaCode", "请输入图形验证码");
        
        String name = params.getString("name");
        String password = params.getString("password");
        String captchaToken = params.getString("captchaToken");
        String captchaCode = params.getString("captchaCode");
        
        // 校验图形验证码
        captchaService.checkCaptchaCode(captchaToken, captchaCode);
        // 查询用户
        Admin admin = getByNameAndPassword(name, password);
        if (admin == null) {
            throw new AppException(StatusCode.ERR_AUTH, "用户名或密码错误");
        }
        if (admin.getLockFlag()) {
            throw new AppException(StatusCode.ERR_AUTH, "账户已被锁定");
        }
        return admin;
    }
    
    /**
     * 创建管理员
     *
     * @param params
     */
    public Admin createAdmin(JSONObject params) {
        SysUtil.checkParam(params, "name", "请输入账户名");
        SysUtil.checkParam(params, "password", "请输入密码");
        SysUtil.checkParam(params, "realName", "请输入姓名");
        SysUtil.checkParam(params, "mobile", "请输入手机号");
        
        Admin admin = JSONObject.toJavaObject(params, Admin.class);
        // 校验数据格式
        if (!SysUtil.checkAdminName(admin.getName())) {
            throw new AppException(StatusCode.ERR_PARAM, "账户名必须为1-16个字符");
        }
        if (!SysUtil.checkWeakPassword(admin.getPassword())) {
            throw new AppException(StatusCode.ERR_PARAM, "密码长度必须为6-16位");
        }
        if (admin.getRealName().length() < 1 || admin.getRealName().length() > 32) {
            throw new AppException(StatusCode.ERR_PARAM, "姓名必须为1-32个字符");
        }
        if (!SysUtil.checkMobile(admin.getMobile())) {
            throw new AppException(StatusCode.ERR_PARAM, "手机号格式不正确");
        }
        if (StringUtils.isNotBlank(admin.getEmail())) {
            if (admin.getEmail().length() > 32 || !SysUtil.checkEmail(admin.getEmail())) {
                throw new AppException(StatusCode.ERR_PARAM, "邮箱格式不正确");
            }
        }
        // 校验数据唯一性
        Admin checkAdmin = getByName(admin.getName());
        if (checkAdmin != null) {
            throw new AppException(StatusCode.ERR_BIZ, "账户名已存在");
        }
        checkAdmin = getByMobile(admin.getMobile());
        if (checkAdmin != null) {
            throw new AppException(StatusCode.ERR_BIZ, "手机号已存在");
        }
        if (StringUtils.isNotBlank(admin.getEmail())) {
            checkAdmin = getByEmail(admin.getEmail());
            if (checkAdmin != null) {
                throw new AppException(StatusCode.ERR_BIZ, "邮箱已存在");
            }
        }
        
        admin.setPassword(SysUtil.md5Crypt(admin.getPassword()));
        if (StringUtils.isBlank(admin.getAvatar())) {
            admin.setAvatar(BizConstant.DEFAULT_AVATAR);
        }
        if (admin.getLockFlag() == null) {
            admin.setLockFlag(false);
        }
        return save(admin);
    }
    
    /**
     * 查询未锁定的管理员
     *
     * @return
     */
    public List<Admin> listUnlock() {
        JSONObject params = new JSONObject();
        params.put("lockFlag", false);
        return list(params);
    }
    
    /**
     * 查询接收邮件的管理员
     *
     * @return
     */
    public List<Admin> listReceiveEmailAdmin() {
        return adminMapper.listReceiveEmailAdmin();
    }

    /**
     * 根据用户名查询管理员
     *
     * @param name
     * @return
     */
    public Admin getByName(String name) {
        Admin admin = new Admin();
        admin.setName(name);
        return adminMapper.selectOne(admin);
    }
    
    /**
     * 根据手机号查询管理员
     *
     * @param mobile
     * @return
     */
    public Admin getByMobile(String mobile) {
        Admin admin = new Admin();
        admin.setMobile(mobile);
        return adminMapper.selectOne(admin);
    }
    
    /**
     * 根据邮箱查询管理员
     *
     * @param email
     * @return
     */
    public Admin getByEmail(String email) {
        Admin admin = new Admin();
        admin.setEmail(email);
        return adminMapper.selectOne(admin);
    }
    
    /**
     * 根据用户名和密码查询管理员
     *
     * @param name
     * @param password
     * @return
     */
    public Admin getByNameAndPassword(String name, String password) {
        Admin admin = new Admin();
        admin.setName(name);
        admin.setPassword(SysUtil.md5Crypt(password));
        return adminMapper.selectOne(admin);
    }
    
    /**
     * 给管理员发送html邮件
     *
     * @param title
     * @param content
     */
    @Async
    public void sendHtmlEmail(String title, String content) {
        try {
            List<Admin> adminList = listReceiveEmailAdmin();
            for (Admin admin : adminList) {
                mailService.sendHtmlMail(admin.getEmail(), title, content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新管理员信息
     *
     * @param adminId
     * @param params
     * @return
     */
    public Admin updateAdminInfo(long adminId, JSONObject params) {
        String name = params.getString("name");
        String realName = params.getString("realName");
        String mobile = params.getString("mobile");
        String email = params.getString("email");
        Boolean lockFlag = params.getBoolean("lockFlag");
        
        Admin admin = getById(adminId);
        Admin checkAdmin = null;
        if (StringUtils.isNotBlank(name)) {
            if (!SysUtil.checkAdminName(name)) {
                throw new AppException(StatusCode.ERR_PARAM, "账户名必须为1-16个字符");
            }
            checkAdmin = getByName(name);
            if (checkAdmin != null && checkAdmin.getId() != adminId) {
                throw new AppException(StatusCode.ERR_BIZ, "账户名已存在");
            }
            admin.setName(name);
        }
        if (StringUtils.isNotBlank(realName)) {
            if (realName.length() < 1 || realName.length() > 32) {
                throw new AppException(StatusCode.ERR_PARAM, "姓名必须为1-32个字符");
            }
            admin.setRealName(realName);
        }
        if (StringUtils.isNotBlank(mobile)) {
            if (!SysUtil.checkMobile(mobile)) {
                throw new AppException(StatusCode.ERR_PARAM, "手机号格式不正确");
            }
            checkAdmin = getByMobile(admin.getMobile());
            if (checkAdmin != null && checkAdmin.getId() != adminId) {
                throw new AppException(StatusCode.ERR_BIZ, "手机号已存在");
            }
            admin.setMobile(mobile);
        }
        if (StringUtils.isNotBlank(email)) {
            if (email.length() > 32 || !SysUtil.checkEmail(email)) {
                throw new AppException(StatusCode.ERR_PARAM, "邮箱格式不正确");
            }
            checkAdmin = getByEmail(admin.getEmail());
            if (checkAdmin != null && checkAdmin.getId() != adminId) {
                throw new AppException(StatusCode.ERR_BIZ, "邮箱已存在");
            }
            admin.setEmail(email);
        }
        if (lockFlag != null) {
            admin.setLockFlag(lockFlag);
        }
        return updateSelective(admin);
    }
    
    /**
     * 更新管理员头像
     *
     * @param adminId
     * @param avatar
     * @return
     */
    public Admin updateAdminAvatar(long adminId, String avatar) {
        Admin admin = getById(adminId);
        admin.setAvatar(avatar);
        return updateSelective(admin);
    }

    /**
     * 更新管理员密码
     *
     * @param adminId
     * @param params
     * @return
     */
    public Admin updateAdminPassword(long adminId, JSONObject params) {
        SysUtil.checkParam(params, "oldPassword", "请输入原密码");
        SysUtil.checkParam(params, "newPassword", "请输入新密码");
        SysUtil.checkParam(params, "rePassword", "请输确认密码");
        String oldPassword = params.getString("oldPassword");
        String newPassword = params.getString("newPassword");
        String rePassword = params.getString("rePassword");
        if (!newPassword.equals(rePassword)) {
            throw new AppException(StatusCode.ERR_PARAM, "两次输入密码不一致");
        }
        if (!SysUtil.checkWeakPassword(newPassword)) {
            throw new AppException(StatusCode.ERR_PARAM, "密码长度必须为6-16位");
        }
        Admin admin = getById(adminId);
        if (!SysUtil.md5Crypt(oldPassword).equals(admin.getPassword())) {
            throw new AppException(StatusCode.ERR_PARAM, "原密码错误");
        }
        admin.setPassword(SysUtil.md5Crypt(newPassword));
        return updateSelective(admin);
    }
}
