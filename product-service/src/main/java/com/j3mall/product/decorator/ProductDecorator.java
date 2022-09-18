package com.j3mall.product.decorator;

import com.j3mall.modules.feign.product.vo.ProductVO;
import com.j3mall.product.mybatis.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDecorator {

    @Autowired
    private final UserDecorator userDecorator;
    @Autowired
    private final ProductService productService;

    public ProductVO getProductById(Integer id) {
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(productService.getById(id), productVO);
        productVO.setOwnerVO(userDecorator.queryUserById(productVO.getOwnerId()));
        return productVO;
    }

    public List<ProductVO> getProductsByUserId(int userId) {
        List<ProductVO> products = productService.getProductsByUserId(userId);
        List<ProductVO> productVos = products.stream().peek(productVO -> {
            productVO.setOwnerVO(userDecorator.queryUserById(userId));
        }).collect(Collectors.toList());
        log.info("p.s用户{}返回{}个商品", userId, productVos.size());
        return productVos;
    }

}
