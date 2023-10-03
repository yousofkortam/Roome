package com.booking.roome.exception;

import lombok.Data;

@Data
public class ExceptionRequest {
    private String message;
    private int status;

    public ExceptionRequest(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
