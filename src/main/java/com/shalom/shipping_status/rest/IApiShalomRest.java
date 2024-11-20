package com.shalom.shipping_status.rest;

import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.model.response.api.APISearchShalomResponse;
import com.shalom.shipping_status.model.response.api.APIStateShalomResponse;
import reactor.core.publisher.Mono;

public interface IApiShalomRest {
    Mono<APISearchShalomResponse> search(ShipShalomRequest request);
    Mono<APIStateShalomResponse> states(String oseId);
}
