package com.zhilong.springcloud.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class OauthClientDetails {

    @Id
    @Column(columnDefinition = "varchar(50)")
    private String clientId;
    @Column(columnDefinition = "varchar(255)")
    private String resourceIds;
    @Column(columnDefinition = "varchar(255)")
    private String clientSecret;
    @Column(columnDefinition = "varchar(255)")
    private String scope;
    @Column(columnDefinition = "varchar(255)")
    private String authorizedGrantTypes;
    @Column(columnDefinition = "varchar(255)")
    private String webServerRedirectUri;
    @Column(columnDefinition = "varchar(255)")
    private String authorities;
    private long accessTokenValidity;
    private long refreshTokenValidity;
    @Column(columnDefinition = "varchar(4096)")
    private String additionalInformation;
    private long autoapprove;

}
