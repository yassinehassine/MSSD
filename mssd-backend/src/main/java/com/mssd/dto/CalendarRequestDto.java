package com.mssd.dto;

import com.mssd.model.Calendar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarRequestDto {
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;
    
    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotNull(message = "Maximum capacity is required")
    @Positive(message = "Maximum capacity must be positive")
    private Integer maxCapacity;
    
    private Calendar.CalendarStatus status = Calendar.CalendarStatus.AVAILABLE;
} 