package com.shalom.shipping_status.service.impl;

import com.shalom.shipping_status.document.ShipStatusDocument;
import com.shalom.shipping_status.model.dto.TrackingDto;
import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.model.response.SearchShalomResponse;
import com.shalom.shipping_status.repository.ShipStatusRepository;
import com.shalom.shipping_status.service.VerifyCurrentTrackingService;
import com.shalom.shipping_status.strategy.step_message.StepMessageComposite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class VerifyCurrentTrackingServiceImpl implements VerifyCurrentTrackingService {
    private final ShipStatusRepository shipStatusRepository;
    private final StepMessageComposite stepMessageComposite;

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
                .flatMap(this.stepMessageComposite::buildStepMessages)
                .doOnNext(response::setTracking)
                .thenReturn(response);
    }
}
