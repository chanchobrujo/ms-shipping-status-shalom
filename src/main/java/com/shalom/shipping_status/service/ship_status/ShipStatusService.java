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
                .switchIfEmpty(error(new BusinessException("Correo ya registrado.")))
                .doOnNext(itemSaved -> itemSaved.setEmail(request.getEmail()))
                .flatMap(this.repository::save);
    }

    @Override
    public Mono<ShipStatusDocument> verifyCurrentTracking(ShipShalomRequest request, SearchShalomResponse searchShalomResponse) {
        return this.repository
                .findById(searchShalomResponse.getTrackingNumber())
                .switchIfEmpty(this.repository.save(new ShipStatusDocument(request)))
                .flatMap(item -> {
                    searchShalomResponse.setEmail(item.getEmail());
                    //ORDENAMOS LA LISTA
                    List<TrackingDto> currentTrackingSaved = item.getTracking()
                            .stream()
                            .sorted(comparing(TrackingDto::_date))
                            .distinct()
                            .collect(toList());

                    //AGREGAMOS NUEVOS ELEMENTOS A LA LISTA
                    searchShalomResponse.getTracking()
                            .stream()
                            .filter(t -> !currentTrackingSaved.contains(t))
                            .forEach(currentTrackingSaved::add);
                    //GUARDAMOS LA LISTA
                    item.setTracking(currentTrackingSaved);
                    return this.repository.save(item);
                });
    }
}
