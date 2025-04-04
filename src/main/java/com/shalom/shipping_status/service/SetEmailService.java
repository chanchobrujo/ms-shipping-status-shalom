package com.shalom.shipping_status.service;

import com.shalom.shipping_status.model.request.SetEmailShipShalomRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface SetEmailService {
    Mono<Map<String, String>> setEmail(SetEmailShipShalomRequest request);
}
