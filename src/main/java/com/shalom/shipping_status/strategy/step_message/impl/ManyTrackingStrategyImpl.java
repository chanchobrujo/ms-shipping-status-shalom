package com.shalom.shipping_status.strategy.step_message.impl;

import com.shalom.shipping_status.model.dto.TrackingDto;
import com.shalom.shipping_status.properties.DefaultStepMessageProperties;
import com.shalom.shipping_status.strategy.step_message.StepMessageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManyTrackingStrategyImpl implements StepMessageStrategy {
    private final DefaultStepMessageProperties defaultStepMessageProperties;

    @Override
    public boolean support(List<TrackingDto> trackings) {
        return trackings.size() > 1;
    }

    @Override
    public void buildMessages(List<TrackingDto> trackings) {
        var stepMessages = this.defaultStepMessageProperties.getDefaultStepMessages();

        int lastIndex = trackings.size() - 1;
        trackings.forEach(tracking -> tracking.setMessage(stepMessages.getStart()));

        var trackingFinal = trackings.get(lastIndex);
        trackings.get(lastIndex).setMessage(stepMessages.getEnd().get(trackingFinal.keyTuck()));
    }
}
