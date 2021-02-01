package com.jingqueyimu.controller.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.annotation.Perm;
import com.jingqueyimu.controller.BaseController;
import com.jingqueyimu.model.bean.CurrAdmin;
import com.jingqueyimu.model.dict.FeedbackStatus;
import com.jingqueyimu.model.dict.FeedbackType;
import com.jingqueyimu.model.dict.PermissionGroup;
import com.jingqueyimu.model.dict.RegisterType;
import com.jingqueyimu.model.dict.ScheduleJobExecuteStatus;
import com.jingqueyimu.model.dict.ScheduleJobStatus;
import com.jingqueyimu.model.vo.PermissionWithAccessVO;
import com.jingqueyimu.service.AdminService;
import com.jingqueyimu.service.FeedbackService;
import com.jingqueyimu.service.PermissionService;
import com.jingqueyimu.service.ScheduleJobService;
import com.jingqueyimu.service.SiteConfigService;
import com.jingqueyimu.service.UserService;

/**
 * 控台页面控制器
 *
 * @author zhuangyilian
 */
@Controller
@RequestMapping("/console")
public class ViewConsoleController extends BaseController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private SiteConfigService siteConfigService;
    @Autowired
    private ScheduleJobService scheduleJobService;
    
    /**
     * 提示页面
     *
     * @return
     */
    @GetMapping("/msg.v")
    public ModelAndView msg(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/console/msg");
        mv.addObject("msg", request.getParameter("msg"));
        return mv;
    }
    
    /**
     * 登录页面
     *
     * @return
     */
    @GetMapping({"", "/", "/login.v"})
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView();
        CurrAdmin currAdmin = adminContext.getCurrAdmin();
        if (currAdmin == null) {
            mv.setViewName("/console/login");
        } else {
            mv.setViewName("/console/index");
        }
        return mv;
    }
    
    /**
     * 首页
     *
     * @return
     */
    @Perm(group="home", name="首页", description="首页")
    @GetMapping("/index.v")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        CurrAdmin currAdmin = adminContext.getCurrAdmin();
        if (currAdmin == null) {
            mv.setViewName("/console/login");
        } else {
            mv.setViewName("/console/index");
        }
        return mv;
    }
    
    /**
     * 用户列表
     *
     * @param request
     * @return
     */
    @Perm(group="user", name="用户列表", description="用户管理-用户列表")
    @GetMapping("/user/list.v")
    public ModelAndView userList(HttpServletRequest request) {
        int pageNum = NumberUtils.toInt(request.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 10);
        JSONObject params = JSONObject.parseObject(request.getParameter("params"));
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/console/user-list");
        mv.addObject("params", params);
        mv.addObject("pageInfo", userService.page(pageNum, pageSize, params));
        mv.addObject("registerTypeMap", RegisterType.getEnumInfo());
        mv.addObject("registerTypes", RegisterType.values());
        return mv;
    }
    
    /**
     * 产品列表
     *
     * @param request
     * @return
     */
    @Perm(group="product", name="产品列表", description="产品管理-产品列表")
    @GetMapping("/product/list.v")
    public ModelAndView productList(HttpServletRequest request) {
        int pageNum = NumberUtils.toInt(request.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 10);
        JSONObject params = JSONObject.parseObject(request.getParameter("params"));
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/console/product-list");
        mv.addObject("params", params);
        // TODO 产品数据
        return mv;
    }
    
    /**
     * 订单列表
     *
     * @param request
     * @return
     */
    @Perm(group="order", name="订单列表", description="订单管理-订单列表")
    @GetMapping("/order/list.v")
    public ModelAndView orderList(HttpServletRequest request) {
        int pageNum = NumberUtils.toInt(request.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 10);
        JSONObject params = JSONObject.parseObject(request.getParameter("params"));
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/console/order-list");
        mv.addObject("params", params);
        // TODO 订单数据
        return mv;
    }
    
    /**
     * 意见反馈列表
     *
     * @param request
     * @return
     */
    @Perm(group="feedback", name="意见反馈列表", description="反馈管理-意见反馈列表")
    @GetMapping("/feedback/list.v")
    public ModelAndView feedbackList(HttpServletRequest request) {
        int pageNum = NumberUtils.toInt(request.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 10);
        JSONObject params = JSONObject.parseObject(request.getParameter("params"));
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/console/feedback-list");
        mv.addObject("params", params);
        mv.addObject("pageInfo", feedbackService.pageFeedback(pageNum, pageSize, params));
        mv.addObject("feedbackTypeMap", FeedbackType.getEnumInfo());
        mv.addObject("feedbackTypes", FeedbackType.values());
        mv.addObject("feedbackStatusMap", FeedbackStatus.getEnumInfo());
        mv.addObject("feedbackStatuses", FeedbackStatus.values());
        return mv;
    }
    
    /**
     * 管理员列表
     *
     * @param request
     * @return
     */
    @Perm(group="admin", name="管理员列表", description="管理员管理-管理员列表")
    @GetMapping("/admin/list.v")
    public ModelAndView adminList(HttpServletRequest request) {
        int pageNum = NumberUtils.toInt(request.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 10);
        JSONObject params = JSONObject.parseObject(request.getParameter("params"));
        if (params != null) {
            // 强制转Boolean,避免前端传的值为字符串
            params.put("lockFlag", params.getBoolean("lockFlag"));
        }
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/console/admin-list");
        mv.addObject("params", params);
        mv.addObject("pageInfo", adminService.page(pageNum, pageSize, params));
        return mv;
    }
    
    /**
     * 管理员权限
     *
     * @param request
     * @return
     */
    @Perm(group="admin", name="管理员权限", description="管理员管理-管理员权限")
    @GetMapping("/admin/permission.v")
    public ModelAndView adminPermission(HttpServletRequest request) {
        long adminId = NumberUtils.toLong(request.getParameter("adminId"), 0L);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/console/admin-permission");
        // 权限数据
        Map<String, List<PermissionWithAccessVO>> permissionData = new HashMap<String, List<PermissionWithAccessVO>>();
        JSONObject params = new JSONObject();
        params.put("adminId", adminId);
        for (PermissionGroup permissionGroup : PermissionGroup.values()) {
            params.put("groupCode", permissionGroup.getCode());
            List<PermissionWithAccessVO> permissions = permissionService.listWithAccess(params);
            permissionData.put(permissionGroup.getCode(), permissions);
        }
        mv.addObject("permissionData", permissionData);
        mv.addObject("adminId", adminId);
        mv.addObject("permissionGroups", PermissionGroup.values());
        return mv;
    }
    
    /**
     * 管理员个人信息
     *
     * @param request
     * @return
     */
    @Perm(group="", name="管理员个人信息", description="管理员个人信息")
    @GetMapping("/admin/info.v")
    public ModelAndView adminInfo(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/console/admin-info");
        return mv;
    }
    
    /**
     * 网站配置列表
     *
     * @param request
     * @return
     */
    @Perm(group="system", name="网站配置列表", description="系统管理-网站配置列表")
    @GetMapping("/site_config/list.v")
    public ModelAndView siteConfigList(HttpServletRequest request) {
        int pageNum = NumberUtils.toInt(request.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 10);
        JSONObject params = JSONObject.parseObject(request.getParameter("params"));
        if (params != null) {
            // 强制转Boolean,避免前端传的值为字符串
            params.put("lockFlag", params.getBoolean("lockFlag"));
        }
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/console/site-config-list");
        mv.addObject("params", params);
        mv.addObject("pageInfo", siteConfigService.page(pageNum, pageSize, params));
        return mv;
    }
    
    /**
     * 调度列表
     *
     * @param request
     * @return
     */
    @Perm(group="system", name="调度列表", description="系统管理-调度列表")
    @GetMapping("/schedule_job/list.v")
    public ModelAndView scheduleJobList(HttpServletRequest request) {
        int pageNum = NumberUtils.toInt(request.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 10);
        JSONObject params = JSONObject.parseObject(request.getParameter("params"));
        if (params != null) {
            params.put("status", params.getInteger("status"));
        }
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/console/schedule-job-list");
        mv.addObject("params", params);
        mv.addObject("pageInfo", scheduleJobService.page(pageNum, pageSize, params));
        mv.addObject("scheduleJobStatuses", ScheduleJobStatus.values());
        mv.addObject("scheduleJobStatusMap", ScheduleJobStatus.getEnumInfo());
        mv.addObject("scheduleJobExecuteStatusMap", ScheduleJobExecuteStatus.getEnumInfo());
        return mv;
    }
}
