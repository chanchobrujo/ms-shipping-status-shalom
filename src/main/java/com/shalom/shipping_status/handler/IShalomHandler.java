package com.shalom.shipping_status.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface IShalomHandler {
    Mono<ServerResponse> getPackage(ServerRequest request);
    Mono<ServerResponse> setEmail(ServerRequest request);
}
