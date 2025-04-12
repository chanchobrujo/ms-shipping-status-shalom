package com.shalom.shipping_status.strategy.step_message;

import com.shalom.shipping_status.model.dto.TrackingDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public record StepMessageComposite(List<StepMessageStrategy> stepMessageStrategies) {
    public Mono<List<TrackingDto>> buildStepMessages(List<TrackingDto> trackings) {
        if (trackings.isEmpty()) return Mono.just(trackings);
        return Flux.fromIterable(this.stepMessageStrategies)
                .filter(strategy -> strategy.support(trackings))
                .next()
                .map(strategy -> {
                    strategy.buildMessages(trackings);
                    return trackings;
                });
    }
}
