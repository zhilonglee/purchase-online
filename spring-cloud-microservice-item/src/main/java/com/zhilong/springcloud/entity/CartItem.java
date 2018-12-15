package com.zhilong.springcloud.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CartItem implements Serializable {
    private Long itemId;
    private BigDecimal price;
    private BigDecimal currentprice;
    private String image;
    private String title;
    private String category;
    private Integer num;
    private String sell_point;
}
