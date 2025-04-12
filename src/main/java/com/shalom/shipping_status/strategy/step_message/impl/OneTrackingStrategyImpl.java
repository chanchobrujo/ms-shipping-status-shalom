package com.shalom.shipping_status.strategy.step_message.impl;

import com.shalom.shipping_status.model.dto.TrackingDto;
import com.shalom.shipping_status.properties.DefaultStepMessageProperties;
import com.shalom.shipping_status.strategy.step_message.StepMessageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OneTrackingStrategyImpl implements StepMessageStrategy {
    private final DefaultStepMessageProperties defaultStepMessageProperties;

    @Override
    public boolean support(List<TrackingDto> trackings) {
        return trackings.size() == 1;
    }

    @Override
    public void buildMessages(List<TrackingDto> trackings) {
        var stepMessages = this.defaultStepMessageProperties.getDefaultStepMessages();
        trackings.stream()
                .findFirst()
                .ifPresent(tracking -> tracking.setMessage(stepMessages.getEnd().get(tracking.keyTuck())));
    }
}
