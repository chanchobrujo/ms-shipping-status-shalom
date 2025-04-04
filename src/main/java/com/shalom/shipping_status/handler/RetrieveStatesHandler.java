package com.shalom.shipping_status.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.service.RetrieveStatusService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public record RetrieveStatesHandler(
        ObjectMapper objectMapper,
        RetrieveStatusService retrieveStatusService) {

    public Mono<ServerResponse> retrieveState(ServerRequest request) {
        return request.bodyToMono(ShipShalomRequest.class)
                .flatMap(this.retrieveStatusService::retrieve)
                .flatMap(response -> ok().bodyValue(response));
    }
}
