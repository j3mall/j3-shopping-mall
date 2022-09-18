package com.j3mall.mall.vo;

import com.j3mall.modules.feign.user.vo.UserVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MallProductVO {

    @ApiModelProperty(value = "商品ID")
    private Integer id;

    @ApiModelProperty(value = "发布人ID")
    private Integer ownerId;

    @ApiModelProperty(value = "商品名")
    private String name;

    @ApiModelProperty(value = "库存数量")
    private Integer stockAmount;

    @ApiModelProperty(value = "销售价")
    private BigDecimal pdPrice;

    @ApiModelProperty(value = "发布人信息")
    private UserVO ownerVO;

}
