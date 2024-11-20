package com.shalom.shipping_status.handler;

import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.service.shalom.IShalomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@RequiredArgsConstructor
public class ShalomHandler implements IShalomHandler {
    private final IShalomService service;

    @Override
    public Mono<ServerResponse> getPackage(ServerRequest request) {
        return request.bodyToMono(ShipShalomRequest.class)
                .flatMap(this.service::getPackage)
                .flatMap(response -> ok().contentType(APPLICATION_JSON).bodyValue(response));
    }
}
