package com.shalom.shipping_status.service.shalom;

import com.shalom.shipping_status.document.ShipStatusDocument;
import com.shalom.shipping_status.model.exception.BusinessException;
import com.shalom.shipping_status.model.request.SetEmailShipShalomRequest;
import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.model.response.SearchShalomResponse;
import com.shalom.shipping_status.model.response.api.APISearchShalomResponse;
import com.shalom.shipping_status.model.response.api.APIStateShalomResponse;
import com.shalom.shipping_status.rest.IApiShalomRest;
import com.shalom.shipping_status.service.ship_status.IShipStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

import static reactor.core.publisher.Mono.error;

@Service
@RequiredArgsConstructor
public class ShalomService implements IShalomService {
    private final IApiShalomRest apiShalomRest;
    private final IShipStatusService shipStatusService;

    @Override
    public Mono<SearchShalomResponse> getPackage(ShipShalomRequest request) {
        return this.apiShalomRest
                .search(request)
                .filter(APISearchShalomResponse::getSuccess)
                .switchIfEmpty(error(new BusinessException("Elemento no encontrado.")))
                .map(APISearchShalomResponse::toResponse)
                .flatMap(this::setTrackingStates)
                .flatMap(this::verifyCurrentTracking);
    }

    @Override
    public Mono<Map<String, String>> setEmail(SetEmailShipShalomRequest request) {
        return this.shipStatusService
                .setEmailByTrackingNumber(request)
                .map(response -> Map.of("message", "ok"));
    }

    private Mono<SearchShalomResponse> setTrackingStates(SearchShalomResponse response) {
        return this.apiShalomRest
                .states(response.getOse_id().toString())
                .filter(APIStateShalomResponse::getSuccess)
                .doOnNext(c -> c.setDataOfStates(response))
                .thenReturn(response);
    }

    private Mono<SearchShalomResponse> verifyCurrentTracking(SearchShalomResponse response) {
        return this.shipStatusService
                .verifyCurrentTracking(response)
                .map(ShipStatusDocument::getTracking)
                .doOnNext(response::setTracking)
                .thenReturn(response);
    }
}
