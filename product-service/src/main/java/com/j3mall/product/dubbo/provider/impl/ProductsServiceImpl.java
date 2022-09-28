package com.j3mall.product.dubbo.provider.impl;

import cn.hutool.core.bean.BeanUtil;
import com.j3mall.dubbo.provider.ProductDubboService;
import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.modules.feign.product.vo.ProductVO;
import com.j3mall.product.decorator.ProductDecorator;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
@Slf4j
public class ProductsServiceImpl implements ProductDubboService {

    @Autowired
    private ProductDecorator productDecorator;

    @Override
    public JsonResult<ProductVO> queryProductById(Integer j3UserId, Integer productId) {
        return JsonResult.success(productDecorator.getProductById(productId));
    }

    public JsonResult<List<ProductVO>> queryProductsByUser(Integer userId) {
        log.debug("Request from dubbo consumer: " + RpcContext.getContext().getRemoteAddress());
        List<ProductVO> products = productDecorator.getProductsByUserId(userId);
        log.debug("Response from dubbo provider: " + RpcContext.getContext().getLocalAddress());
        return JsonResult.success(products);
    }

    @Override
    public JsonResult<ProductVO> createProduct(Integer j3UserId, ProductVO productVO) {
        ProductVO newProductVo = new ProductVO();
        BeanUtil.copyProperties(productDecorator.createProduct(productVO), newProductVo);
        return JsonResult.success(newProductVo);
    }

}

