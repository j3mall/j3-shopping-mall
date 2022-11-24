package com.j3mall.mall.decorator;

import com.alibaba.fastjson.JSON;
import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.modules.feign.order.OrderFeignService;
import com.j3mall.modules.feign.order.vo.OrderVO;
import com.j3mall.modules.feign.product.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderDecorator {
    private final OrderFeignService orderFeignService;

    private final Random random = new Random(System.currentTimeMillis());

    /** 随机生成订单内容，仅用于测试 **/
    @Transactional(rollbackFor = Exception.class)
    public OrderVO createRandomOrder(Integer userId, OrderVO orderVO) {
        OrderVO newOrder = new OrderVO();
        ProductVO productVO = orderVO.getProductVO();
        newOrder.setUserId(userId);
        newOrder.setProductId(productVO.getId());

        newOrder.setStatus(OrderVO.Status.UNPAID);
        newOrder.setPdAmount(random.nextInt(3) + 1);
        newOrder.setTotalPrice(productVO.getPdPrice().multiply(BigDecimal.valueOf(newOrder.getPdAmount())));

        JsonResult<OrderVO> jsonResult = orderFeignService.createOrder(userId, newOrder);
        if (jsonResult.isSuccess()) {
            OrderVO resultOrder = jsonResult.getBody();
            resultOrder.setProductVO(productVO);
            return resultOrder;
        } else {
            // TODO: 抛出自定义订单异常
            log.error("订单创建失败: {}, {}", JSON.toJSONString(orderVO), jsonResult);
            return null;
        }
    }

}
