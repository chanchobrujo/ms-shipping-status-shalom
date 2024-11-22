package com.shalom.shipping_status.service.shalom;

import com.shalom.shipping_status.model.request.SetEmailShipShalomRequest;
import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.model.response.SearchShalomResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface IShalomService {
    Mono<SearchShalomResponse> getPackage(ShipShalomRequest request);
    Mono<Map<String, String>> setEmail(SetEmailShipShalomRequest request);
}
