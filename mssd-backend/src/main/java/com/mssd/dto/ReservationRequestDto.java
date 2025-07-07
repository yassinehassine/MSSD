package com.mssd.dto;

import com.mssd.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {
    @NotNull(message = "Calendar ID is required")
    private Long calendarId;
    
    @NotBlank(message = "Visitor name is required")
    private String visitorName;
    
    @NotBlank(message = "Visitor email is required")
    @Email(message = "Invalid email format")
    private String visitorEmail;
    
    private String visitorPhone;
    
    @NotNull(message = "Number of people is required")
    @Min(value = 1, message = "Number of people must be at least 1")
    @Max(value = 50, message = "Number of people cannot exceed 50")
    private Integer numberOfPeople = 1;
    
    private String notes;
    
    private LocalDateTime reservationDate;
} 