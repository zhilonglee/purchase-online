package com.zhilong.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SpringCloudMicroserviceFrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudMicroserviceFrontendApplication.class, args);
    }
}
