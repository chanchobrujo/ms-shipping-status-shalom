package com.shalom.shipping_status.message;

import com.shalom.shipping_status.model.dto.TrackingDto;
import com.shalom.shipping_status.model.response.SearchShalomResponse;
import reactor.core.publisher.Mono;

public interface MessageDefaultStrategy {
    boolean support(TrackingDto lastTracking, SearchShalomResponse searchShalomResponse);

    Mono<String> buildMessage(TrackingDto lastTracking, SearchShalomResponse searchShalomResponse);
}
