package com.booking.roome.exception;

import lombok.Data;

@Data
public class ExceptionResponse {
    private String message;
    private int status;

    public ExceptionResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
