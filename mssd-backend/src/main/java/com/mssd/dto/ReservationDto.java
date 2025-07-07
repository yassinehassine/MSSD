package com.mssd.dto;

import com.mssd.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private Long id;
    private Long calendarId;
    private String calendarTitle;
    private String visitorName;
    private String visitorEmail;
    private String visitorPhone;
    private int numberOfPeople;
    private String notes;
    private Reservation.ReservationStatus status;
    private LocalDateTime reservationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 