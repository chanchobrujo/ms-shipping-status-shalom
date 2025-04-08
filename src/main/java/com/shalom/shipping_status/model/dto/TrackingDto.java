package com.shalom.shipping_status.model.dto;

import com.shalom.shipping_status.common.constants.DateConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.parse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingDto {
    private String date;
    private String truck;
    private String message;

    public LocalDateTime _date() {
        return LocalDateTime.parse(this.date);
    }

    public String formatedDate() {
        return parse(this.getDate())
                .atZone(DateConstants.ZONE_ID)
                .format(DateConstants.FORMATTER);
    }
}
