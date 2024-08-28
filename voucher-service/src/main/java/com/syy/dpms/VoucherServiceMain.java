package com.syy.dpms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.syy.dpms.mapper")
@EnableDiscoveryClient
@EnableAsync
public class VoucherServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(VoucherServiceMain.class, args);
    }
}