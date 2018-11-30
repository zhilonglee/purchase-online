package com.zhilong.springcloud.entity.dto;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ItemSimpleTo implements Serializable {
    private Long id;
    private BigDecimal currentprice;
    private String image;
    private String item_des;
    private String title;
    private Integer cat_id;

    public ItemSimpleTo() {
    }

    public ItemSimpleTo(Long id, BigDecimal currentprice, String image, String item_des, String title, Integer cat_id) {
        this.id = id;
        this.currentprice = currentprice;
        this.image = image;
        this.item_des = item_des;
        this.title = title;
        this.cat_id = cat_id;
    }
}