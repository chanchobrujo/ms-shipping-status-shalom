package com.shalom.shipping_status.task;

import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.shalom.shipping_status.repository.ShipStatusRepository;
import com.shalom.shipping_status.strategy.default_message.MessageDefaultComposite;
import com.shalom.shipping_status.strategy.send_message.SendMessageStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.cronutils.model.CronType.SPRING;
import static com.cronutils.model.definition.CronDefinitionBuilder.instanceDefinitionFor;
import static com.cronutils.model.time.ExecutionTime.forCron;
import static com.shalom.shipping_status.common.constants.DateConstants.ZONE_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatusVerifierTask {
    private final MessageDefaultComposite composite;
    private final ShipStatusRepository shipStatusRepository;
    private final List<SendMessageStrategy> sendMessageStrategies;

    @Value("${configuration.verifyStatusTask}")
    private String cronExpression;

    @PostConstruct
    public void startCronLoop() {
        CronParser parser = new CronParser(instanceDefinitionFor(SPRING));
        var executionTime = forCron(parser.parse(cronExpression));
        this.scheduleNextExecution(executionTime);
    }

    private void scheduleNextExecution(ExecutionTime executionTime) {
        Optional<Duration> delayOpt = executionTime.timeToNextExecution(ZonedDateTime.now(ZONE_ID));
        if (delayOpt.isEmpty()) return;

        Mono.delay(delayOpt.get())
                .flatMap(tick -> {
                    return this.shipStatusRepository.findIncompleteWithEmail()
                            .flatMap(shipStatus -> {
                                return Mono.just(shipStatus)
                                        .zipWith(this.composite.retrieve(shipStatus))
                                        .flatMap(tuple -> {
                                            return Flux.fromIterable(this.sendMessageStrategies)
                                                    .flatMap(strategy -> strategy.send(tuple))
                                                    .then();
                                        });
                            })
                            .collectList();
                })
                .doFinally(signal -> this.scheduleNextExecution(executionTime))
                .subscribe();
    }
}
