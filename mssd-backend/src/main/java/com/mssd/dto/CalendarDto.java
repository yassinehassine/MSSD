package com.mssd.dto;

import com.mssd.model.Calendar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private int maxCapacity;
    private int currentCapacity;
    private Calendar.CalendarStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int availableSpots;
} 