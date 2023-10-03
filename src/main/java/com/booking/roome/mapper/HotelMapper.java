package com.booking.roome.mapper;

import com.booking.roome.dto.hotelDto;
import com.booking.roome.model.Hotel;

public interface HotelMapper {
    Hotel toEntity(hotelDto dto);
}
