package com.shalom.shipping_status.strategy.step_message;

import com.shalom.shipping_status.model.dto.TrackingDto;

import java.util.List;

public interface StepMessageStrategy {
    boolean support(List<TrackingDto> trackings);

    void buildMessages(List<TrackingDto> trackings);
}
