package com.zhilong.springcloud.controller;

import com.zhilong.springcloud.entity.Cart;
import com.zhilong.springcloud.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping("/{itemId}")
    public ResponseEntity addCartItem(@PathVariable Long itemId, Integer num, String username) {
        Cart cart = cartService.addCartItem(itemId, num, username);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity deleteCartItem(@PathVariable Long itemId, Integer num, String username) {
        Cart cart = cartService.deleteCartItem(itemId, num, username);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{username}")
    public ResponseEntity getUserCart(@PathVariable String username) {
        Cart cart = cartService.getUserCart(username);
        return ResponseEntity.ok((cart != null ? cart : new Cart()));
    }
}
