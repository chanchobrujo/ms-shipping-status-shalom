package com.shalom.shipping_status.configuration;

import com.shalom.shipping_status.handler.RetrieveStatesHandler;
import com.shalom.shipping_status.handler.SetEmailHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

@Configuration
@RequiredArgsConstructor
public class ShipShalomRouterConfig {

    private static final String BASE_PATH = "/api/ship-shalom";

    @Bean
    public RouterFunction<ServerResponse> routes(RetrieveStatesHandler retrieveStatesHandler, SetEmailHandler setEmailHandler) {
        return RouterFunctions.nest(path(BASE_PATH),
                RouterFunctions.route()
                        .PUT("/set-email", setEmailHandler::setEmail)
                        .POST("/states", retrieveStatesHandler::retrieveState)
                        .build()
        );
    }
}
