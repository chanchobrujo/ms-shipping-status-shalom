package com.shalom.shipping_status.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
public class NotificationDto {
    private String text;
    private String email;
    private String subject;
    private String numberPhone;

    public Map<String, String> toBodyMessage() {
        Map<String, String> bodyMessage = new HashMap<>();
        bodyMessage.put("text", this.getText());
        bodyMessage.put("email", this.getEmail());
        bodyMessage.put("subject", this.getSubject());
        bodyMessage.put("numberPhone", this.getNumberPhone());
        return bodyMessage;
    }
}
