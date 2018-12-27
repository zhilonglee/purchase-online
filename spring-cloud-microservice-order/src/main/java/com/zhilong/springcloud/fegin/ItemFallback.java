package com.zhilong.springcloud.fegin;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemFallback implements ItemFeginClient{

    @Override
    public ResponseEntity deleteCartItem(Long itemId, Integer num, String username) {
        return  ResponseEntity.badRequest().body("Exception from Item Side");
    }

    @Override
    public ResponseEntity item(Long id) {
        return  ResponseEntity.badRequest().body("Exception from Item Side");
    }

    @Override
    public ResponseEntity itemDeductStockNum(Long id, Integer num) {
        return  ResponseEntity.badRequest().body("Exception from Item Side");
    }

    @Override
    public ResponseEntity<String> secKillitem(Long id) {
        return  ResponseEntity.badRequest().body("Exception from Item Side");
    }
}
