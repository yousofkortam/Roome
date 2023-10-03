package com.booking.roome.mapper.Impl;

import com.booking.roome.dto.hotelDto;
import com.booking.roome.mapper.HotelMapper;
import com.booking.roome.model.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelMapperImpl implements HotelMapper {
    @Override
    public Hotel toEntity(hotelDto dto) {
        return Hotel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .rate(dto.getRate())
                .price(dto.getPrice())
                .numberRooms(dto.getNumberRooms())
                .build();
    }
}
