package com.shalom.shipping_status.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import com.shalom.shipping_status.error.exception.BusinessException;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseErrorHandler {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ResponseErrorHandler.class);
    private final ObjectMapper mapper;

    @Bean
    public WebFilter exceptionHandlingFilter() {
        return (exchange, next) -> next.filter(exchange).onErrorResume(Exception.class, (ex) -> this.handleException(ex, exchange));
    }

    private Mono<Void> handleException(Throwable ex, ServerWebExchange exchange) {
        try {
            log.error("Error capturado: ", ex);
            HttpStatusCode httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
           /*
            if (ex instanceof AppException exception) {
                httpStatus = exception.getHttpStatusCode();
            }
            */

            response.setStatusCode(httpStatus);
            byte[] errorDetailByte = this.mapper.writeValueAsBytes(Map.of("message", ex.getMessage()));
            return response.writeWith(Mono.just(response.bufferFactory().wrap(errorDetailByte)));
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Generated
    public ResponseErrorHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }
}
