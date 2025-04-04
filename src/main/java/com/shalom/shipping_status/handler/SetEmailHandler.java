package com.shalom.shipping_status.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalom.shipping_status.model.request.SetEmailShipShalomRequest;
import com.shalom.shipping_status.service.SetEmailService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public record SetEmailHandler(
        ObjectMapper objectMapper,
        SetEmailService setEmailService) {

    public Mono<ServerResponse> setEmail(ServerRequest request) {
        return request.bodyToMono(SetEmailShipShalomRequest.class)
                .flatMap(this.setEmailService::setEmail)
                .flatMap(response -> ok().bodyValue(response));
    }
}
