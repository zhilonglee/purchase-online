package com.zhilong.springcloud.fegin;

import com.zhilong.springcloud.entity.to.CartItemTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient(name = "order",fallback = OrderFallback.class)
public interface OrderFeginClient {
    @RequestMapping(method = RequestMethod.POST,value = "/v1/seckill/detail")
    ResponseEntity generateSecKillOrder(@RequestBody CartItemTo cartItemTo);
}
