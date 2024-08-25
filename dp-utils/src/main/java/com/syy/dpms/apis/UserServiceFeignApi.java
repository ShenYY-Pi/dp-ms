package com.syy.dpms.apis;

import com.syy.dpms.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "user-service",fallback = UserServiceFeignApiFallBack.class)
public interface UserServiceFeignApi {

    @GetMapping("/user/feign/{id}")
    Result getUserById(@PathVariable("id") Long id);

    @PostMapping("/user/queryByIds")
    Result queryUsersByIdsWithOrder(@RequestBody List<Long> ids);

    @PostMapping("/user/getByIds")
    Result getUsersByIds(@RequestBody List<Long> ids);
}
