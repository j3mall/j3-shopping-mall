package com.j3mall.product.decorator;

import com.j3mall.modules.feign.product.vo.ProductVO;
import com.j3mall.product.mybatis.domain.Product;
import com.j3mall.product.mybatis.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    public List<ProductVO> getProductsByUserId(Integer userId) {
        List<ProductVO> products = productService.getProductsByUserId(userId);
        List<ProductVO> productVos = products.stream().peek(productVO -> {
            productVO.setOwnerVO(userDecorator.queryUserById(userId));
        }).collect(Collectors.toList());
        log.info("p.s用户{}返回{}个商品", userId, productVos.size());
        return productVos;
    }

    public Product createProduct(ProductVO productVO) {
        Product product = new Product();
        BeanUtils.copyProperties(productVO, product);
        // TODO 统一设置时间
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        if (productService.save(product)) {
            return product;
        } else {
            // TODO 自定义异常处理
            return null;
        }
    }

}
