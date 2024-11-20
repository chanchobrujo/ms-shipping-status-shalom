package com.yape.shipping_status.service.ship_status;

import com.yape.shipping_status.document.ShipStatusDocument;
import com.yape.shipping_status.model.dto.TrackingDto;
import com.yape.shipping_status.model.request.ShipShalomRequest;
import com.yape.shipping_status.model.response.SearchShalomResponse;
import com.yape.shipping_status.repository.IShipStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipStatusService implements IShipStatusService {
    private final IShipStatusRepository repository;

    @Override
    public Mono<ShipStatusDocument> getCurrentTracking(ShipShalomRequest request) {
        return this.repository
                .findById(request.getNumber())
                .switchIfEmpty(this.save(request.getNumber(),request.getCode(), new ArrayList<>()));
    }

    @Override
    public Mono<ShipStatusDocument> verifyCurrentTracking(SearchShalomResponse searchShalomResponse, List<TrackingDto> trackingToBd) {
        searchShalomResponse.getTracking()
                .stream()
                .filter(item -> !trackingToBd.contains(item))
                .forEach(trackingToBd::add);
        return this.save(searchShalomResponse.getTrackingNumber(), searchShalomResponse.getCode(), trackingToBd);
    }

    private Mono<ShipStatusDocument> save(String trackingNumber, String code, List<TrackingDto> trackingToBd) {
        if (!trackingToBd.isEmpty()) {
            trackingToBd = trackingToBd
                    .stream()
                    .sorted(comparing(TrackingDto::_date))
                    .collect(toList());
        }
        return this.repository.save(new ShipStatusDocument(trackingNumber, code, trackingToBd));
    }
}
