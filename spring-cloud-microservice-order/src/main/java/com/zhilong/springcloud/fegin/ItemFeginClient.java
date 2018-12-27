package com.zhilong.springcloud.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "item",fallback = ItemFallback.class)
public interface ItemFeginClient {
    @RequestMapping(method = RequestMethod.DELETE, value = "/v1/cart/{itemId}")
    ResponseEntity<String> deleteCartItem(@PathVariable("itemId") Long itemId, @RequestParam("num") Integer num,@RequestParam("username") String username);

    @RequestMapping(method = RequestMethod.GET, value = "/v1/{id}")
    ResponseEntity<String> item(@PathVariable("id") Long id);

    @RequestMapping(method = RequestMethod.PUT, value = "/v1/deduct/{id}")
    ResponseEntity<String> itemDeductStockNum(@PathVariable("id") Long id, @RequestParam("num") Integer num);

    @RequestMapping(method = RequestMethod.GET, value = "/v1/limitedspike/{id}")
    ResponseEntity<String> secKillitem(@PathVariable("id") Long id);
}
