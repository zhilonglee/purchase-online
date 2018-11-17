package com.zhilong.springcloud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /** * @param http * @throws Exception */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * Manually configure this to resolve following errors(By default, Spring Security CSRF is enable)
         *
         * Request execution failure with status code 401; retrying on another server if available
         * 2018-10-15 13:54:33.843  WARN 8992 --- [nfoReplicator-0] com.netflix.discovery.DiscoveryClient    :
         * DiscoveryClient_USER-PROVIDER/ncs-110517fz11.ncs.corp.int-ads:user-provider:8100 - registration failed Cannot execute request on any known server
         */
        http.csrf().disable();
        super.configure(http);
    }
}
