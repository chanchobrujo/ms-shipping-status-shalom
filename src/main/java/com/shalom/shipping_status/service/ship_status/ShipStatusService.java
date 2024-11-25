package com.shalom.shipping_status.service.ship_status;

import com.shalom.shipping_status.document.ShipStatusDocument;
import com.shalom.shipping_status.model.dto.TrackingDto;
import com.shalom.shipping_status.model.exception.BusinessException;
import com.shalom.shipping_status.model.request.SetEmailShipShalomRequest;
import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.model.response.SearchShalomResponse;
import com.shalom.shipping_status.repository.IShipStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.error;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipStatusService implements IShipStatusService {
    private final IShipStatusRepository repository;

    @Override
    public Mono<ShipStatusDocument> setEmailByTrackingNumber(SetEmailShipShalomRequest request) {
        return this.repository.findById(request.getNumber())
                .filter(itemSaved -> isNull(itemSaved.getEmail()) || itemSaved.getEmail().isEmpty())
                .switchIfEmpty(error(new BusinessException("Elemento no encontrado.")))
                .doOnNext(itemSaved -> itemSaved.setEmail(request.getEmail()))
                .flatMap(this.repository::save);
    }

    @Override
    public Mono<ShipStatusDocument> getCurrentTracking(ShipShalomRequest request) {
        return this.repository
                .findById(request.getNumber())
                .switchIfEmpty(this.repository.save(new ShipStatusDocument(request)));
    }

    @Override
    public Mono<ShipStatusDocument> verifyCurrentTracking(SearchShalomResponse searchShalomResponse, List<TrackingDto> trackingToBd) {
        //ORDENAMOS LA LISTA
        var finalList = trackingToBd
                .stream()
                .sorted(comparing(TrackingDto::_date))
                .distinct()
                .collect(toList());

        //AGREGAMOS NUEVOS ELEMENTOS A LA LISTA
        searchShalomResponse.getTracking()
                .stream()
                .filter(item -> !finalList.contains(item))
                .forEach(finalList::add);

        return this.repository
                .findById(searchShalomResponse.getTrackingNumber())
                .flatMap(item -> {
                    //GUARDAMOS LA LISTA
                    item.setTracking(finalList);
                    return this.repository.save(item);
                });
    }
}
