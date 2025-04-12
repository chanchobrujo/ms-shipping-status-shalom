package com.shalom.shipping_status.mapper;

import com.shalom.shipping_status.document.ShipStatusDocument;
import com.shalom.shipping_status.model.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;

@Slf4j
@Component
public record NotificationMapper() {
    public NotificationDto mapper(Tuple2<ShipStatusDocument, String> tuple) {
        var shipStatus = tuple.getT1();
        var message = tuple.getT2();
        log.info("-Pedido en sede destino: ".concat(shipStatus.getCode()));
        log.info(Strings.EMPTY);

        return NotificationDto.builder()
                .text(message)
                .email(shipStatus.getEmail())
                .subject("PEDIDO ".concat(shipStatus.getTrackingNumber()))
                .build();
    }
}
