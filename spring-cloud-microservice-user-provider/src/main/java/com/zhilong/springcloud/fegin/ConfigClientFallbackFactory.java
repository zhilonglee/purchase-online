package com.zhilong.springcloud.fegin;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ConfigClientFallbackFactory implements FallbackFactory<ConfigClientFeginClient> {
    @Override
    public ConfigClientFeginClient create(Throwable throwable) {
        return new ConfigClientFeginClient() {
            @Override
            public String getProfile() {
                return "No Profile Setting";
            }
        };
    }
}
