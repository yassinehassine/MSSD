package com.mssd.service;

import com.mssd.dto.ReservationDto;
import com.mssd.dto.ReservationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    List<ReservationDto> getAllReservations();
    ReservationDto getReservationById(Long id);
    List<ReservationDto> getReservationsByCalendarId(Long calendarId);
    List<ReservationDto> getReservationsByVisitorEmail(String visitorEmail);
    List<ReservationDto> getReservationsByStatus(String status);
    ReservationDto createReservation(ReservationRequestDto dto);
    ReservationDto updateReservation(Long id, ReservationRequestDto dto);
    ReservationDto confirmReservation(Long id);
    ReservationDto cancelReservation(Long id);
    void deleteReservation(Long id);
    boolean isReservationPossible(Long calendarId, int numberOfPeople);
} 