package com.shalom.shipping_status.task;

import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.shalom.shipping_status.document.ShipStatusDocument;
import com.shalom.shipping_status.mapper.NotificationMapper;
import com.shalom.shipping_status.message.MessageDefaultStrategy;
import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.repository.ShipStatusRepository;
import com.shalom.shipping_status.service.RetrieveStatusService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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

    private final NotificationMapper notificationMapper;
    private final ShipStatusRepository shipStatusRepository;
    private final RetrieveStatusService retrieveStatusService;

    private final List<MessageDefaultStrategy> messageDefaultStrategies;

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
                                        .zipWith(this.builderMessage(shipStatus));
                            })
                            .map(this.notificationMapper::mapper)
                            .collectList();
                })
                .doFinally(signal -> this.scheduleNextExecution(executionTime))
                .subscribe();
    }

    private Mono<String> builderMessage(ShipStatusDocument document) {
        var request = new ShipShalomRequest(document.getCode(), document.getTrackingNumber());
        return this.retrieveStatusService
                .retrieve(request)
                .flatMap(shipStatusResponse -> {
                    var trackings = shipStatusResponse.getTracking();
                    if (!trackings.isEmpty()) {
                        var currentTracking = document.getLastDetectedTracking();
                        var tracking = trackings.get(trackings.size() - 1);

                        if (!tracking.equals(currentTracking)) {
                            var msgStrategy = this.messageDefaultStrategies.stream()
                                    .filter(strategy -> strategy.support(tracking, shipStatusResponse))
                                    .map(strategy -> strategy.buildMessage(tracking, shipStatusResponse))
                                    .findFirst()
                                    .orElseGet(Mono::empty);
                            return msgStrategy.flatMap(message -> {
                                document.setLastDetectedTracking(tracking);
                                return this.shipStatusRepository.save(document)
                                        .thenReturn(message);
                            });
                        }
                    }
                    return Mono.empty();
                });
    }
}
