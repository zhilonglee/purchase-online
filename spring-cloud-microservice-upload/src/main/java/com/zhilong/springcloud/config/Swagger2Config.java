package com.zhilong.springcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Swagger2Config {

    private String basePackage = "com.zhilong.springcloud";
    private String termsOfServiceUrl = "https://github.com/zhilonglee/purchase-online/";
    private String ContactName = "zhilonglee";
    private String ContactUrl = "https://github.com/zhilonglee/";
    private String ContactEmail = "zhilong.li1995@gmail.com";
    private String apiVersion = "1.0";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(this.basePackage))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Upload Provider RESTful APIs")
                .description("Upload Provider RESTful APIs")
                .termsOfServiceUrl(this.termsOfServiceUrl)
                .contact(new Contact(this.ContactName,this.ContactUrl,this.ContactEmail))
                .version(this.apiVersion)
                .build();
    }
}
