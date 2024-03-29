package com.j3mall.order.decorator;

import com.j3mall.dubbo.provider.ProductDubboService;
import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.modules.feign.order.vo.OrderVO;
import com.j3mall.modules.feign.product.ProductFeginService;
import com.j3mall.modules.feign.product.vo.ProductVO;
import com.j3mall.order.mybatis.domain.Order;
import com.j3mall.order.mybatis.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderDecorator {

    @Autowired
    private final OrderService orderService;

    //private final ProductFeginService productFeginService;
    @DubboReference(check = false)
    private final ProductDubboService productDubboService;

    @Lazy
    public OrderDecorator(OrderService orderService, ProductDubboService productDubboService) {
        this.orderService = orderService;
        this.productDubboService = productDubboService;
    }

    public List<OrderVO> getOrdersByUserId(Integer userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);

        return orders.stream().map(order -> {
            OrderVO orderVO = new OrderVO();
            JsonResult<ProductVO> jsonResult = productDubboService.queryProductById(userId, order.getProductId());
            if (jsonResult.isSuccess()) {
                BeanUtils.copyProperties(order, orderVO);
                orderVO.setProductVO(jsonResult.getBody());
            }
            return orderVO;
        }).collect(Collectors.toList());
    }

    public Order createUnpaidOrder(OrderVO orderVO) {
        Order order = new Order();
        BeanUtils.copyProperties(orderVO, order);
        // TODO 统一设置时间
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        if (orderService.save(order)) {
            return order;
        } else {
            // TODO 自定义异常处理
            return null;
        }
    }

}
