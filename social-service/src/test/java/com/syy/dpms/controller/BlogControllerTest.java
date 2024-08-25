package com.syy.dpms.controller;


import com.syy.dpms.apis.UserServiceFeignApi;
import com.syy.dpms.dto.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogControllerTest {

    @Resource
    private UserServiceFeignApi userServiceFeignApi;

    @Test
    void testFeign(){
        Result user = userServiceFeignApi.getUserById(1L);
        System.out.println(user);
    }

}