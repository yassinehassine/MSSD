package com.mssd.mapper;

import com.mssd.dto.ReservationDto;
import com.mssd.dto.ReservationRequestDto;
import com.mssd.model.Reservation;
import com.mssd.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationMapper {
    
    private final CalendarRepository calendarRepository;
    
    public ReservationDto toDto(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setCalendarId(reservation.getCalendar().getId());
        dto.setCalendarTitle(reservation.getCalendar().getTitle());
        dto.setVisitorName(reservation.getVisitorName());
        dto.setVisitorEmail(reservation.getVisitorEmail());
        dto.setVisitorPhone(reservation.getVisitorPhone());
        dto.setNumberOfPeople(reservation.getNumberOfPeople());
        dto.setNotes(reservation.getNotes());
        dto.setStatus(reservation.getStatus());
        dto.setReservationDate(reservation.getReservationDate());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());
        
        return dto;
    }
    
    public Reservation toEntity(ReservationRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        Reservation reservation = new Reservation();
        reservation.setCalendar(calendarRepository.findById(dto.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException("Calendar not found with id: " + dto.getCalendarId())));
        reservation.setVisitorName(dto.getVisitorName());
        reservation.setVisitorEmail(dto.getVisitorEmail());
        reservation.setVisitorPhone(dto.getVisitorPhone());
        reservation.setNumberOfPeople(dto.getNumberOfPeople());
        reservation.setNotes(dto.getNotes());
        reservation.setReservationDate(dto.getReservationDate());
        
        return reservation;
    }
    
    public void updateEntity(Reservation reservation, ReservationRequestDto dto) {
        if (reservation == null || dto == null) {
            return;
        }
        
        if (dto.getCalendarId() != null) {
            reservation.setCalendar(calendarRepository.findById(dto.getCalendarId())
                    .orElseThrow(() -> new IllegalArgumentException("Calendar not found with id: " + dto.getCalendarId())));
        }
        reservation.setVisitorName(dto.getVisitorName());
        reservation.setVisitorEmail(dto.getVisitorEmail());
        reservation.setVisitorPhone(dto.getVisitorPhone());
        reservation.setNumberOfPeople(dto.getNumberOfPeople());
        reservation.setNotes(dto.getNotes());
        if (dto.getReservationDate() != null) {
            reservation.setReservationDate(dto.getReservationDate());
        }
    }
} 