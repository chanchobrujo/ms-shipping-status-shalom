package com.yape.shipping_status.model.response.api;

import com.yape.shipping_status.model.dto.TrackingDto;
import com.yape.shipping_status.model.response.SearchShalomResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@Data
public class APIStateShalomResponse {
    private Boolean success;
    private String message;
    private Map<String, APIStateItemShalomResponse> data =  new HashMap<>();

    @Data
    public static class APIStateItemShalomResponse {
        private String fecha;
        private String carguero;
        private Boolean completo;
        private List<String> cargueros = new ArrayList<>();
        private Map<String, String> cliente =new HashMap<>();

        public TrackingDto toDto() {
            TrackingDto rsp = new TrackingDto();
            ofNullable(this.getCarguero())
                    .ifPresent(rsp::setTruck);
            ofNullable(this.getFecha())
                    .map(s -> s.replace(" ","T"))
                    .map(LocalDateTime::parse)
                    .map(LocalDateTime::toString)
                    .ifPresent(rsp::setDate);
            return rsp;
        }
    }

    public void setDataOfStates(SearchShalomResponse response) {
        ofNullable(this.getData())
                .filter(d -> !d.isEmpty())
                .ifPresent(data -> {
                    if (data.containsKey("destino") && nonNull(data.get("destino"))) {
                        response.setCompleto(data.get("destino").getCompleto());
                    }
                    if (data.containsKey("transito") && nonNull(data.get("transito"))) {
                        var value = data.get("transito").toDto();
                        response.getTracking().add(value);
                    }
                });
    }
}
