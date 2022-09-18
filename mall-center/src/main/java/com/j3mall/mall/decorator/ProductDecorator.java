package com.j3mall.mall.decorator;

import com.j3mall.j3.framework.utils.JsonResult;
import com.j3mall.mall.vo.MallProductVO;
import com.j3mall.modules.feign.product.ProductFeginService;
import com.j3mall.modules.feign.product.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductDecorator {

    private final ProductFeginService productFeginService;

    public MallProductVO productInfo(Integer productId) {
        MallProductVO mallProduct = new MallProductVO();
        JsonResult<ProductVO> jsonResult = productFeginService.queryProductById(productId);
        if (jsonResult.isSuccess()) {
            BeanUtils.copyProperties(jsonResult.getBody(), mallProduct);
        }
        return mallProduct;
    }

    public List<MallProductVO> productsListByUser(Integer userId) {
        List<MallProductVO> mallProducts = new ArrayList<>();
        JsonResult<List<ProductVO>> jsonResult = productFeginService.queryProductsByUser(userId);

        if (jsonResult.isSuccess()) {
            mallProducts = jsonResult.getBody().stream().map(productVO -> {
                MallProductVO mallProduct = new MallProductVO();
                BeanUtils.copyProperties(productVO, mallProduct);
                return mallProduct;
            }).collect(Collectors.toList());
        }
        return mallProducts;
    }

}
