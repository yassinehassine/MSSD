package com.mssd.mapper;

import com.mssd.dto.FormationBookingDto;
import com.mssd.dto.FormationBookingRequestDto;
import com.mssd.model.Formation;
import com.mssd.model.FormationBooking;
import org.springframework.stereotype.Component;

@Component
public class FormationBookingMapper {
    public FormationBooking toEntity(FormationBookingRequestDto dto, Formation formation) {
        if (dto == null || formation == null) return null;
        FormationBooking booking = new FormationBooking();
        booking.setFormation(formation);
        booking.setName(dto.getName());
        booking.setEmail(dto.getEmail());
        booking.setPhone(dto.getPhone());
        booking.setCompany(dto.getCompany());
        return booking;
    }

    public FormationBookingDto toDto(FormationBooking booking) {
        if (booking == null) return null;
        return new FormationBookingDto(
            booking.getId(),
            booking.getFormation() != null ? booking.getFormation().getId() : null,
            booking.getName(),
            booking.getEmail(),
            booking.getPhone(),
            booking.getCompany(),
            booking.getCreatedAt()
        );
    }
} 