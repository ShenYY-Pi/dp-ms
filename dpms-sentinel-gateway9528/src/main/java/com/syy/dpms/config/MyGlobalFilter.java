package com.syy.dpms.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class MyGlobalFilter implements GlobalFilter, Ordered {

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

        // 执行下一个过滤器
        return chain.filter(exchange);
    }

    private boolean isWhiteListedPath(String path) {
        return WHITE_LIST.stream().anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }
}
