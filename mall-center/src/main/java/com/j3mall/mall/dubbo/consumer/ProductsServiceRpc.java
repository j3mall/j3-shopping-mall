package com.j3mall.mall.dubbo.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.j3mall.dubbo.provider.ProductDubboService;
import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.modules.feign.product.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 将服务调用包装，可以方便进行调用监控
 */
@Component
@Slf4j
public class ProductsServiceRpc {

    @Reference(version = "1.0.0")
    private ProductDubboService productDubboService;

    public JsonResult<List<ProductVO>> queryProductsByUser(Integer userId) {
        JsonResult<List<ProductVO>> jsonResult = null;
        try {
            jsonResult = productDubboService.queryProductsByUser(userId);
            log.debug("Dubbo调用成功, {}", jsonResult);
        } catch (Exception ex) {
            log.error("Dubbo调用异常, ", ex);
            ex.printStackTrace();
        }
        return jsonResult;
    }

}
