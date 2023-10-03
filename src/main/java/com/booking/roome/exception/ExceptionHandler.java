package com.booking.roome.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {ExceptionRequest.class})
    public ResponseEntity<Object> handleRequestException(ExceptionRequest ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), ex.getStatus().value());
        return new ResponseEntity<>(exceptionResponse, ex.getStatus());
    }
}
