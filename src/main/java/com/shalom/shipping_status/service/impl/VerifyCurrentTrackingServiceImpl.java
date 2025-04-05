package com.shalom.shipping_status.service.impl;

import com.shalom.shipping_status.document.ShipStatusDocument;
import com.shalom.shipping_status.model.dto.TrackingDto;
import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.model.response.SearchShalomResponse;
import com.shalom.shipping_status.repository.ShipStatusRepository;
import com.shalom.shipping_status.service.VerifyCurrentTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.micrometer.common.util.StringUtils.isEmpty;
import static io.micrometer.common.util.StringUtils.isNotEmpty;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Service
@RequiredArgsConstructor
public class VerifyCurrentTrackingServiceImpl implements VerifyCurrentTrackingService {
    private final ShipStatusRepository shipStatusRepository;

    @Override
    public Mono<SearchShalomResponse> verify(ShipShalomRequest request, SearchShalomResponse response) {
        return this.shipStatusRepository
                .findById(response.getTrackingNumber())
                .switchIfEmpty(this.shipStatusRepository.save(new ShipStatusDocument(request)))
                .flatMap(item -> {
                    response.setEmail(item.getEmail());
                    //ORDENAMOS LA LISTA
                    List<TrackingDto> currentTrackingSaved = item.getTracking()
                            .stream()
                            .sorted(comparing(TrackingDto::_date))
                            .distinct()
                            .collect(toList());

                    //AGREGAMOS NUEVOS ELEMENTOS A LA LISTA
                    response.getTracking()
                            .stream()
                            .filter(t -> !currentTrackingSaved.contains(t))
                            .forEach(currentTrackingSaved::add);
                    //GUARDAMOS LA LISTA
                    item.setTracking(currentTrackingSaved);
                    return this.shipStatusRepository.save(item);
                })
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
