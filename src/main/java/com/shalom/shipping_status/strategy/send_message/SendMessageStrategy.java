package com.shalom.shipping_status.strategy.send_message;

import com.shalom.shipping_status.document.ShipStatusDocument;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface SendMessageStrategy {
    Mono<Void> send(Tuple2<ShipStatusDocument, String> tuple);
}
