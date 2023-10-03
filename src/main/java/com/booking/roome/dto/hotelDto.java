package com.booking.roome.dto;

import lombok.Data;

import java.util.List;

@Data
public class hotelDto {
    private String name;
    private String description;
    private String location;
    private float rate;
    private float price;
    private int admin_id;
    private int numberRooms;
    private List<Integer> facilities;
}
