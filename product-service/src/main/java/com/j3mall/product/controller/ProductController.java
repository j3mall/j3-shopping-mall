package com.j3mall.product.controller;

import com.j3mall.j3.framework.constants.KeyConstants;
import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.modules.feign.product.vo.ProductVO;
import com.j3mall.product.decorator.ProductDecorator;
import com.j3mall.product.mybatis.domain.Product;
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

    @GetMapping("/{productId}")
    @ApiOperation("获取商品详情")
    public JsonResult<ProductVO> queryProductById(@RequestHeader(KeyConstants.KEY_J3_USERID) Integer j3UserId,
                                            @PathVariable("productId") Integer productId) {
        ProductVO product = productDecorator.getProductById(productId);
        return JsonResult.success(product);
    }

    @GetMapping("")
    @ApiOperation("获取商品列表")
    public JsonResult<List<ProductVO>> queryProductsByUser(@RequestParam(KeyConstants.KEY_USERID) Integer userId) {
        List<ProductVO> products = productDecorator.getProductsByUserId(userId);
        return JsonResult.success(products);
    }

    @PostMapping("")
    @ApiOperation("发布一个商品")
    public JsonResult<Product> createProduct(@RequestHeader(KeyConstants.KEY_J3_USERID) Integer j3UserId,
                                             @RequestBody ProductVO productVO) {
        productVO.setOwnerId(j3UserId);
        return JsonResult.success(productDecorator.createProduct(productVO));
    }

}
