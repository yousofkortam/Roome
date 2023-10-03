package com.booking.roome.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
public class ExceptionRequest extends RuntimeException {
    private HttpStatus status;
    public ExceptionRequest(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
