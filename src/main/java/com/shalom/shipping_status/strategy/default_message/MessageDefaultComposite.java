package com.shalom.shipping_status.strategy.default_message;

import com.shalom.shipping_status.document.ShipStatusDocument;
import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.repository.ShipStatusRepository;
import com.shalom.shipping_status.service.RetrieveStatusService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public record MessageDefaultComposite(
        ShipStatusRepository shipStatusRepository,
        RetrieveStatusService retrieveStatusService,
        List<MessageDefaultStrategy> messageDefaultStrategies) {

    public Mono<String> retrieve(ShipStatusDocument document) {
        var request = new ShipShalomRequest(document.getCode(), document.getTrackingNumber());
        return this.retrieveStatusService
                .retrieve(request)
                .flatMap(shipStatusResponse -> {
                    var trackings = shipStatusResponse.getTracking();
                    if (!trackings.isEmpty()) {
                        var currentTracking = document.getLastDetectedTracking();
                        var tracking = trackings.get(trackings.size() - 1);

                        if (!tracking.equals(currentTracking)) {
                            return Flux.fromIterable(this.messageDefaultStrategies)
                                    .filter(strategy -> strategy.support(tracking, shipStatusResponse))
                                    .next()
                                    .map(strategy -> strategy.buildMessage(tracking, shipStatusResponse))
                                    .flatMap(message -> {
                                        document.setLastDetectedTracking(tracking);
                                        return this.shipStatusRepository.save(document)
                                                .thenReturn(message);
                                    });
                        }
                    }
                    return Mono.empty();
                });
    }
}
