package com.jingqueyimu.controller.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jingqueyimu.annotation.Perm;
import com.jingqueyimu.controller.BaseController;
import com.jingqueyimu.model.Order;
import com.jingqueyimu.model.bean.ResultData;
import com.jingqueyimu.service.OrderService;

/**
 * 订单控制器
 *
 * @author zhuangyilian
 */
@RestController
@RequestMapping("/console/order")
public class ConsoleOrderController extends BaseController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 分页查询订单
     *
     * @param params
     * @return
     */
    @Perm(group="order", name="分页查询订单", description="订单管理-订单列表-分页查询订单")
    @PostMapping("/page")
    public ResultData page(@RequestBody JSONObject params) {
        int pageNum = params.getIntValue("pageNum");
        int pageSize = params.getIntValue("pageSize");
        PageInfo<Order> page = orderService.page(pageNum, pageSize, params);
        return ResultData.succ(page);
    }
}
