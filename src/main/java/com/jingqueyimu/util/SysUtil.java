package com.jingqueyimu.util;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;

/**
 * 系统通用工具类
 *
 * @author zhuangyilian
 */
public class SysUtil {
    
    private static final String MD5_SALT = "$1$chewing";
    
    /**
     * Md5加密
     *
     * @param source
     * @return
     */
    public static String md5Crypt(String source) {
        return Md5Crypt.md5Crypt(source.getBytes(), MD5_SALT);
    }
    
    /**
     * 校验参数
     *
     * @param params
     * @param keys
     */
    public static void checkParams(JSONObject params, String... keys) {
        for (String key : keys) {
            if (!params.containsKey(key) || "".equals(params.getString(key).trim())) {
                throw new AppException(StatusCode.ERR_PARAM, key + "不能为空");
            }
        }
    }
    
    /**
     * 校验参数
     *
     * @param params
     * @param key
     * @param msg
     */
    public static void checkParam(JSONObject params, String key, String msg) {
        if (!params.containsKey(key) || "".equals(params.getString(key).trim())) {
            throw new AppException(StatusCode.ERR_PARAM, msg);
        }
    }
    
    /**
     * 校验数据
     *
     * @param objs
     */
    public static void checkData(Object... objs) {
        for (Object obj : objs) {
            if (obj == null) {
                throw new RuntimeException("存在空数据");
            }
            if (obj instanceof String && obj.toString().isEmpty()) {
                throw new RuntimeException("存在空数据");
            }
        }
    }
    
    /**
     * 获取应用上下文
     *
     * @param session
     * @return
     */
    public static ApplicationContext getApplicationContext(HttpSession session) {
        // 获取 servlet上下文
        ServletContext servletContext = session.getServletContext();
        // 获取spring容器
        ApplicationContext applicationContext = (ApplicationContext) WebApplicationContextUtils.getWebApplicationContext(servletContext);
        if (applicationContext == null) {
            throw new RuntimeException("系统异常");
        }
        return applicationContext;
    }
    
    /**
     * 是否ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return !StringUtils.isEmpty(header) && "XMLHttpRequest".equals(header);
    }
    
    /**
     * 是否json格式数据
     *
     * @param request
     * @return
     */
    public static boolean isJsonData(HttpServletRequest request) {
        String header = request.getHeader("Content-Type");
        return !StringUtils.isEmpty(header) && header.contains("application/json");
    }
    
    /**
     * 校验用户名(任意字符.1-16位)
     *
     * @param username
     * @return
     */
    public static boolean checkUsername(String username) {
        return username.matches("^.{1,16}$");
    }
    
    /**
     * 校验用户名(字母,数字,下划线,中文.1-16位)
     *
     * @param username
     * @return
     */
    public static boolean checkUsername2(String username) {
        return username.matches("^[a-zA-Z0-9_\u4e00-\u9fa5]{1,16}$");
    }
    
    /**
     * 校验管理员账户名(字母,数字,下划线.1-16位)
     *
     * @param adminName
     * @return
     */
    public static boolean checkAdminName(String adminName) {
        return adminName.matches("^[a-zA-Z0-9_]{1,16}$");
    }
    
    /**
     * 校验邮箱格式
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        return email.matches("^[a-zA-Z0-9_-]+@[a-z0-9]+(\\.[a-z]+)+$");
    }
    
    /**
     * 校验手机号格式
     *
     * @param mobile
     * @return
     */
    public static boolean checkMobile(String mobile) {
        return mobile.matches("^1[3456789]\\d{9}$");
    }
    
    /**
     * 校验强密码格式(包含数字,字母,字符中的两种.长度6-16)
     *
     * @param password
     * @return
     */
    public static boolean checkStrongPassword(String password) {
        return password.matches("^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{6,16}$");
    }
    
    /**
     * 校验弱密码格式(任意字符.长度6-16)
     *
     * @param password
     * @return
     */
    public static boolean checkWeakPassword(String password) {
        return password.matches("^.{6,16}$");
    }
    
    /**
     * 将请求参数转为JSON
     *
     * @param request
     * @return
     */
    public static JSONObject requestParamsToJson(HttpServletRequest request) {
        JSONObject params = new JSONObject();
        Map<String, String[]> paramMap = request.getParameterMap();
        String[] values = null;
        for (Entry<String, String[]> entry : paramMap.entrySet()) {
            values = entry.getValue();
            if (values == null) {
                continue;
            } else if (values.length == 0) {
                params.put(entry.getKey(), "");
            } else if (values.length == 1) {
                params.put(entry.getKey(), values[0]);
            } else {
                params.put(entry.getKey(), values);
            }
        }
        return params;
    }
    
    /**
     * 获取域名
     *
     * @param request
     * @return
     */
    public static String getDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString(); 
    }
    
    /**
     * 获取服务地址(带部署环境上下文)
     *
     * @param request
     * @return
     */
    public static String getServerUrl(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length())
                .append(request.getContextPath()).append("/").toString();
    }
    
    /**
     * 获取cookie
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie;
            }
        }
        return null;
    }
}
