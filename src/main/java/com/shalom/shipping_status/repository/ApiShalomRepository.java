package com.shalom.shipping_status.repository;

import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.repository.wrapper.SearchShalomWrapper;
import com.shalom.shipping_status.repository.wrapper.StateShalomWrapper;
import reactor.core.publisher.Mono;

public interface ApiShalomRepository {
    Mono<SearchShalomWrapper> search(ShipShalomRequest request);

    Mono<StateShalomWrapper> states(String oseId);
}
