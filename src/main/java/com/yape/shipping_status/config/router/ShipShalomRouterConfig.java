package com.yape.shipping_status.config.router;

import com.yape.shipping_status.handler.IShalomHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.Builder;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ShipShalomRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(IShalomHandler handler) {
        return route()
                .path("/api/ship-shalom", (Builder builder) -> builder.POST("/states", accept(APPLICATION_JSON), handler::getPackage))
                .build();
    }
}
