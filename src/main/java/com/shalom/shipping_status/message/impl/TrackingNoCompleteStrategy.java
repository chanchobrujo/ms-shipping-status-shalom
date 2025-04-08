package com.shalom.shipping_status.message.impl;

import com.shalom.shipping_status.message.MessageDefaultStrategy;
import com.shalom.shipping_status.model.dto.TrackingDto;
import com.shalom.shipping_status.model.response.SearchShalomResponse;
import com.shalom.shipping_status.properties.DefaultMessageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.shalom.shipping_status.common.enums.DefaultMessageFlowEnum.TRACKING_NO_COMPLETE;
import static io.micrometer.common.util.StringUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class TrackingNoCompleteStrategy implements MessageDefaultStrategy {
    private final DefaultMessageProperties properties;

    @Override
    public boolean support(TrackingDto lastTracking, SearchShalomResponse shipStatusResponse) {
        return isNotEmpty(lastTracking.getTruck());
    }

    @Override
    public Mono<String> buildMessage(TrackingDto lastTracking, SearchShalomResponse shipStatusResponse) {
        var defaultMessagesMail = this.properties.getDefaultMessagesMail();
        var messageFirstPart = defaultMessagesMail.getFirstPart()
                .replace("[DESTINATARIO]", shipStatusResponse.getDestinatarioName())
                .replace("[REMITENTE]", shipStatusResponse.getRemitenteName());
        var flowMessage = defaultMessagesMail.getFlows()
                .get(TRACKING_NO_COMPLETE)
                .replace("[LAST_TRACKING_DATE]", lastTracking.formatedDate());
        return Mono.just(messageFirstPart.concat(flowMessage));
    }
}
