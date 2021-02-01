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
import com.jingqueyimu.model.User;
import com.jingqueyimu.model.bean.CurrUser;
import com.jingqueyimu.service.component.RedisService;
import com.jingqueyimu.util.SysUtil;

import cn.hutool.core.lang.UUID;

/**
 * 用户上下文
 *
 * @author zhuangyilian
 */
@Component
public class UserContext {
    
    /**
     * 登录标识cookie名称
     */
    public static final String COOKIE_SESSION_ID = "chewing";
    /**
     * 登录用户会话缓存key
     */
    public static final String CACHE_SESSION_USER = "session_user_";
    /**
     * 默认会话有效期(30分钟)
     */
    private static final int DEFAULT_SESSION_VALID_PERIOD = 60 * 30;
    /**
     * 记住登录状态有效期(7天)
     */
    public static final int REMEMBER_ME_VALID_PERIOD = 60 * 60 * 24 * 7;
    private static final Object CurrUser = null;
    
    @Autowired
    private RedisService redisService;
    
    /**
     * 获取当前登录用户
     *
     * @return
     */
    public CurrUser getCurrUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie cookie = SysUtil.getCookie(request, COOKIE_SESSION_ID);
        if (cookie == null) {
            return null;
        }
        String sessionId = cookie.getValue();
        Object userObj = redisService.get(CACHE_SESSION_USER + sessionId);
        if (userObj != null) {
            CurrUser currUser = (CurrUser) userObj;
            // cookie生命周期为session时(没有勾选"记住我"),刷新redis中的登录信息有效期
            if (Boolean.FALSE.equals(currUser.getRememberMe())) {
                redisService.expire(CACHE_SESSION_USER + sessionId, DEFAULT_SESSION_VALID_PERIOD);
            }
            return currUser;
        }
        return null;
    }
    
    /**
     * 保存当前登录用户
     *
     * @param user
     * @param rememberMe
     * @return
     */
    public CurrUser saveCurrUser(User user, boolean rememberMe) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String sessionId = UUID.randomUUID().toString();
        CurrUser currUser = new CurrUser();
        BeanUtils.copyProperties(user, currUser);
        currUser.setRememberMe(rememberMe);
        if (rememberMe) {
            redisService.set(CACHE_SESSION_USER + sessionId, currUser, REMEMBER_ME_VALID_PERIOD);
            Cookie cookie = new Cookie(COOKIE_SESSION_ID, sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(REMEMBER_ME_VALID_PERIOD);
            response.addCookie(cookie);
        } else {
            redisService.set(CACHE_SESSION_USER + sessionId, currUser, DEFAULT_SESSION_VALID_PERIOD);
            Cookie cookie = new Cookie(COOKIE_SESSION_ID, sessionId);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return currUser;
    }
    
    /**
     * 更新当前登录用户
     *
     * @param user
     * @return
     */
    public CurrUser updateCurrUser(User user) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie cookie = SysUtil.getCookie(request, COOKIE_SESSION_ID);
        if (cookie == null) {
            throw new AppException(StatusCode.ERR_AUTH_NO_LOGIN, "用户未登录");
        }
        String sessionId = cookie.getValue();
        boolean hasKey = redisService.hasKey(CACHE_SESSION_USER + sessionId);
        if (!hasKey) {
            throw new AppException(StatusCode.ERR_AUTH_NO_LOGIN, "用户未登录");
        }
        CurrUser currUser = new CurrUser();
        BeanUtils.copyProperties(user, currUser);
        redisService.set(CACHE_SESSION_USER + sessionId, currUser);
        return currUser;
    }
    
    /**
     * 清除当前登录用户
     */
    public void clearCurrUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie cookie = SysUtil.getCookie(request, COOKIE_SESSION_ID);
        if (cookie == null) {
            return;
        }
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String sessionId = cookie.getValue();
        redisService.delete(CACHE_SESSION_USER + sessionId);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
