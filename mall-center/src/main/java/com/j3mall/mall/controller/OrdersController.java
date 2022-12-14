package com.j3mall.mall.controller;

import com.j3mall.j3.framework.constants.KeyConstants;
import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.mall.decorator.OrderDecorator;
import com.j3mall.mall.decorator.ProductDecorator;
import com.j3mall.modules.feign.order.OrderFeignService;
import com.j3mall.modules.feign.order.vo.OrderVO;
import com.j3mall.modules.feign.product.vo.ProductVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Api(tags = "聚合订单服务的接口")
@RequiredArgsConstructor
public class OrdersController {

    @Autowired
    private OrderDecorator orderDecorator;
    @Autowired
    private ProductDecorator productDecorator;

    @PostMapping("")
    @ApiOperation("创建一个待付款订单")
    public JsonResult<OrderVO> createOrder(@RequestHeader(KeyConstants.KEY_J3_USERID) Integer j3UserId,
                                           @RequestBody OrderVO orderVO) {
        orderVO.setProductVO(productDecorator.randomProductByUser(j3UserId+1));
        OrderVO orderVo = orderDecorator.createRandomOrder(j3UserId, orderVO);
        return JsonResult.success(orderVo);
    }

}
