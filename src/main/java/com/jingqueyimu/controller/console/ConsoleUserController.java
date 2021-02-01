package com.jingqueyimu.controller.console;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jingqueyimu.annotation.Perm;
import com.jingqueyimu.controller.BaseController;
import com.jingqueyimu.handler.excel.IExcelExportHandler;
import com.jingqueyimu.model.User;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.model.dict.RegisterType;
import com.jingqueyimu.service.UserService;
import com.jingqueyimu.util.DateUtil;
import com.jingqueyimu.util.ExcelUtil;

/**
 * 用户控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/console/user")
public class ConsoleUserController extends BaseController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 分页查询用户
     *
     * @param params
     * @return
     */
    @Perm(group="user", name="分页查询用户", description="用户管理-用户列表-分页查询用户")
    @PostMapping("/page")
    public ResultData page(@RequestBody JSONObject params) {
        int pageNum = params.getIntValue("pageNum");
        int pageSize = params.getIntValue("pageSize");
        PageInfo<User> page = userService.page(pageNum, pageSize, params);
        for (User user : page.getList()) {
            user.setPassword(null);
        }
        return ResultData.succ(page);
    }
    
    /**
     * 用户详情
     *
     * @param params
     * @return
     */
    @Perm(group="user", name="用户详情", description="用户管理-用户列表-用户详情")
    @PostMapping("/get")
    public ResultData detail(@RequestBody JSONObject params) {
        long userId = params.getLongValue("userId");
        User user = userService.getById(userId);
        user.setPassword(null);
        return ResultData.succ(user);
    }
    
    /**
     * 用户导出
     *
     * @param params
     * @return
     */
    @Perm(group="user", name="用户导出", description="用户管理-用户列表-用户导出")
    @RequestMapping("/exportExcel")
    public void exportExcel(@RequestBody JSONObject params, HttpServletResponse response) {
        ServletOutputStream os = null;
        try {
            String fileName = "用户列表";
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
            // 用户列表数据
            List<User> list = userService.list(params);
            // 表头
            String[] headers = new String[] {"编号 ", "用户名", "姓名", "手机号", "邮箱", "注册方式", "注册时间", "上次登录时间", "是否VIP"};
            os = response.getOutputStream();
            // 导出
            ExcelUtil.exportExcel(fileName, list, headers, os, new IExcelExportHandler<User>() {
                
                @Override
                public List<Object> handle(User user) {
                    List<Object> rowDatas = new ArrayList<>();
                    rowDatas.add(user.getId());
                    rowDatas.add(user.getUsername());
                    rowDatas.add(user.getRealName());
                    rowDatas.add(user.getMobile());
                    rowDatas.add(user.getEmail());
                    rowDatas.add(RegisterType.getEnum(user.getRegisterType()).getValue());
                    rowDatas.add(DateUtil.format(user.getRegisterTime(), "yyyy-MM-dd HH:mm:ss"));
                    rowDatas.add(user.getLastLoginTime() == null ? "" : DateUtil.format(user.getLastLoginTime(), "yyyy-MM-dd HH:mm:ss"));
                    rowDatas.add(Boolean.TRUE.equals(user.getVipFlag()) ? "是" : "否");
                    return rowDatas;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
