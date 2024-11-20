package com.yape.shipping_status.service.ship_status;

import com.yape.shipping_status.document.ShipStatusDocument;
import com.yape.shipping_status.model.dto.TrackingDto;
import com.yape.shipping_status.model.request.ShipShalomRequest;
import com.yape.shipping_status.model.response.SearchShalomResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IShipStatusService {
    Mono<ShipStatusDocument> getCurrentTracking(ShipShalomRequest request);
    Mono<ShipStatusDocument> verifyCurrentTracking(SearchShalomResponse searchShalomResponse, List<TrackingDto> trackingToBd);
}
