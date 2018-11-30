# purchase-online
![logo](https://github.com/zhilonglee/purchase-online/blob/master/spring-cloud-microservice-frontend/src/main/resources/static/img/logo.png)
## Introduce
This project is a micro service project based on Spring Boot, Spring Cloud, Spring Oauth2 and Spring Cloud Netflix.

## Technology stack

* Spring boot - an entry-level micro framework for microservices to simplify the initial setup and development of Spring applications.
* Eureka - cloud service discovery, a rest-based service used to locate services to enable cloud mid-tier service discovery and failover.
* Spring Cloud Config - configuration management toolkit allows you to put your configuration on a remote server, centralize your cluster configuration, and currently support local storage, Git, or Subversion.
* Hystrix - fuse, fault tolerant management tool, aims to control the nodes of services and third-party libraries through circuit breakers, thus providing more robust fault tolerance for delays and failures.
* zuul - zuul is a framework for edge services such as dynamic routing, monitoring, elasticity, and security on cloud platforms.Zuul is the front door for all requests on the back end of Web sites for devices and Netflix streaming apps.
* Spring Cloud bus - event, message Bus, for propagating state changes in clusters (for example, configuration change events), can be used in conjunction with Spring Cloud Config for hot deployment.
* Spring Cloud Sleuth - log collection toolkit, which encapsulates Dapper and log-based tracking as well as Zipkin and HTrace operations, implements a distributed tracking solution for SpringCloud applications.
* Ribbon - offers cloud load balancing. A variety of load balancing strategies are available for service discovery and circuit breaker use.
* Turbine - Turbine is a tool for the aggregate server to send event stream data to monitor the metrics of hystrix under the cluster.
* Spring Cloud stream-spring data flow operation development package, which encapsulates sending and receiving messages with Redis, Rabbit, Kafka, etc.
* Feign - Feign is a declarative, template-based HTTP client.
* Spring Security - Spring Security is a framework that focuses on providing both authentication and authorization to Java applications. Like all Spring projects, the real power of Spring Security is found in how easily it can be extended to meet custom requirements
* Spring Cloud OAuth2 - adds Security controls to your application based on Spring Security and OAuth2 Security toolkits.
* Spring Boot Admin - codecentric’s Spring Boot Admin is a community project to manage and monitor your Spring Boot ® applications. 

## Application Architecture

* spring-cloud-microservice-eureka - Service registration and discovery using eureka server
* spring-cloud-microservice-monitor -  The applications register with our Spring Boot Admin Client (via HTTP) or are discovered using Spring Cloud ® (e.g. Eureka, Consul). The UI is just an AngularJs application on top of the Spring Boot Actuator endpoints.
* spring-cloud-microservice-common - Public call module provided lots of utils and golobal configs 
* spring-cloud-microservice-user-provider - User module,to process user account registration, activation, password reset functions and so on.
* spring-cloud-microservice-api-gateway - using Zuul as cloud gateway.And using spring-security-oauth2 does authentications(User Authentication depends on user-provider module).
* spring-cloud-microservice-frontend - frontend page.
* spring-cloud-microservice-config - config server using spring-cloud-config
* spring-cloud-microservice-config-repo - config repository store the configurations on remote Git 
* spring-cloud-microservice-config-client - fetch configs from config server
* spring-cloud-microservice-item - Item module, to list all kinds of items, and do related operations
* spring-cloud-microservice-upload - Upload file module. Currently, the system has implements transfer file from local to SFTP server
## Note
**1. Springcloud provides a service-registry actuator that can be used to view or change the status of the current service's Registration in the service registry.For eureka, these states are UP,DOWN,OUT_OF_SERVICE,UNKNOWN.** 
> *  Http Method : POST 
> * URL: /actuator/service-registry
> * Request Body: {"status":"DOWN"} 
>>
**2. How to use Spring Integration for uploading files to a remote SFTP server?**
> Quick overview:
>>      Create SFTP Session Factory, i.e. [DefaultSftpSessionFactory]
>>      Create and setup [SftpMessageHandler]
>>      Create [UploadGateway] as an entry point to upload any file

## Swagger UI
* spring-cloud-microservice-user-provider: http://localhost:8100/swagger-ui.html
* spring-cloud-microservice-upload: http://localhost:8110/swagger-ui.html
* spring-cloud-microservice-item: http://localhost:8120/swagger-ui.html
* spring-cloud-microservice-api-gateway: http://localhost:8042/swagger-ui.html (Zuul API-GATEWAY integration with Swagger2)
## Bug Fixes
### _spring-cloud-microservice-eureka_
Currently, nothing bug is recorded.
### _spring-cloud-microservice-monitor_
Currently, nothing bug is recorded.
### _spring-cloud-microservice-common_ 
**1.When other module which depend this module run 'maven clean install'. There are going to show this compiler error 'can not find symbol'**
> Actually, when the spring-boot project is packaged as a jar, it is different from other projects. A layer of boot-inf is added, and all classes are put here
 When relying on a spring-boot project, the following configuration should be added to the dependent project pom:
>> 
       <plugins>
           <plugin>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-maven-plugin</artifactId>
               <configuration>
                   <classifier>exec</classifier>
               </configuration>
           </plugin>
       </plugins>
### _spring-cloud-microservice-user-provider_
Currently, nothing bug is recorded.
### _spring-cloud-microservice-api-gateway_
Currently, nothing bug is recorded.
### _spring-cloud-microservice-frontend_
Currently, nothing bug is recorded.
### _spring-cloud-microservice-config_ 
Currently, nothing bug is recorded.
### _spring-cloud-microservice-config-client_
**1.Even with URL configuration, the config client server still get the configuration from http://localhost:8888**
> It's actually a configuration file priority issue;There is a bootstrap context within SpringCloud, mainly for loading remote configuration, which is inside Config Server.
In other word, loading the configuration is fetching from Config Server, the default context loading order is:  bootstrap.* -> Remote configuration -> application.*;

**Solution:**  move spring.cloud.config.uri: http://localhost:8200/ from application.yml to bootstrap.yml

**2.java.lang.IllegalArgumentException: Could not resolve placeholder 'profile' in value ${profile}**
>The HTTP service has resources in the form:
>* **_/{application}/{profile}[/{label}]_**
>* **_/{application}-{profile}.yml_**
>* **_/{label}/{application}-{profile}.yml_**
>* **_/{application}-{profile}.properties_**
>* **_/{label}/{application}-{profile}.properties_**
>>**Solution:**  Add property spring.cloud.config.name = config-server(There are config-server-{profile}.yml files in URL https://github.com/zhilonglee/purchase-online/spring-cloud-microservice-config-repo/)

**3.How to configure the config-client server to update automatically**
>* add spring-cloud-starter-bus-amqp and spring-boot-starter-actuator dependencies
Using rabbitMQ as Message Oriented Middleware communicant with Git.Dependency actuator is used to explode /actuator/bus-refresh endpoint.
>* Both the server and client services are configured:
management.endpoints.web.exposure.include=bus-refresh
>>But still have issues:
Dispatcher has no subscribers for channel 'config-sever(config-client)-1.springCloudBusOutput
Spring Team says "Greenwich.M1 is not compatible with boot 2.1.0.RELEASE".(Link : https://github.com/spring-cloud/spring-cloud-bus/issues/137)
>>
>**Have no alternative but to ignore this bug.**
### _spring-cloud-microservice-item_ 
**1.Could not parse multipart servlet request; nested exception is java.io.IOException: org.apache.tomcat.util.http.fileupload.FileUploadException: the request was rejected because no multipart boundary was found**
>* Case : when I use fegin client to call other server upload service , I encountered this error.
>* Solution: Using @RequestPart to replace @RequestParam before {MultipartFile} parameter
>* @RequestPart Annotation that can be used to associate the part of a "multipart/form-data" request with a method argument. 
### _spring-cloud-microservice-upload_
Currently, nothing bug is recorded.
## Reference documentation
* [Spring Boot(version 2.1.0.RELEASE)](https://docs.spring.io/spring-boot/docs/2.1.0.RELEASE/reference/htmlsingle/)
* [Spring Security(version 5.1.1.RELEASE)](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/reference/htmlsingle/)
* [Spring Cloud(version Finchley.SR2)](https://cloud.spring.io/spring-cloud-static/Finchley.SR2/)
* [Spring Boot Admin(version 2.1.0)](http://codecentric.github.io/spring-boot-admin/2.1.0/)
