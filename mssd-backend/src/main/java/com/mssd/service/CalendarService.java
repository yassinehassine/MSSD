package com.mssd.service;

import com.mssd.dto.CalendarDto;
import com.mssd.dto.CalendarRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarService {
    List<CalendarDto> getAllCalendars();
    CalendarDto getCalendarById(Long id);
    List<CalendarDto> getAvailableCalendars();
    List<CalendarDto> getCalendarsByDateRange(LocalDateTime start, LocalDateTime end);
    List<CalendarDto> getCalendarsByLocation(String location);
    CalendarDto createCalendar(CalendarRequestDto dto);
    CalendarDto updateCalendar(Long id, CalendarRequestDto dto);
    void deleteCalendar(Long id);
    void updateCalendarCapacity(Long calendarId);
    CalendarDto joinEvent(Long id);
    List<CalendarDto> getUpcomingEventsNext2Weeks();
} 