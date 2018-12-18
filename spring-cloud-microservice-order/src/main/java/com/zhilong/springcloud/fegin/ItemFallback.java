package com.zhilong.springcloud.fegin;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemFallback implements ItemFeginClient{

    @Override
    public ResponseEntity deleteCartItem(Long itemId, Integer num, String username) {
        return ResponseEntity.ok("Exception from Item Side");
    }

    @Override
    public ResponseEntity item(Long id) {
        return ResponseEntity.ok("Exception from Item Side");
    }

    @Override
    public ResponseEntity itemDeductStockNum(Long id, Integer num) {
        return ResponseEntity.ok("Exception from Item Side");
    }
}
