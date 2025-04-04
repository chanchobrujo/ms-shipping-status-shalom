package com.shalom.shipping_status.error.exception;

import java.io.Serial;

public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 874387341803198776L;

    public BusinessException(String message) {
        super(message);
    }
}
