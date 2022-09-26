package com.j3mall.mall.decorator;

import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson.JSON;
import com.github.javafaker.Faker;
import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.mall.dubbo.consumer.ProductsServiceRpc;
import com.j3mall.mall.vo.MallProductVO;
import com.j3mall.modules.feign.product.ProductFeginService;
import com.j3mall.modules.feign.product.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDecorator {

    private final ProductFeginService productFeginService;
    // Dubbo 与 Feign 示范
    private final ProductsServiceRpc productsServiceRpc;

    public MallProductVO productInfo(Integer userId, Integer productId) {
        MallProductVO mallProduct = new MallProductVO();
        JsonResult<ProductVO> jsonResult = productFeginService.queryProductById(userId, productId);
        if (jsonResult.isSuccess()) {
            BeanUtils.copyProperties(jsonResult.getBody(), mallProduct);
        }
        return mallProduct;
    }

    public List<MallProductVO> productsListByUser(Integer userId) {
        List<MallProductVO> mallProducts = null;
        JsonResult<List<ProductVO>> jsonResult = productsServiceRpc.queryProductsByUser(userId);

        if (jsonResult.isSuccess()) {
            mallProducts = jsonResult.getBody().stream().map(productVO -> {
                MallProductVO mallProduct = new MallProductVO();
                BeanUtils.copyProperties(productVO, mallProduct);
                return mallProduct;
            }).collect(Collectors.toList());
        }
        return mallProducts;
    }

    /**
     * 获取一个商品，或者随机发布一个
     * @param sellerUserId
     * @return
     */
    public ProductVO randomProductByUser(Integer sellerUserId) {
        ProductVO productVO = new ProductVO();
        List<MallProductVO> productList = productsListByUser(sellerUserId);
        if (ObjUtil.isNotEmpty(productList)) {
            BeanUtils.copyProperties(productList.get(0), productVO);
        } else {
            productVO = publishRandomProduct(sellerUserId, new ProductVO());
        }
        return productVO;
    }

    /**
     * 随机发布一个商品，仅用于测试
     */
    public ProductVO publishRandomProduct(Integer userId, ProductVO productVO) {
        ProductVO newProduct = randomProductVo(userId, productVO);

        JsonResult<ProductVO> jsonResult = productFeginService.createProduct(userId, newProduct);
        log.debug("用户随机发布商品{}, {}", userId, JSON.toJSONString(jsonResult.getBody()));
        return jsonResult.getBody();
    }

    private ProductVO randomProductVo(Integer userId, ProductVO productVO) {
        Faker faker = new Faker(new Locale("zh-CN"));
        ProductVO newProduct = new ProductVO();
        newProduct.setOwnerId(userId);

        newProduct.setName(Optional.ofNullable(productVO.getName()).filter(ObjUtil::isNotEmpty)
                .orElse(faker.book().title()));
        newProduct.setStockAmount(Optional.ofNullable(productVO.getStockAmount())
                .orElse(new Random().nextInt(5000)));
        BigDecimal pdPrice = Optional.ofNullable(productVO.getPdPrice())
                .orElse(BigDecimal.valueOf(new Random().nextInt(20)));
        newProduct.setPdPrice(pdPrice);
        return newProduct;
    }

}
