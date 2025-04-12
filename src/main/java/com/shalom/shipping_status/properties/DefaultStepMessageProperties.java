package com.shalom.shipping_status.properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@ToString
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "configuration")
public class DefaultStepMessageProperties {
    private final ObjectMapper objectMapper;
    private DefaultStepMessageDto defaultStepMessages;

    public void setDefaultStepMessagesConfig(String defaultStepMessagesConfig) throws JsonProcessingException {
        this.defaultStepMessages = this.objectMapper.readValue(defaultStepMessagesConfig, new TypeReference<>() {
        });
    }

    @Getter
    @Setter
    @Builder
    public static class DefaultStepMessageDto {
        private String start;
        private Map<String, String> end;
    }
}
