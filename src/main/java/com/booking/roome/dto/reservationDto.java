package com.booking.roome.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class reservationDto {
    private int hotel_id;
    private int user_id;
    private int room_number;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
}
