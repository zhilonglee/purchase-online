package com.zhilong.springcloud.service.impl;

import com.zhilong.springcloud.constant.UserProviderConstant;
import com.zhilong.springcloud.contonst.PurchaseOnlieGlobalConstant;
import com.zhilong.springcloud.entity.JsonResult;
import com.zhilong.springcloud.entity.TokenResponse;
import com.zhilong.springcloud.service.Oauth2Service;
import com.zhilong.springcloud.utils.RedisUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class Oauth2ServiceImpl implements Oauth2Service {

    private final Logger logger = LoggerFactory.getLogger(Oauth2ServiceImpl.class);

    private static final String TOKEN_EXTEND_SUCCESS_MSG = "Oauthor2 Token is extended successfully.";
    private static final String TOKEN_EXTEND_FAILURE_MSG = "Oauthor2 Token is failed to extend.";

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public HttpEntity getOauth2TokenViaApiGateWay(Map<String, String> map) {

        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add(PurchaseOnlieGlobalConstant.OAUTH2_TOKEN_PARAM_USERNAME, map.get(PurchaseOnlieGlobalConstant.SECURITY_PARAM_USERNAME));
        params.add(PurchaseOnlieGlobalConstant.OAUTH2_TOKEN_PARAM_PASSWORD, map.get(PurchaseOnlieGlobalConstant.SECURITY_PARAM_PASSWORD));
        params.add(PurchaseOnlieGlobalConstant.OAUTH2_TOKEN_PARAM_CLIENTID, map.get(PurchaseOnlieGlobalConstant.SECURITY_PARAM_USERNAME));
        params.add(PurchaseOnlieGlobalConstant.OAUTH2_TOKEN_PARAM_CLIENTSECRET, map.get(PurchaseOnlieGlobalConstant.SECURITY_PARAM_PASSWORD));
        params.add(PurchaseOnlieGlobalConstant.OAUTH2_TOKEN_PARAM_GRANTTYPE, PurchaseOnlieGlobalConstant.OAUTH2_TOKEN_GRANTTYPE_PASSWORD);

        TokenResponse tokenResponse = null;
        try {

            String tokenKey = PurchaseOnlieGlobalConstant.OAUTH2_TOKEN_IN_REDIS_PREFIX + map.get(PurchaseOnlieGlobalConstant.SECURITY_PARAM_USERNAME);
            tokenResponse = (TokenResponse)redisUtils.getObj(tokenKey);

            if (tokenResponse == null) {
                HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
                tokenResponse = restTemplate.postForObject(UserProviderConstant.APIGATEWAY_AUTHEN_TOKEN_URL, requestEntity, TokenResponse.class);
                redisUtils.setObj(tokenKey,tokenResponse);
                redisUtils.expire(tokenKey,20,RedisUtils.TIME_TO_MINUTES);
            }

        } catch (Exception e) {
            logger.error("", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        logger.info(tokenResponse.toString());

        return ResponseEntity.ok().body(tokenResponse);
    }

    @Override
    public JsonResult extendToken(String username) {

        String tokenKey = PurchaseOnlieGlobalConstant.OAUTH2_TOKEN_IN_REDIS_PREFIX + username;
        TokenResponse tokenResponse = (TokenResponse)redisUtils.getObj(tokenKey);
        JsonResult jsonResult = null;

        if (tokenResponse != null) {
            redisUtils.expire(tokenKey,20,RedisUtils.TIME_TO_MINUTES);
            jsonResult = JsonResult.ok(TOKEN_EXTEND_SUCCESS_MSG);
        }

        return ((jsonResult == null) ? JsonResult.badRequest(TOKEN_EXTEND_FAILURE_MSG) : jsonResult);
    }
}
