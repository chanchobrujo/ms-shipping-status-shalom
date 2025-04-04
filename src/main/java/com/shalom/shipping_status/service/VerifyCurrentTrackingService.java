package com.shalom.shipping_status.service;

import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.model.response.SearchShalomResponse;
import reactor.core.publisher.Mono;

public interface VerifyCurrentTrackingService {
    Mono<SearchShalomResponse> verify(ShipShalomRequest request, SearchShalomResponse response);
}
