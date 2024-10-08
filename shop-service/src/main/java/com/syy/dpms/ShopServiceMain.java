package com.syy.dpms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan("com.syy.dpms.mapper")
@EnableDiscoveryClient
public class ShopServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(ShopServiceMain.class, args);
    }
}