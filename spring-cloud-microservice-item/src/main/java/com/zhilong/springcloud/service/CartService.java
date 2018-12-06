package com.zhilong.springcloud.service;

import com.zhilong.springcloud.entity.Cart;

public interface CartService {
    Cart addCartItem(Long itemId, Integer num, String username);
    Cart deleteCartItem(Long itemId, Integer num, String username);
    Cart getUserCart(String username);
}
