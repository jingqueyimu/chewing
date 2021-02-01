package com.jingqueyimu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingqueyimu.mapper.OrderMapper;
import com.jingqueyimu.model.Order;

/**
 * 订单服务
 *
 * @author zhuangyilian
 */
@Service
public class OrderService extends BaseService<Order> {
    
    @Autowired
    private OrderMapper orderMapper;
}
