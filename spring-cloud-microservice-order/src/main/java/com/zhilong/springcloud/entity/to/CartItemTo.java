package com.zhilong.springcloud.entity.to;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CartItemTo implements Serializable {

    private List<CartItem> cartItems = new ArrayList<>();
    private String  username;

}
