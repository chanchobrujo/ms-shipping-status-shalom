package com.shalom.shipping_status.document;

import com.shalom.shipping_status.model.dto.TrackingDto;
import com.shalom.shipping_status.model.request.ShipShalomRequest;
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
    private String email;
    private String code;
    private boolean complete = false;
    private TrackingDto lastDetectedTracking;
    private List<TrackingDto> tracking = new ArrayList<>();

    public ShipStatusDocument(ShipShalomRequest request) {
        this.code = request.getCode();
        this.trackingNumber = request.getNumber();
    }
}
