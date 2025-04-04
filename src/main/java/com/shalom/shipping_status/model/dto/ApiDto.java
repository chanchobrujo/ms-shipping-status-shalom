package com.shalom.shipping_status.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class ApiDto {
    private String value;
    private Map<String, String> methods;
}
