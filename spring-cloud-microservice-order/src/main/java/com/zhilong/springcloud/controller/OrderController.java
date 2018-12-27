package com.zhilong.springcloud.controller;

import com.zhilong.springcloud.entity.to.CartItemTo;
import com.zhilong.springcloud.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class OrderController {

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    @PostMapping("/detail")
    public ResponseEntity generateOrder(@RequestBody CartItemTo cartItemTo){
        return (cartItemTo.getCartItems().size() != 0 ? orderService.generateOrder(cartItemTo,true) : ResponseEntity.ok("No Items to process"));
    }

    @PostMapping("/seckill/detail")
    public ResponseEntity generateSecKillOrder(@RequestBody CartItemTo cartItemTo){
        return (cartItemTo.getCartItems().size() != 0 ? orderService.generateOrder(cartItemTo,false) : ResponseEntity.ok("No Items to process"));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity getOrderInfo(@PathVariable("id") Long id ){
        return orderService.getOrderInfo(id);
    }
}
