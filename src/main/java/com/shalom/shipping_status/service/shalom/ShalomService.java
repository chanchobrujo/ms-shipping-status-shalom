package com.shalom.shipping_status.service.shalom;

import com.shalom.shipping_status.document.ShipStatusDocument;
import com.shalom.shipping_status.model.dto.TrackingDto;
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

import java.util.List;
import java.util.Map;

import static io.micrometer.common.util.StringUtils.isEmpty;
import static io.micrometer.common.util.StringUtils.isNotEmpty;
import static java.util.stream.IntStream.range;
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
                .flatMap(response -> this.verifyCurrentTracking(request, response));
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

    private Mono<SearchShalomResponse> verifyCurrentTracking(ShipShalomRequest request, SearchShalomResponse response) {
        return this.shipStatusService
                .verifyCurrentTracking(request, response)
                .map(ShipStatusDocument::getTracking)
                .map(this::setMessagesIntoTracking)
                .doOnNext(response::setTracking)
                .thenReturn(response);
    }

    private List<TrackingDto> setMessagesIntoTracking(List<TrackingDto> trackings) {
        var size = trackings.size();
        if (!trackings.isEmpty()) {
            if (size == 1) {
                trackings.stream()
                        .findFirst()
                        .ifPresent(t -> t.setMessage(isNotEmpty(t.getTruck()) ? "El ultimo movimiento del camion de embarque fue a las..." : "Su pedido llego a la sede destino."));
            } else {
                var finalIndex = (size - 1);

                trackings.get(0).setMessage("Su pedido partio aproximadamente a las...");

                var trackingFinal = trackings.get(finalIndex);
                trackings.get(finalIndex).setMessage(isEmpty(trackingFinal.getTruck()) ? "Su pedido llego a la sede destino." : "El ultimo movimiento del camion de embarque fue a las...");

                if (size > 2) {
                    range(1, (finalIndex))
                            .forEach(i -> trackings.get(i).setMessage("Su pedido fue trasladado a las..."));
                }
            }
        }
        return trackings;
    }
}
