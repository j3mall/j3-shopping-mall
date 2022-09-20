package com.j3mall.order.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.j3mall.order.mybatis.domain.Order;
import com.j3mall.order.mybatis.mapper.OrderMapper;
import com.j3mall.order.mybatis.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        return baseMapper.selectList(
                new QueryWrapper<Order>().lambda().eq(Order::getUserId, userId));
    }

}
