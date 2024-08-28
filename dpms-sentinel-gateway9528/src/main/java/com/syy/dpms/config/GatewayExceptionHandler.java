package com.syy.dpms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 根据异常类型选择响应状态码
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 默认内部服务器错误

        // 设置响应状态码
        response.setStatusCode(status);

        // 设置响应头
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 构建错误响应体
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", throwable.getMessage());

        // 将错误详情转换为 JSON 字符串
        String errorResponse;
        try {
            errorResponse = objectMapper.writeValueAsString(errorDetails);
        } catch (IOException e) {
            errorResponse = "{\"error\":\"Unable to process error details\"}";
        }

        // 写入响应体
        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorResponse.getBytes())));
    }
}