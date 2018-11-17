package com.zhilong.springcloud;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
public class SpringCloudMircserviceMonitorApplication {

    /**
     * {@URL http://codecentric.github.io/spring-boot-admin/2.1.0/}
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudMircserviceMonitorApplication.class, args);
    }

}
