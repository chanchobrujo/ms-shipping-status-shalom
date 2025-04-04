package com.shalom.shipping_status.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalom.shipping_status.model.response.SearchShalomResponse;
import com.shalom.shipping_status.repository.wrapper.SearchShalomWrapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.util.Optional.ofNullable;

@Component
public record SearchShalomMapper(ObjectMapper objectMapper) {

    public SearchShalomResponse toResponse(SearchShalomWrapper searchShalomWrapper) {
        return ofNullable(searchShalomWrapper.getData())
                .map(data -> {
                    SearchShalomResponse response = this.objectMapper.convertValue(data, SearchShalomResponse.class);
                    ofNullable(data.getFecha_traslado())
                            .map(s -> s.replace(" ", "T"))
                            .map(LocalDateTime::parse)
                            .ifPresent(response::setFecha);
                    response.setTrackingNumber(data.getNumero_orden());
                    response.set_destino(data.getNamePlace(data.getDestino()));
                    response.set_origen(data.getNamePlace(data.getOrigen()));
                    return response;
                })
                .orElseThrow();
    }
}
