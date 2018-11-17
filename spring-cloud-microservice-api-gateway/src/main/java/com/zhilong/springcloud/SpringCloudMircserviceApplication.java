package com.zhilong.springcloud;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableSwagger2Doc
@SpringBootApplication
@EnableZuulProxy
public class SpringCloudMircserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudMircserviceApplication.class, args);
    }
}
