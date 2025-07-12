package com.mssd.service.impl;

import com.mssd.dto.FormationBookingDto;
import com.mssd.dto.FormationBookingRequestDto;
import com.mssd.exception.ResourceNotFoundException;
import com.mssd.mapper.FormationBookingMapper;
import com.mssd.model.Formation;
import com.mssd.model.FormationBooking;
import com.mssd.repository.FormationBookingRepository;
import com.mssd.repository.FormationRepository;
import com.mssd.service.FormationBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FormationBookingServiceImpl implements FormationBookingService {
    private final FormationBookingRepository bookingRepository;
    private final FormationRepository formationRepository;
    private final FormationBookingMapper bookingMapper;

    @Override
    public FormationBookingDto createBooking(FormationBookingRequestDto dto) {
        Formation formation = formationRepository.findById(dto.getFormationId())
                .orElseThrow(() -> new ResourceNotFoundException("Formation not found with id: " + dto.getFormationId()));
        FormationBooking booking = bookingMapper.toEntity(dto, formation);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public java.util.List<FormationBookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
            .map(bookingMapper::toDto)
            .toList();
    }
} 