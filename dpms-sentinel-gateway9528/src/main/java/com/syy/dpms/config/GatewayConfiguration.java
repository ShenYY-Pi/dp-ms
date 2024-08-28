package com.syy.dpms.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.syy.dpms.dto.Result;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 配置类用于设置 Spring Cloud Gateway 的限流和服务降级功能
 */
@Configuration
public class GatewayConfiguration {

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    /**
     * 构造函数，注入 ViewResolver 和 ServerCodecConfigurer
     *
     * @param viewResolversProvider 提供视图解析器的对象
     * @param serverCodecConfigurer 编解码配置器
     */
    public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider, ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    /**
     * 注册 Sentinel 的限流异常处理器，用于处理限流后的异常情况
     *
     * @return SentinelGatewayBlockExceptionHandler 实例
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    /**
     * 注册 Sentinel 的限流过滤器，进行限流控制
     *
     * @return SentinelGatewayFilter 实例
     */
    @Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    /**
     * 初始化限流规则和限流处理器
     */
    @PostConstruct
    public void doInit() {
        initRules(); // 初始化限流规则
        initBlockHandler(); // 初始化限流处理器
    }

    /**
     * 初始化限流规则
     * 这里可以添加更多的限流规则来限制请求速率
     */
    private void initRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        // 示例规则：对名为 "blog_like" 的路由设置每秒允许 1 次请求
        rules.add(new GatewayFlowRule("blog_like").setCount(1).setIntervalSec(1));
        // 可以在这里添加更多的规则
        GatewayRuleManager.loadRules(rules);
    }

    /**
     * 初始化限流处理器
     * 定义当请求被限流时返回的统一响应
     */
    private void initBlockHandler() {
        BlockRequestHandler handler = (exchange, t) -> {
            // 返回全局统一的限流响应
            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(Result.builder()
                            // 设置失败标识和友好的错误消息
                            .data(null)
                            .success(false)
                            .errorMsg("请求太过频繁，请休息一会~") // 用户友好的错误消息
                            .total(null)
                            .build()));
        };
        // 设置全局限流处理器
        GatewayCallbackManager.setBlockHandler(handler);
    }
}
