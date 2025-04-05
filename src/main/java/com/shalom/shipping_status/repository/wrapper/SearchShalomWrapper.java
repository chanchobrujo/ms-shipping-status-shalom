package com.shalom.shipping_status.repository.wrapper;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Data
public class SearchShalomWrapper {
    private String message;
    private Boolean success;
    private SearchDataShalomWrapper data;

    @Data
    public static class SearchDataShalomWrapper {
        private Long ose_id;
        private Long monto_adicional;

        private String monto;
        private String tipo_pago;
        private String contenido;
        private String estado_pago;
        private String numero_orden;
        private String codigo_orden;
        private String tiempo_llegada;
        private String direccion_entrega;

        private Boolean aereo;
        private Boolean reparto;
        private Boolean entregado;

        private String fecha_emision;
        private String fecha_traslado;

        private SearchVoucherShalomWrapper comprobante;

        private Map<String, Object> origen = new HashMap<>();
        private Map<String, Object> destino = new HashMap<>();
        private Map<String, String> remitente = new HashMap<>();
        private Map<String, String> destinatario = new HashMap<>();

        public String getNamePlace(Map<String, Object> place) {
            if (place.isEmpty()) return "";
            return Stream.of(place.get("nombre"), place.get("departamento"), place.get("provincia"))
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .filter(c -> !c.isEmpty())
                    .reduce((a, b) -> a.concat(", ").concat(b))
                    .orElse("");
        }

        @Data
        public static class SearchVoucherShalomWrapper {
            private Long copId;

            private String tipo;
            private String serie;
            private String numero;
            private String tipoPago;
            private String estadoPago;

            private LocalDate fecha;
            private String hora;
        }
    }
}
