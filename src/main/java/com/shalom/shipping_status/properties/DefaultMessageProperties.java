package com.shalom.shipping_status.properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalom.shipping_status.common.enums.DefaultMessageFlowEnum;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@ToString
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "configuration")
public class DefaultMessageProperties {
    private final ObjectMapper objectMapper;
    private DefaultMessageDto defaultMessages;

    public void setDefaultMessagesConfig(String defaultMessagesConfig) throws JsonProcessingException {
        this.defaultMessages = this.objectMapper.readValue(defaultMessagesConfig, new TypeReference<>() {
        });
    }

    @Getter
    @Setter
    @Builder
    public static class DefaultMessageDto {
        private String firstPart;
        private Map<DefaultMessageFlowEnum, String> flows;
    }
}
