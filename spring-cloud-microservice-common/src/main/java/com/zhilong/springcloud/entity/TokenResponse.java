package com.zhilong.springcloud.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenResponse implements Serializable {
    String access_token;
    String token_type;
    String refresh_token;
    String scope;
}
