package com.shalom.shipping_status.service.ship_status;

import com.shalom.shipping_status.document.ShipStatusDocument;
import com.shalom.shipping_status.model.request.SetEmailShipShalomRequest;
import com.shalom.shipping_status.model.response.SearchShalomResponse;
import reactor.core.publisher.Mono;

public interface IShipStatusService {
    Mono<ShipStatusDocument> setEmailByTrackingNumber(SetEmailShipShalomRequest request);

    Mono<ShipStatusDocument> verifyCurrentTracking(SearchShalomResponse searchShalomResponse);
}
