package com.zhilong.springcloud.config;


import com.zhilong.springcloud.handler.CustomLogoutHandler;
import com.zhilong.springcloud.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    DataSource dataSource;

    @Autowired
    CustomLogoutHandler customLogoutHandler;


    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService(){
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        return manager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/",
                        "/index*",
                        "/register*"
                ,"/footer"
                ,"/active*"
                ,"/resetpassword*"
                ,"/newpassword*"
                        ,"/oauth/token"
                ,"/js/**"
                ,"/img/**"
                ,"/css/**").permitAll()
                .anyRequest().hasAuthority("USER")
                .and()
                .formLogin()
                .loginPage("/login")
                // .loginProcessingUrl("/login")
                .failureForwardUrl("/login?error")
                //.successForwardUrl("/index.html")
                .defaultSuccessUrl("/index.html")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/index.html")
                .addLogoutHandler(customLogoutHandler)
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .and()
                .httpBasic();
    }
}
