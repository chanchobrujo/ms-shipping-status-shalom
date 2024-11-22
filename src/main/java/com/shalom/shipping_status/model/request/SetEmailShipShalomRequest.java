package com.shalom.shipping_status.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetEmailShipShalomRequest {
    private String email;
    private String number;
}
