package com.jingqueyimu.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.jingqueyimu.constant.CacheConstant;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.context.AdminContext;
import com.jingqueyimu.context.UserContext;
import com.jingqueyimu.model.bean.CurrAdmin;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.component.RedisService;
import com.jingqueyimu.util.SysUtil;

/**
 * 权限过滤器
 * 过滤器可以过滤几乎所有请求
 *
 * @author zhuangyilian
 */
@Component
public class AuthFilter implements Filter {
    
    /**
     * 排除过滤的控台地址
     */
    private static final List<String> EXCLUDE_CONSOLE_PATH = new ArrayList<>();
    
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserContext userContext;
    @Autowired
    private AdminContext adminContext;
    @Value("${spring.jackson.date-format}")
    private String jacksonDateFormat;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化Filter时调用
        if (EXCLUDE_CONSOLE_PATH.isEmpty()) {
            EXCLUDE_CONSOLE_PATH.add("/console");
            EXCLUDE_CONSOLE_PATH.add("/console/");
            EXCLUDE_CONSOLE_PATH.add("/console/msg.v");
            EXCLUDE_CONSOLE_PATH.add("/console/login.v");
            EXCLUDE_CONSOLE_PATH.add("/console/admin/login");
            EXCLUDE_CONSOLE_PATH.add("/console/admin/logout");
        }
    }
    
    @Override
    public void destroy() {
        // 销毁Filter时调用
        EXCLUDE_CONSOLE_PATH.clear();
    }

    /**
     * 过滤处理
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession();
        String servletPath = httpServletRequest.getServletPath();
        // 需要登录权限的地址
        if (servletPath.startsWith("/api/")) {
            // 未登录
            if (userContext.getCurrUser() == null) {
                // 是否ajax请求
                boolean isAjax = SysUtil.isAjaxRequest(httpServletRequest);
                // 是否json格式数据
                boolean isJson = SysUtil.isJsonData(httpServletRequest);
                if (isAjax || isJson) {
                    returnResultData(httpServletResponse, ResultData.fail(StatusCode.ERR_AUTH_NO_LOGIN, "您还未登录"));
                } else {
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login.v");
                }
                return;
            }
            chain.doFilter(request, response);
            return;
        }
        // 控台地址
        if (servletPath.startsWith("/console/")) {
            if (EXCLUDE_CONSOLE_PATH.contains(servletPath)) {
                chain.doFilter(request, response);
                return;
            }
            CurrAdmin currAdmin = adminContext.getCurrAdmin();
            if (currAdmin == null) {
                // 是否ajax请求
                boolean isAjax = SysUtil.isAjaxRequest(httpServletRequest);
                // 是否json格式数据
                boolean isJson = SysUtil.isJsonData(httpServletRequest);
                if (isAjax || isJson) {
                    returnResultData(httpServletResponse, ResultData.fail(StatusCode.ERR_AUTH_NO_LOGIN, "您还未登录"));
                } else {
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/console/login.v");
                }
                return;
            }
            // 校验管理员权限
            boolean hasPermission = checkAdminPermission(session, currAdmin, servletPath);
            if (!hasPermission) {
                // 是否ajax请求
                boolean isAjax = SysUtil.isAjaxRequest(httpServletRequest);
                // 是否json格式数据
                boolean isJson = SysUtil.isJsonData(httpServletRequest);
                if (isAjax || isJson) {
                    returnResultData(httpServletResponse, ResultData.fail(StatusCode.ERR_AUTH, "无权限访问"));
                } else {
                    httpServletResponse.sendRedirect("/console/msg.v?msg=" + URLEncoder.encode("无权限访问", "UTF-8"));
                }
                return;
            }
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(request, response);
    }
    
    /**
     * 返回JSON格式的结果数据
     *
     * @param response
     * @param resData
     * @throws IOException
     */
    private void returnResultData(HttpServletResponse response, ResultData resData) throws IOException {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(Date.class, new SimpleDateFormatSerializer(jacksonDateFormat));
        String resDateStr = JSONObject.toJSONString(resData, serializeConfig, SerializerFeature.WriteMapNullValue);
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        OutputStream out = response.getOutputStream();
        out.write(resDateStr.getBytes("UTF-8"));
        out.flush();
    }
    
    /**
     * 校验管理员权限
     *
     * @param session
     * @param admin
     * @param currPath
     */
    @SuppressWarnings("unchecked")
    private boolean checkAdminPermission(HttpSession session, CurrAdmin admin, String currPath) {
        Object adminPermissionPathsObj = redisService.get(CacheConstant.ADMIN_PERMISSION + admin.getId());
        if (adminPermissionPathsObj == null) {
            return false;
        }
        List<String> adminPermissionPaths = (List<String>) adminPermissionPathsObj;
        if (!adminPermissionPaths.contains(currPath)) {
            return false;
        }
        return true;
    }
}
