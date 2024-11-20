package com.shalom.shipping_status.document;

import com.shalom.shipping_status.model.dto.TrackingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipStatusDocument {
    @Id
    private String trackingNumber;
    private String code;
    private boolean complete = false;
    private TrackingDto lastDetectedTracking;
    private List<TrackingDto> tracking = new ArrayList<>();

    public ShipStatusDocument(String trackingNumber,  String code, List<TrackingDto> tracking) {
        this.tracking = tracking;
        this.code = code;
        this.trackingNumber = trackingNumber;
    }
}
