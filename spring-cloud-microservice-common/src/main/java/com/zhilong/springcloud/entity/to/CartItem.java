package com.zhilong.springcloud.entity.to;

import lombok.Data;

import java.io.Serializable;

@Data
public class CartItem implements Serializable {
    private Long id;
    private Integer num;
}