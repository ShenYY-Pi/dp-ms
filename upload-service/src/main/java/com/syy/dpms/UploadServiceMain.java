package com.syy.dpms;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UploadServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(UploadServiceMain.class,args);
    }
}