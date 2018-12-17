package com.zhilong.springcloud.controller;

import com.zhilong.springcloud.contonst.PurchaseOnlieGlobalConstant;
import com.zhilong.springcloud.entity.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1")
public class OrderController {

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/detail")
    public ResponseEntity generateOrder(@RequestBody Map<String,String> map, String username){
        // String tokenKey = PurchaseOnlieGlobalConstant.OAUTH2_TOKEN_IN_REDIS_PREFIX + username;
        // TokenResponse tokenResponse = (TokenResponse)redisUtils.getObj(tokenKey);
        logger.info(map.toString());
        return null;
    }
}
