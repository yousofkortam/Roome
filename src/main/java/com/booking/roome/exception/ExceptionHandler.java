package com.booking.roome.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {ExceptionResponse.class})
    public ResponseEntity<Object> handleRequestException(ExceptionResponse ex) {
        ExceptionRequest exceptionRequest = new ExceptionRequest(ex.getMessage(), ex.getStatus().value());
        return new ResponseEntity<>(exceptionRequest, ex.getStatus());
    }
}
