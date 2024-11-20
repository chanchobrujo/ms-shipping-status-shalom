package com.yape.shipping_status.service.shalom;

import com.yape.shipping_status.model.request.ShipShalomRequest;
import com.yape.shipping_status.model.response.SearchShalomResponse;
import reactor.core.publisher.Mono;

public interface IShalomService {
    Mono<SearchShalomResponse> getPackage(ShipShalomRequest request);
}
