package com.jingqueyimu.interceptor;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jingqueyimu.annotation.MyAnnotation;
import com.jingqueyimu.constant.CacheConstant;
import com.jingqueyimu.context.AdminContext;
import com.jingqueyimu.context.UserContext;
import com.jingqueyimu.model.bean.CurrAdmin;
import com.jingqueyimu.model.bean.CurrUser;
import com.jingqueyimu.service.component.RedisService;
import com.jingqueyimu.util.SysUtil;

/**
 * 全局拦截器
 * 只能拦截Controller请求
 * 后于过滤器执行,执行顺序:过滤前->拦截前->Action处理->拦截后->过滤后
 *
 * @author zhuangyilian
 */
@Component
public class GlobalInterceptor implements HandlerInterceptor {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private UserContext userContext;
    @Autowired
    private AdminContext adminContext;
    @Autowired
    private RedisService redisService;
    
    /**
     * 在请求处理之前调用(Controller方法调用之前)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug("GlobalInterceptor preHandle exec...");
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取注解
            MyAnnotation typeAnnotation = handlerMethod.getBeanType().getAnnotation(MyAnnotation.class);
            MyAnnotation methodAnnotation = handlerMethod.getMethod().getAnnotation(MyAnnotation.class);
            log.debug("MyAnnotation: {}, {}", typeAnnotation, methodAnnotation);
        }
        return true;
    }
    
    /**
     * 在请求处理之后视图被渲染之前调用(Controller方法调用之后)
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) 
            throws Exception {
        log.debug("GlobalInterceptor postHandle exec...");
        // 添加Model数据
        addModelData(request, modelAndView);
    }
    
    /**
     * 添加Model数据
     *
     * @param request
     * @param modelAndView
     */
    private void addModelData(HttpServletRequest request, ModelAndView modelAndView) {
        if (modelAndView == null) {
            return;
        }
        // 服务地址
        modelAndView.addObject("serverUrl", SysUtil.getServerUrl(request));
        String servletPath = request.getServletPath();
        if (servletPath.startsWith("/console/") || "/console".equals(servletPath)) {
            CurrAdmin currAdmin = adminContext.getCurrAdmin();
            if (currAdmin != null) {
                // 当前登录管理员
                modelAndView.addObject("currAdmin", currAdmin);
                // 管理员权限地址
                Object permissionPathsObj = redisService.get(CacheConstant.ADMIN_PERMISSION + currAdmin.getId());
                modelAndView.addObject("permissionPaths", permissionPathsObj == null ? new ArrayList<>() : permissionPathsObj);
            }
        } else {
            CurrUser currUser = userContext.getCurrUser();
            if (currUser != null) {
                // 当前登录用户
                modelAndView.addObject("currUser", currUser);
            }
        }
    }
    
    /**
     * 在整个请求结束之后调用(DispatcherServlet渲染了视图之后),主要是用于进行资源清理工作
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        log.debug("GlobalInterceptor afterCompletion exec...");
    }
}
