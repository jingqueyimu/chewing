package com.jingqueyimu.context;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.model.Admin;
import com.jingqueyimu.model.bean.CurrAdmin;
import com.jingqueyimu.service.component.RedisService;
import com.jingqueyimu.util.SysUtil;

import cn.hutool.core.lang.UUID;

/**
 * 管理员上下文
 *
 * @author zhuangyilian
 */
@Component
public class AdminContext {
    
    /**
     * 登录标识cookie名称
     */
    public static final String COOKIE_SESSION_ID = "chewing_admin";
    /**
     * 登录管理员会话缓存key
     */
    public static final String CACHE_SESSION_ADMIN = "session_admin_";
    /**
     * 默认会话有效期(30分钟)
     */
    private static final int DEFAULT_SESSION_VALID_PERIOD = 60 * 30;
    /**
     * 记住登录状态有效期(7天)
     */
    public static final int REMEMBER_ME_VALID_PERIOD = 60 * 60 * 24 * 7;
    
    @Autowired
    private RedisService redisService;
    
    /**
     * 获取当前登录管理员
     *
     * @return
     */
    public CurrAdmin getCurrAdmin() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie cookie = SysUtil.getCookie(request, COOKIE_SESSION_ID);
        if (cookie == null) {
            return null;
        }
        String sessionId = cookie.getValue();
        Object adminObj = redisService.get(CACHE_SESSION_ADMIN + sessionId);
        if (adminObj != null) {
            CurrAdmin currAdmin = (CurrAdmin) adminObj;
            // cookie生命周期为session时(没有勾选"记住我"),刷新redis中的登录信息有效期
            if (Boolean.FALSE.equals(currAdmin.getRememberMe())) {
                redisService.set(CACHE_SESSION_ADMIN + sessionId, adminObj, DEFAULT_SESSION_VALID_PERIOD);
            }
            return currAdmin;
        }
        return null;
    }
    
    /**
     * 保存当前登录管理员
     *
     * @param admin
     * @param rememberMe
     * @return
     */
    public CurrAdmin saveCurrAdmin(Admin admin, boolean rememberMe) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String sessionId = UUID.randomUUID().toString();
        CurrAdmin currAdmin = new CurrAdmin();
        BeanUtils.copyProperties(admin, currAdmin);
        currAdmin.setRememberMe(rememberMe);
        if (rememberMe) {
            redisService.set(CACHE_SESSION_ADMIN + sessionId, currAdmin, REMEMBER_ME_VALID_PERIOD);
            Cookie cookie = new Cookie(COOKIE_SESSION_ID, sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(REMEMBER_ME_VALID_PERIOD);
            response.addCookie(cookie);
        } else {
            redisService.set(CACHE_SESSION_ADMIN + sessionId, currAdmin, DEFAULT_SESSION_VALID_PERIOD);
            Cookie cookie = new Cookie(COOKIE_SESSION_ID, sessionId);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return currAdmin;
    }
    
    /**
     * 更新当前登录管理员
     *
     * @param admin
     * @return
     */
    public CurrAdmin updateCurrAdmin(Admin admin) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie cookie = SysUtil.getCookie(request, COOKIE_SESSION_ID);
        if (cookie == null) {
            throw new AppException(StatusCode.ERR_AUTH_NO_LOGIN, "管理员未登录");
        }
        String sessionId = cookie.getValue();
        boolean hasKey = redisService.hasKey(CACHE_SESSION_ADMIN + sessionId);
        if (!hasKey) {
            throw new AppException(StatusCode.ERR_AUTH_NO_LOGIN, "管理员未登录");
        }
        CurrAdmin currAdmin = new CurrAdmin();
        BeanUtils.copyProperties(admin, currAdmin);
        redisService.set(CACHE_SESSION_ADMIN + sessionId, currAdmin);
        return currAdmin;
    }
    
    /**
     * 清除当前登录管理员
     */
    public void clearCurrAdmin() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie cookie = SysUtil.getCookie(request, COOKIE_SESSION_ID);
        if (cookie == null) {
            return;
        }
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String sessionId = cookie.getValue();
        redisService.delete(CACHE_SESSION_ADMIN + sessionId);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
