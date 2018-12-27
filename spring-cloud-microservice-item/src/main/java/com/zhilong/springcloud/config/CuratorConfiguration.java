package com.zhilong.springcloud.config;

import com.zhilong.springcloud.utils.ZKDistributeLockUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CuratorConfiguration {

    private String namespace = "zk-curator-connector";

    @Value("${curator.client.retry.count}")
    private int retryCount;

    @Value("${curator.client.retry.sleepMsBetweenRetries}")
    private int sleepMsBetweenRetries;

    @Value("${curator.client.connectString}")
    private String connectString;

    @Value("${curator.client.sessionTimeoutMs}")
    private int sessionTimeoutMs;

    @Value("${curator.client.connectionTimeoutMs}")
    private int connectionTimeoutMs;

    @Bean
    public RetryNTimes retryNTimes(){
        return new RetryNTimes(this.retryCount, this.sleepMsBetweenRetries);
    }

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework(RetryNTimes retryNTimes){
        return CuratorFrameworkFactory.newClient(
                this.connectString,
                this.connectionTimeoutMs,
                this.sessionTimeoutMs,
                retryNTimes
        );
    }

    @Bean(initMethod = "init")
    public ZKDistributeLockUtils zkDistributeLockUtils(CuratorFramework curatorFramework){
        return new ZKDistributeLockUtils(namespace, curatorFramework);
    }
}
