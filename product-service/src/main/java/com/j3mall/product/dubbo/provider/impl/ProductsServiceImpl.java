package com.j3mall.product.dubbo.provider.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.j3mall.dubbo.provider.ProductDubboService;
import com.j3mall.j3.framework.constants.KeyConstants;
import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.modules.feign.product.vo.ProductVO;
import com.j3mall.product.decorator.ProductDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service(version = "1.0.0")
public class ProductsServiceImpl implements ProductDubboService {

    @Autowired
    private ProductDecorator productDecorator;

    public JsonResult<List<ProductVO>> queryProductsByUser(@RequestParam(KeyConstants.KEY_USERID) Integer userId) {
        List<ProductVO> products = productDecorator.getProductsByUserId(userId);
        return JsonResult.success(products);
    }

}

