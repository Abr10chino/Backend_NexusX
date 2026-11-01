package com.example.apigateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@Configuration
public class LimiterConfig {

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(
                OptionallyGetClientIp(exchange.getRequest().getRemoteAddress())
        );
    }

    private String OptionallyGetClientIp(InetSocketAddress remoteAddress) {
        if (remoteAddress == null) {
            return "unknown";
        }
        return remoteAddress.getHostString();
    }
}
