package com.shalom.shipping_status.service;

import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.model.response.SearchShalomResponse;
import reactor.core.publisher.Mono;

public interface RetrieveStatusService {
    Mono<SearchShalomResponse> retrieve(ShipShalomRequest request);
}
