package com.j3mall.product.controller;

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
    public JsonResult<ProductVO> queryProduct(@RequestParam("id") Integer id) {
        ProductVO product = productDecorator.getProductById(id);
        return JsonResult.success(product);
    }

    @GetMapping("")
    @ApiOperation("根据指定用户的商品列表")
    public JsonResult<List<ProductVO>> queryProducts(@RequestParam("userId") Integer userId) {
        List<ProductVO> products = productDecorator.getProductsByUserId(userId);
        return JsonResult.success(products);
    }

}
