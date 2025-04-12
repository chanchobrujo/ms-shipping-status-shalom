package com.shalom.shipping_status.strategy.send_message;

import com.shalom.shipping_status.document.ShipStatusDocument;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

import static reactor.core.publisher.Flux.fromIterable;

@Component
public record SendMessageComposite(List<SendMessageStrategy> sendMessageStrategies) {

    public Mono<Void> retrieve(Tuple2<ShipStatusDocument, String> tuple) {
        return fromIterable(this.sendMessageStrategies)
                .flatMap(strategy -> strategy.send(tuple))
                .then();
    }
}
