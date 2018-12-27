package com.zhilong.springcloud.service;

import com.zhilong.springcloud.entity.to.CartItemTo;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity generateOrder(CartItemTo cartItemTo,Boolean isNormalOrder);

    ResponseEntity getOrderInfo(Long id);
}
