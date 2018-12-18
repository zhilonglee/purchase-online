package com.zhilong.springcloud.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderItem implements Serializable {
    private Long id;
    private BigDecimal currentprice;
    private String image;
    private String title;
    private Long catId;
    private Integer num;
    private BigDecimal price;
    private Integer stockNum;
}
