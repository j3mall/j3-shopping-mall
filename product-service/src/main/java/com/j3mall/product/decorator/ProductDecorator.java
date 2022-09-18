package com.j3mall.product.decorator;

import com.j3mall.product.mybatis.service.ProductService;
import com.j3mall.product.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductDecorator {
    @Autowired
    private ProductService productService;

    public ProductVO getProductById(int id) {
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(productService.getById(id), productVO);
        return productVO;
    }

    public List<ProductVO> getProductsByUserId(int userId) {
        return productService.getProductsByUserId(userId);
    }

}
