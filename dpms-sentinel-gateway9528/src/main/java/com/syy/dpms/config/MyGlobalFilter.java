package com.syy.dpms.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.syy.dpms.dto.UserDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.syy.dpms.utils.RedisConstants.LOGIN_USER_KEY;
import static com.syy.dpms.utils.RedisConstants.LOGIN_USER_TTL;

@Component
@Slf4j
public class MyGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String TOKEN_HEADER = "Authorization"; // Token 放在这个 Header 中
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/shop/**",
            "/voucher/**",
            "/shop-type/**",
            "/upload/**",
            "/blog/hot/**",
            "/user/code/**",
            "/user/login/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/doc.html",
            "/knife4j-ui/**",
            "/v3/**",
            "/doc/**",
            "/docs/**",
            "/swagger/**"
    );

    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        if (isWhiteListedPath(path)) {
            return chain.filter(exchange);
        }

        // 获取 Token
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String token = headers.getFirst(TOKEN_HEADER);

        if (token == null || token.isEmpty()) {
            // Token 不存在，返回未授权
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 基于TOKEN获取redis中的用户
        String key = LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);

        // 判断用户是否存在
        if (userMap.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        // 将查询到的hash数据转为UserDTO
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);

        // 存在，保存用户信息到请求头
        String userInfo = JSONUtil.toJsonStr(userDTO);
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("X-User-Info", userInfo)
                .build();

        // 刷新token有效期
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);

        // 创建新的 ServerWebExchange 对象并执行下一个过滤器
        ServerWebExchange newExchange = exchange.mutate().request(request).build();
        return chain.filter(newExchange);
    }

    private boolean isWhiteListedPath(String path) {
        return WHITE_LIST.stream().anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }
}
