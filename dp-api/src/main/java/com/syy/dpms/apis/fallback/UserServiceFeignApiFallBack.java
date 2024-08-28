package com.syy.dpms.apis.fallback;

import com.syy.dpms.apis.UserServiceFeignApi;
import com.syy.dpms.dto.Result;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceFeignApiFallBack implements FallbackFactory<UserServiceFeignApi> {

    @Override
    public UserServiceFeignApi create(Throwable cause) {
        return new UserServiceFeignApi() {
            @Override
            public Result getUserById(Long id) {
                return Result.fail("对方服务宕机或不可用，FallBack服务降级o(╥﹏╥)o");
            }

            @Override
            public Result queryUsersByIdsWithOrder(List<Long> ids) {
                return Result.fail("对方服务宕机或不可用，FallBack服务降级o(╥﹏╥)o");
            }

            @Override
            public Result getUsersByIds(List<Long> ids) {
                return Result.fail("对方服务宕机或不可用，FallBack服务降级o(╥﹏╥)o");
            }
        };
    }
}