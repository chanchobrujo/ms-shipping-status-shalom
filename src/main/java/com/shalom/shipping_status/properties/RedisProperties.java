package com.shalom.shipping_status.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.redis-listener")
public class RedisProperties {
    private String uri;
    private String key;
    private String group;
}
