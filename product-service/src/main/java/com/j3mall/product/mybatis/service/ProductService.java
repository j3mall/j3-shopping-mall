package com.j3mall.product.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.j3mall.product.mybatis.domain.Product;
import com.j3mall.product.vo.ProductVO;

import java.util.List;

public interface ProductService extends IService<Product> {

    /**
     * 获取指定用户的产品列表
     */
    List<ProductVO> getProductsByUserId(int userId);

}
