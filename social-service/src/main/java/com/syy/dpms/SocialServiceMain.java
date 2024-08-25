package com.syy.dpms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.syy.dpms.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class SocialServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(SocialServiceMain.class,args);
    }
}