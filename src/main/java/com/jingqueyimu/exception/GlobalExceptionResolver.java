package com.jingqueyimu.exception;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.util.SysUtil;

/**
 * 全局异常处理类(实现SpringMVC的HandlerExceptionResolver)
 * 只能处理请求过程中抛出的异常,不能处理异常处理本身抛出的异常和视图解析过程中抛出的异常
 * 没有进行异常处理时,BasicErrorController会继续处理(BasicErrorController会捕获/error的所有错误,过滤器中的错误会被重定向到/error)
 * 
 * @author zhuangyilian
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver{

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Value("${spring.jackson.date-format}")
    private String jacksonDateFormat;
    
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.error("全局异常处理捕获了异常: {}", ex.getMessage(), ex);
        // 是否ajax请求
        boolean isAjax = SysUtil.isAjaxRequest(request);
        // 是否json格式数据
        boolean isJson = SysUtil.isJsonData(request);
        if (!isAjax && !isJson) {
            ModelAndView mv = new ModelAndView();
            // 控台地址
            if (request.getServletPath().startsWith("/console/")) {
                mv.setViewName("/console/500");
            } else {
                mv.setViewName("/client/500");
            }
            if (ex instanceof AppException) {
                mv.addObject("msg", ex.getMessage());
            }
            return mv;
        }
        if (ex instanceof AppException) {
            AppException appEx = (AppException) ex;
            return buildJsonModelAndView(appEx.getCode(), appEx.getMessage());
        }
        return buildJsonModelAndView(StatusCode.ERR_SYS, ex.getMessage());
    }
    
    /**
     * 组装返回JSON数据的ModelAndView(默认跳转页面)
     *
     * @param code
     * @param msg
     * @return
     */
    private ModelAndView buildJsonModelAndView(int code, String msg) {
        ModelAndView mv = new ModelAndView();
        // 返回JSON数据
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        mv.setView(jsonView);
        mv.addObject("code", code);
        mv.addObject("msg", msg);
        mv.addObject("data", null);
        mv.addObject("time", DateFormatUtils.format(new Date(), jacksonDateFormat));
        return mv;
    }
}
