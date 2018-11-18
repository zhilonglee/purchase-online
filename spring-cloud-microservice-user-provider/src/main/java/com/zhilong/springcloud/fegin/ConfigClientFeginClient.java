package com.zhilong.springcloud.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
//@FeignClient(name = "config-client", fallback = ConfigClientFallback.class)
@FeignClient(name = "config-client", fallbackFactory = ConfigClientFallbackFactory.class)
public interface ConfigClientFeginClient {
    @RequestMapping(value = "/profile", consumes = "application/json")
    public String getProfile();
}
