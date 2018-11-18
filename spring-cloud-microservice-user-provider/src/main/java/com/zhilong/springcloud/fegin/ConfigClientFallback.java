package com.zhilong.springcloud.fegin;

import org.springframework.stereotype.Component;

@Component
public class ConfigClientFallback implements ConfigClientFeginClient {
    @Override
    public String getProfile() {
        return "No Profile Setting";
    }
}
