package com.shalom.shipping_status.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DefaultMessageFlowEnum {
    TRACKING_COMPLETE, TRACKING_NO_COMPLETE;
}
