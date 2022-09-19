package com.j3mall.mall.controller;

import com.j3mall.j3.framework.constants.KeyConstants;
import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.mall.decorator.ProductDecorator;
import com.j3mall.mall.vo.MallProductVO;
import com.j3mall.modules.feign.product.vo.ProductVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@Api(tags = "聚合商品服务的接口")
@RequiredArgsConstructor
public class ProductsController {

    @Autowired
    private ProductDecorator productDecorator;

    @GetMapping("/{id}")
    @ApiOperation("获取商城单个商品详情")
    public JsonResult<MallProductVO> productInfo(@RequestHeader(KeyConstants.KEY_J3_USERID) Integer j3UserId,
                                                 @PathVariable("id") Integer productId) {
        MallProductVO product = productDecorator.productInfo(j3UserId, productId);
        return JsonResult.success(product);
    }

    @GetMapping("")
    @ApiOperation("获取用户的商品列表，默认当前用户")
    public JsonResult<List<MallProductVO>> productsList(@RequestHeader(KeyConstants.KEY_J3_USERID) Integer j3UserId,
                                                        @RequestParam(value = KeyConstants.KEY_USERID, required = false) Integer userId) {
        Integer queryUserId = Optional.ofNullable(userId).orElse(j3UserId);
        List<MallProductVO> products = productDecorator.productsListByUser(queryUserId);
        return JsonResult.success(products);
    }

    @PostMapping("")
    @ApiOperation("发布一个商品(支持随机生成)")
    public JsonResult<ProductVO> publishProduct(@RequestHeader(KeyConstants.KEY_J3_USERID) Integer j3UserId,
                                                @RequestBody ProductVO productVO) {
        return JsonResult.success(productDecorator.publishRandomProduct(j3UserId, productVO));
    }

}
