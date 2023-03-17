package com.j3mall.product.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.j3mall.modules.feign.product.vo.ProductVO;
import com.j3mall.product.constants.Constants;
import com.j3mall.product.mybatis.domain.Product;
import com.j3mall.product.mybatis.mapper.ProductMapper;
import com.j3mall.product.mybatis.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    /** 根据参数查询缓存，若有就返回；没有就查询，并放入缓存 **/
    @Cacheable(value = Constants.REDIS_KEY_PRODUCT, key = "#id")
    public Product findProductById(Integer id) {
        log.info("--findProductById({})", id);
        return baseMapper.selectById(id);
    }

    /** 每次都会执行方法体，且清除缓存 */
    @CacheEvict(value = {Constants.REDIS_KEY_PRODUCT}, key = "#product.id")
    public synchronized int updateProductById(Product product) {
        log.info("--updateProductById({})", product.getId());
        return baseMapper.updateById(product);
    }

    /** 根据指定的参数移除缓存 **/
    @CacheEvict(value = Constants.REDIS_KEY_PRODUCT, key = "#id")
    public int removeProductById(Integer id) {
        log.info("--removeProductById({})", id);
        return baseMapper.deleteById(id);
    }

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
