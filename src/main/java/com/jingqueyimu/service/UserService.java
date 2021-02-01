package com.jingqueyimu.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.constant.BizConstant;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.handler.login.LoginHandler;
import com.jingqueyimu.handler.register.RegisterHandler;
import com.jingqueyimu.mapper.UserMapper;
import com.jingqueyimu.model.User;
import com.jingqueyimu.service.component.MailService;
import com.jingqueyimu.service.component.SmsService;
import com.jingqueyimu.util.SysUtil;

/**
 * 用户服务
 *
 * @author zhuangyilian
 */
@Service
public class UserService extends BaseService<User> {
    
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailService mailService;
    @Autowired
    private SmsService smsService;
    
    /**
     * 注册
     *
     * @param params
     * @return
     */
    public User register(JSONObject params) {
        SysUtil.checkParam(params, "registerType", "请选择注册方式");
        int registerType = params.getInteger("registerType");
        RegisterHandler handler = RegisterHandler.getHandler(registerType);
        if (handler == null) {
            throw new AppException(StatusCode.ERR_BIZ, "暂不支持该注册方式");
        }
        return handler.handle(params);
    }
    
    /**
     * 创建注册用户
     *
     * @param username
     * @param password
     * @param mobile
     * @param email
     * @param registerType
     * @return
     */
    public User createRegisterUser(String username, String password, String mobile, String email, int registerType) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(SysUtil.md5Crypt(password));
        user.setAvatar(BizConstant.DEFAULT_AVATAR);
        user.setMobile(mobile);
        user.setEmail(email);
        user.setRegisterType(registerType);
        user.setRegisterTime(new Date());
        user.setVipFlag(false);
        return save(user);
    }
    
    /**
     * 登录
     *
     * @param params
     * @return
     */
    public User login(JSONObject params) {
        SysUtil.checkParam(params, "loginType", "请选择登录方式");
        int loginType = params.getInteger("loginType");
        LoginHandler handler = LoginHandler.getHandler(loginType);
        if (handler == null) {
            throw new AppException(StatusCode.ERR_BIZ, "暂不支持该登录方式");
        }
        return handler.handle(params);
    }
    
    /**
     * 根据账号和密码查询用户
     *
     * @param account
     * @param password
     * @return
     */
    public User getByAccountAndPassword(String account, String password) {
        JSONObject params = new JSONObject();
        params.put("account", account);
        params.put("password", SysUtil.md5Crypt(password));
        return userMapper.getByAccountAndPassword(params);
    }
    
    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    public User getByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        return userMapper.selectOne(user);
    }
    
    /**
     * 根据手机号查询用户
     *
     * @param mobile
     * @return
     */
    public User getByMobile(String mobile) {
        User user = new User();
        user.setMobile(mobile);
        return userMapper.selectOne(user);
    }
    
    /**
     * 根据邮箱查询用户
     *
     * @param email
     * @return
     */
    public User getByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return userMapper.selectOne(user);
    }

    /**
     * 更新用户信息
     *
     * @param userId
     * @param params
     */
    public User updateUserInfo(long userId, JSONObject params) {
        String username = params.getString("username");
        String avatar = params.getString("avatar");
        String realName = params.getString("realName");
        Date birthday = params.getDate("birthday");
        
        User user = getById(userId);
        // 用户名
        if (StringUtils.isNotBlank(username)) {
            if (!SysUtil.checkUsername(username)) {
                throw new AppException(StatusCode.ERR_PARAM, "用户名必须为1-16个字符");
            }
            User checkUser = getByUsername(username);
            if (checkUser != null && userId != checkUser.getId()) {
                throw new AppException(StatusCode.ERR_BIZ, "用户名已存在");
            }
            user.setUsername(username);
        }
        // 头像
        if (StringUtils.isNotBlank(avatar)) {
            user.setAvatar(avatar);
        }
        // 姓名
        if (StringUtils.isNotBlank(realName)) {
            if (realName.length() < 1 || realName.length() > 32) {
                throw new AppException(StatusCode.ERR_PARAM, "姓名必须为1-32个字符");
            }
            user.setRealName(realName);
        }
        // 生日
        if (birthday != null) {
            if (birthday.after(new Date())) {
                throw new AppException(StatusCode.ERR_PARAM, "出生日期不能大于当前时间");
            }
            user.setBirthday(birthday);
        }
        return updateSelective(user);
    }
    
    /**
     * 更新用户密码
     *
     * @param userId
     * @param params
     */
    public User updateUserPassword(long userId, JSONObject params) {
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
        User user = getById(userId);
        if (!SysUtil.md5Crypt(oldPassword).equals(user.getPassword())) {
            throw new AppException(StatusCode.ERR_PARAM, "原密码错误");
        }
        user.setPassword(SysUtil.md5Crypt(newPassword));
        return updateSelective(user);
    }
    
    /**
     * 更新用户邮箱
     *
     * @param userId
     * @param params
     */
    public User updateUserEmail(long userId, JSONObject params) {
        SysUtil.checkParam(params, "email", "请输入邮箱");
        SysUtil.checkParam(params, "emailCode", "请输入邮箱验证码");
        String email = params.getString("email");
        String emailCode = params.getString("emailCode");
        
        if (email.length() > 32 || !SysUtil.checkEmail(email)) {
            throw new AppException(StatusCode.ERR_PARAM, "邮箱格式不正确");
        }
        User user = getById(userId);
        if (email.equals(user.getEmail())) {
            throw new AppException(StatusCode.ERR_BIZ, "新邮箱不能与原邮箱相同");
        }
        User checkUser = getByEmail(email);
        if (checkUser != null && checkUser.getId() != userId) {
            throw new AppException(StatusCode.ERR_BIZ, "该邮箱已绑定其他账号");
        }
        // 校验邮箱验证码
        mailService.checkEmailCode(email, emailCode);
        
        user.setEmail(email);
        return updateSelective(user);
    }
    
    /**
     * 更新用户手机号
     *
     * @param userId
     * @param params
     */
    public User updateUserMobile(long userId, JSONObject params) {
        SysUtil.checkParam(params, "mobile", "请输入手机号");
        SysUtil.checkParam(params, "smsCode", "请输入短信验证码");
        String mobile = params.getString("mobile");
        String smsCode = params.getString("smsCode");
        
        if (!SysUtil.checkMobile(mobile)) {
            throw new AppException(StatusCode.ERR_PARAM, "手机号格式不正确");
        }
        User user = getById(userId);
        if (mobile.equals(user.getMobile())) {
            throw new AppException(StatusCode.ERR_BIZ, "新手机号不能与原手机号相同");
        }
        User checkUser = getByMobile(mobile);
        if (checkUser != null && checkUser.getId() != userId) {
            throw new AppException(StatusCode.ERR_BIZ, "该手机号已绑定其他账号");
        }
        // 校验短信验证码
        smsService.checkSmsCode(mobile, smsCode);
        
        user.setMobile(mobile);
        return updateSelective(user);
    }
}
