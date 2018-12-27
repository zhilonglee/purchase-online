package com.zhilong.springcloud.fegin;

import com.zhilong.springcloud.entity.to.CartItemTo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderFallback implements OrderFeginClient {
    @Override
    public ResponseEntity generateSecKillOrder(CartItemTo cartItemTo) {
        return ResponseEntity.badRequest().body("Exception from Order Side");
    }
}
