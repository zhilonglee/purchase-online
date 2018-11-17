package com.zhilong.springcloud.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:customizeconfig.properties")
@ConfigurationProperties(prefix="com.zhilong")
public class CustomizeConfig {

    String authorname;
    String authormobile;
    Authorinfo authorinfo;

    @Override
    public String toString() {
        return "CustomizeConfig{" +
                "authorname='" + authorname + '\'' +
                ", authormobile='" + authormobile + '\'' +
                ", authorinfo=" + authorinfo +
                '}';
    }
}
