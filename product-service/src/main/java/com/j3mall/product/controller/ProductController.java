package com.j3mall.product.controller;

import com.j3mall.j3.framework.constants.KeyConstants;
import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.modules.feign.product.vo.ProductVO;
import com.j3mall.product.decorator.ProductDecorator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Api(tags = "商品相关接口")
public class ProductController {

    @Autowired
    private ProductDecorator productDecorator;

    @GetMapping("/info")
    @ApiOperation("获取商品详情")
    public JsonResult<ProductVO> queryProduct(@RequestHeader(KeyConstants.KEY_J3_USERID) Integer userId, @RequestParam("productId") Integer productId) {
        ProductVO product = productDecorator.getProductById(productId);
        return JsonResult.success(product);
    }

    @GetMapping("")
    @ApiOperation("获取我的商品列表")
    public JsonResult<List<ProductVO>> queryProducts(@RequestHeader(KeyConstants.KEY_J3_USERID) Integer userId) {
        List<ProductVO> products = productDecorator.getProductsByUserId(userId);
        return JsonResult.success(products);
    }

}
