package com.shalom.shipping_status.mapper;

import com.shalom.shipping_status.document.ShipStatusDocument;
import com.shalom.shipping_status.model.dto.NotificationDto;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;

@Component
public record NotificationMapper() {
    public NotificationDto mapper(Tuple2<ShipStatusDocument, String> tuple) {
        var shipStatus = tuple.getT1();
        var message = tuple.getT2();

        return NotificationDto.builder()
                .text(message)
                .email(shipStatus.getEmail())
                .subject("PEDIDO ".concat(shipStatus.getTrackingNumber()))
                .build();
    }
}
