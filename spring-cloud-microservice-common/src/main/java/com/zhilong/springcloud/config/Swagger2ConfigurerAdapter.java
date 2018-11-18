package com.zhilong.springcloud.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;

@Data
public class Swagger2ConfigurerAdapter {

    private String basePackage = "com.zhilong.springcloud";
    private String termsOfServiceUrl = "https://github.com/zhilonglee/purchase-online/";
    private String ContactName = "zhilonglee";
    private String ContactUrl = "https://github.com/zhilonglee/";
    private String ContactEmail = "zhilong.li1995@gmail.com";
    private String apiVersion = "1.0";

}
