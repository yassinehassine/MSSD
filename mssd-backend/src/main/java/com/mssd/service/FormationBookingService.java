package com.mssd.service;

import com.mssd.dto.FormationBookingDto;
import com.mssd.dto.FormationBookingRequestDto;

public interface FormationBookingService {
    FormationBookingDto createBooking(FormationBookingRequestDto dto);
    java.util.List<FormationBookingDto> getAllBookings();
} 