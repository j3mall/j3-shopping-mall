package com.j3mall.product.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductVO {

    @ApiModelProperty(value = "商品ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "发布人ID")
    private Integer ownerId;

    @ApiModelProperty(value = "商品名")
    private String name;

    @ApiModelProperty(value = "库存数量")
    private Integer stockAmount;

    @ApiModelProperty(value = "销售价")
    private BigDecimal pdPrice;

    private LocalDateTime updatedAt;

}
