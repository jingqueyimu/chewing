package com.jingqueyimu.controller.view;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jingqueyimu.controller.BaseController;
import com.jingqueyimu.model.Feedback;
import com.jingqueyimu.model.dict.FeedbackType;
import com.jingqueyimu.service.FeedbackService;
import com.jingqueyimu.service.NoticeService;

/**
 * 页面控制器
 *
 * @author zhuangyilian
 */
@Controller
@RequestMapping("/")
public class ViewController extends BaseController {
    
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private NoticeService noticeService;
    
    /**
     * 首页
     *
     * @return
     */
    @GetMapping({"", "/", "index.v"})
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("client/index");
        return mv;
    }
    
    /**
     * 登录页面
     *
     * @return
     */
    @GetMapping("/login.v")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView();
        if (userContext.getCurrUser() != null) {
            mv.setViewName("redirect:index.v");
            return mv;
        }
        mv.setViewName("client/login");
        return mv;
    }
    
    /**
     * 注册页面
     *
     * @return
     */
    @GetMapping("/register.v")
    public ModelAndView register() {
        ModelAndView mv = new ModelAndView();
        if (userContext.getCurrUser() != null) {
            mv.setViewName("redirect:index.v");
            return mv;
        }
        mv.setViewName("client/register");
        return mv;
    }
    
    /**
     * 公告
     *
     * @param request
     * @return
     */
    @GetMapping("/notice/index.v")
    public ModelAndView notice(HttpServletRequest request) {
        int pageNum = NumberUtils.toInt(request.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 10);
        JSONObject params = new JSONObject();
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("client/notice-index");
        mv.addObject("pageInfo", noticeService.page(pageNum, pageSize, params));
        return mv;
    }
    
    /**
     * 公告列表
     *
     * @param request
     * @return
     */
    @GetMapping("/notice/list.v")
    public ModelAndView noticeList(HttpServletRequest request) {
        int pageNum = NumberUtils.toInt(request.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 10);
        JSONObject params = new JSONObject();
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("client/notice-list");
        mv.addObject("pageInfo", noticeService.page(pageNum, pageSize, params));
        return mv;
    }
    
    /**
     * 公告详情
     *
     * @param request
     * @return
     */
    @GetMapping("/notice/detail.v")
    public ModelAndView noticeDetail(HttpServletRequest request) {
        long noticeId = NumberUtils.toLong(request.getParameter("id"), 0L);
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("client/notice-detail");
        mv.addObject("detail", noticeService.viewDetail(noticeId));
        mv.addObject("prev", noticeService.prevNotice(noticeId));
        mv.addObject("next", noticeService.nextNotice(noticeId));
        return mv;
    }
    
    /**
     * 个人中心
     *
     * @return
     */
    @GetMapping("/api/user/center.v")
    public ModelAndView userCenter() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("client/user-center");
        return mv;
    }
    
    /**
     * 账号设置
     *
     * @param request
     * @return
     */
    @GetMapping("/api/user/setting.v")
    public ModelAndView userSetting(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("client/user-setting");
        return mv;
    }
    
    /**
     * 意见反馈
     *
     * @param request
     * @return
     */
    @GetMapping("/api/feedback/index.v")
    public ModelAndView feedback(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("client/feedback-index");
        mv.addObject("feedbackTypes", FeedbackType.values());
        return mv;
    }
    
    /**
     * 我的反馈列表
     *
     * @param request
     * @return
     */
    @GetMapping("/api/feedback/list.v")
    public ModelAndView feedbackList(HttpServletRequest request) {
        int pageNum = NumberUtils.toInt(request.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 10);
        JSONObject params = new JSONObject();
        params.put("userId", userContext.getCurrUser().getId());
        PageInfo<Feedback> pageInfo = feedbackService.page(pageNum, pageSize, params);
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("client/feedback-list");
        mv.addObject("pageInfo", pageInfo);
        return mv;
    }
}
