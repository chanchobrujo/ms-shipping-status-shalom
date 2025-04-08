package com.shalom.shipping_status.common.constants;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ofPattern;

public record DateConstants() {
    public static final ZoneId ZONE_ID = ZoneId.of("America/Lima");
    public static final DateTimeFormatter FORMATTER = ofPattern("EEEE, dd MMMM, yyyy ', a las' HH 'con' MM 'minutos'", new Locale("es", "ES"));

}
