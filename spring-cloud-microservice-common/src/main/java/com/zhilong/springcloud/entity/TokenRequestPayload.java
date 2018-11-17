package com.zhilong.springcloud.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenRequestPayload implements Serializable {
    String username;
    String password;
    String grant_type;
    String scope;
    String client_id;
    String client_secret;
}
