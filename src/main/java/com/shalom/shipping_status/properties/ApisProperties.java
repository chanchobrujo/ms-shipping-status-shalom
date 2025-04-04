package com.shalom.shipping_status.properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalom.shipping_status.model.dto.ApiDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@ToString
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "configuration")
public class ApisProperties {
    private final ObjectMapper objectMapper;
    private final Map<String, ApiDto> apisCredentials;

    public void setApis(Map<String, String> apis) {
        apis.forEach((k, v) -> {
            try {
                this.apisCredentials.put(k, this.objectMapper.readValue(v, new TypeReference<>() {
                }));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
