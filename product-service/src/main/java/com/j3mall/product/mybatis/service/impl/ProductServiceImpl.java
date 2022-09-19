package com.j3mall.product.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.j3mall.modules.feign.product.vo.ProductVO;
import com.j3mall.product.mybatis.domain.Product;
import com.j3mall.product.mybatis.mapper.ProductMapper;
import com.j3mall.product.mybatis.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public List<ProductVO> getProductsByUserId(int userId) {
        List<Product> products = baseMapper.selectList(
                new QueryWrapper<Product>().lambda().eq(Product::getOwnerId, userId));
        return products.stream().map(product -> {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(product, productVO);
            return productVO;
        }).collect(Collectors.toList());
    }

}
