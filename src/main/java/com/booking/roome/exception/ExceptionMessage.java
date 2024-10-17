package com.booking.roome.exception;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ExceptionMessage extends StatusMessage {

    private String message;

    public ExceptionMessage(int code, String status, String message, String timestamp) {
        super(code, status, timestamp);
        this.message = message;
    }

}
