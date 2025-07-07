package com.mssd.mapper;

import com.mssd.dto.CalendarDto;
import com.mssd.dto.CalendarRequestDto;
import com.mssd.model.Calendar;
import org.springframework.stereotype.Component;

@Component
public class CalendarMapper {
    
    public CalendarDto toDto(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        
        CalendarDto dto = new CalendarDto();
        dto.setId(calendar.getId());
        dto.setTitle(calendar.getTitle());
        dto.setDescription(calendar.getDescription());
        dto.setStartTime(calendar.getStartTime());
        dto.setEndTime(calendar.getEndTime());
        dto.setLocation(calendar.getLocation());
        dto.setMaxCapacity(calendar.getMaxCapacity());
        dto.setCurrentCapacity(calendar.getCurrentCapacity());
        dto.setStatus(calendar.getStatus());
        dto.setCreatedAt(calendar.getCreatedAt());
        dto.setUpdatedAt(calendar.getUpdatedAt());
        dto.setAvailableSpots(calendar.getMaxCapacity() - calendar.getCurrentCapacity());
        
        return dto;
    }
    
    public Calendar toEntity(CalendarRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        Calendar calendar = new Calendar();
        calendar.setTitle(dto.getTitle());
        calendar.setDescription(dto.getDescription());
        calendar.setStartTime(dto.getStartTime());
        calendar.setEndTime(dto.getEndTime());
        calendar.setLocation(dto.getLocation());
        calendar.setMaxCapacity(dto.getMaxCapacity());
        calendar.setStatus(dto.getStatus());
        
        return calendar;
    }
    
    public void updateEntity(Calendar calendar, CalendarRequestDto dto) {
        if (calendar == null || dto == null) {
            return;
        }
        
        calendar.setTitle(dto.getTitle());
        calendar.setDescription(dto.getDescription());
        calendar.setStartTime(dto.getStartTime());
        calendar.setEndTime(dto.getEndTime());
        calendar.setLocation(dto.getLocation());
        calendar.setMaxCapacity(dto.getMaxCapacity());
        calendar.setStatus(dto.getStatus());
    }
} 