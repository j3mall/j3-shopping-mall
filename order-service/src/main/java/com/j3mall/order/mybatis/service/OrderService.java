package com.j3mall.order.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.j3mall.modules.feign.order.vo.OrderVO;
import com.j3mall.order.mybatis.domain.Order;

import java.util.List;

public interface OrderService extends IService<Order> {

    /**
     * 获取指定用户的订单列表
     */
    List<Order> getOrdersByUserId(int userId);

}
