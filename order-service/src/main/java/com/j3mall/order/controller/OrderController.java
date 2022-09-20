package com.j3mall.order.controller;

import com.j3mall.j3.framework.constants.KeyConstants;
import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.modules.feign.order.vo.OrderVO;
import com.j3mall.modules.feign.product.vo.ProductVO;
import com.j3mall.order.decorator.OrderDecorator;
import com.j3mall.order.mybatis.domain.Order;
import com.j3mall.order.mybatis.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@Api(tags = "聚合商品服务的接口")
public class OrderController {

    @Autowired
    private OrderDecorator orderDecorator;

    @GetMapping("")
    @ApiOperation("获取用户的商品列表，默认当前用户")
    public JsonResult<List<OrderVO>> ordersList(@RequestHeader(KeyConstants.KEY_J3_USERID) Integer j3UserId,
                                                       @RequestParam(value = KeyConstants.KEY_USERID, required = false) Integer userId) {
        Integer queryUserId = Optional.ofNullable(userId).orElse(j3UserId);
        List<OrderVO> orders = orderDecorator.getOrdersByUserId(queryUserId);
        return JsonResult.success(orders);
    }

    @PostMapping("")
    @ApiOperation("创建一个待付款订单")
    public JsonResult<Order> createOrder(@RequestHeader(KeyConstants.KEY_J3_USERID) Integer j3UserId,
                                         @RequestBody OrderVO orderVO) {
        orderVO.setUserId(j3UserId);
        Order order = orderDecorator.createUnpaidOrder(orderVO);
        return JsonResult.success(order);
    }

}
