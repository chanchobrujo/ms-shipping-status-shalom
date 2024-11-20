package com.shalom.shipping_status.service.shalom;

import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.model.response.SearchShalomResponse;
import reactor.core.publisher.Mono;

public interface IShalomService {
    Mono<SearchShalomResponse> getPackage(ShipShalomRequest request);
}
