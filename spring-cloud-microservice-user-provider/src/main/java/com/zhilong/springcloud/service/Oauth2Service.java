package com.zhilong.springcloud.service;

import com.zhilong.springcloud.entity.JsonResult;
import org.springframework.http.HttpEntity;

import java.util.Map;

public interface Oauth2Service {
    public HttpEntity getOauth2TokenViaApiGateWay(Map<String, String> map);

    public JsonResult extendToken(String username);
}
