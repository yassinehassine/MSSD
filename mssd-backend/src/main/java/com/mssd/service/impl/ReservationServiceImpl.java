package com.mssd.service.impl;

import com.mssd.dto.ReservationDto;
import com.mssd.dto.ReservationRequestDto;
import com.mssd.mapper.ReservationMapper;
import com.mssd.model.Reservation;
import com.mssd.repository.ReservationRepository;
import com.mssd.service.ReservationService;
import com.mssd.service.CalendarService;
import com.mssd.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final CalendarService calendarService;

    @Override
    public List<ReservationDto> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationDto getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        return reservationMapper.toDto(reservation);
    }

    @Override
    public List<ReservationDto> getReservationsByCalendarId(Long calendarId) {
        return reservationRepository.findByCalendarId(calendarId).stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDto> getReservationsByVisitorEmail(String visitorEmail) {
        return reservationRepository.findByVisitorEmail(visitorEmail).stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDto> getReservationsByStatus(String status) {
        try {
            Reservation.ReservationStatus statusEnum = Reservation.ReservationStatus.valueOf(status.toUpperCase());
            return reservationRepository.findByStatus(statusEnum).stream()
                    .map(reservationMapper::toDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Valid statuses are: PENDING, CONFIRMED, CANCELLED, COMPLETED");
        }
    }

    @Override
    public ReservationDto createReservation(ReservationRequestDto dto) {
        if (!isReservationPossible(dto.getCalendarId(), dto.getNumberOfPeople())) {
            throw new IllegalArgumentException("Reservation not possible. Not enough available spots or event is full.");
        }
        
        if (reservationRepository.existsByVisitorEmailAndCalendarId(dto.getVisitorEmail(), dto.getCalendarId())) {
            throw new IllegalArgumentException("Visitor already has a reservation for this event");
        }
        
        Reservation reservation = reservationMapper.toEntity(dto);
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED); // Auto-confirm
        Reservation savedReservation = reservationRepository.save(reservation);
        
        // Update calendar capacity
        calendarService.updateCalendarCapacity(dto.getCalendarId());
        
        return reservationMapper.toDto(savedReservation);
    }

    @Override
    public ReservationDto updateReservation(Long id, ReservationRequestDto dto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        
        reservationMapper.updateEntity(reservation, dto);
        Reservation savedReservation = reservationRepository.save(reservation);
        
        // Update calendar capacity
        calendarService.updateCalendarCapacity(savedReservation.getCalendar().getId());
        
        return reservationMapper.toDto(savedReservation);
    }

    @Override
    public ReservationDto confirmReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        Reservation savedReservation = reservationRepository.save(reservation);
        
        // Update calendar capacity
        calendarService.updateCalendarCapacity(savedReservation.getCalendar().getId());
        
        return reservationMapper.toDto(savedReservation);
    }

    @Override
    public ReservationDto cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        
        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        Reservation savedReservation = reservationRepository.save(reservation);
        
        // Update calendar capacity
        calendarService.updateCalendarCapacity(savedReservation.getCalendar().getId());
        
        return reservationMapper.toDto(savedReservation);
    }

    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        
        Long calendarId = reservation.getCalendar().getId();
        reservationRepository.deleteById(id);
        
        // Update calendar capacity
        calendarService.updateCalendarCapacity(calendarId);
    }

    @Override
    public boolean isReservationPossible(Long calendarId, int numberOfPeople) {
        try {
            var calendarDto = calendarService.getCalendarById(calendarId);
            return calendarDto.getStatus() == com.mssd.model.Calendar.CalendarStatus.AVAILABLE &&
                   calendarDto.getAvailableSpots() >= numberOfPeople;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }
} 