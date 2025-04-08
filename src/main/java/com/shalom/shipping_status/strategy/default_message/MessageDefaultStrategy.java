package com.shalom.shipping_status.strategy.default_message;

import com.shalom.shipping_status.model.dto.TrackingDto;
import com.shalom.shipping_status.model.response.SearchShalomResponse;

public interface MessageDefaultStrategy {
    boolean support(TrackingDto lastTracking, SearchShalomResponse searchShalomResponse);

    String buildMessage(TrackingDto lastTracking, SearchShalomResponse searchShalomResponse);
}
