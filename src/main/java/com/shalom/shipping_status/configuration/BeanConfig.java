package com.shalom.shipping_status.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .build();
    }
}
