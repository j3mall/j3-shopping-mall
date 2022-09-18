package com.j3mall.mall.controller;

import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.mall.decorator.ProductDecorator;
import com.j3mall.mall.vo.MallProductVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Api(tags = "聚合商品服务的接口")
@RequiredArgsConstructor
public class ProductsController {

    @Autowired
    private ProductDecorator productDecorator;

    @GetMapping("/{id}")
    @ApiOperation("获取商城单个商品详情")
    public JsonResult<MallProductVO> productInfo(@PathVariable("id") Integer id) {
        MallProductVO product = productDecorator.productInfo(id);
        return JsonResult.success(product);
    }

    @GetMapping("/search")
    @ApiOperation("根据条件搜索商品")
    public JsonResult<List<MallProductVO>> searchProducts(@RequestParam("userId") Integer userId) {
        List<MallProductVO> products = productDecorator.productsListByUser(userId);
        return JsonResult.success(products);
    }

}
