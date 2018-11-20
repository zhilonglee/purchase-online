package com.zhilong.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.integration.config.EnableIntegration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableIntegration
@EnableHystrix
@EnableSwagger2
@SpringBootApplication
@EnableDiscoveryClient
public class SpringCloudMircserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudMircserviceApplication.class, args);
    }
}
