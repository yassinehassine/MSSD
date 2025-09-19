package com.mssd.controller;

import com.mssd.dto.CalendarDto;
import com.mssd.dto.CalendarRequestDto;
import com.mssd.service.CalendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/calendars")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CalendarController {
    private final CalendarService calendarService;

    /**
     * Get all calendar events
     * GET /api/calendars
     */
    @GetMapping
    public ResponseEntity<List<CalendarDto>> getAllCalendars() {
        List<CalendarDto> calendars = calendarService.getAllCalendars();
        return ResponseEntity.ok(calendars);
    }

    /**
     * Get calendar event by ID
     * GET /api/calendars/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CalendarDto> getCalendarById(@PathVariable Long id) {
        CalendarDto calendar = calendarService.getCalendarById(id);
        return ResponseEntity.ok(calendar);
    }

    /**
     * Get available calendar events
     * GET /api/calendars/available
     */
    @GetMapping("/available")
    public ResponseEntity<List<CalendarDto>> getAvailableCalendars() {
        List<CalendarDto> calendars = calendarService.getAvailableCalendars();
        return ResponseEntity.ok(calendars);
    }

    /**
     * Get calendar events by date range
     * GET /api/calendars/range?start=2024-01-01T10:00&end=2024-01-31T18:00
     */
    @GetMapping("/range")
    public ResponseEntity<List<CalendarDto>> getCalendarsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<CalendarDto> calendars = calendarService.getCalendarsByDateRange(start, end);
        return ResponseEntity.ok(calendars);
    }

    /**
     * Get calendar events by location
     * GET /api/calendars/location/{location}
     */
    @GetMapping("/location/{location}")
    public ResponseEntity<List<CalendarDto>> getCalendarsByLocation(@PathVariable String location) {
        List<CalendarDto> calendars = calendarService.getCalendarsByLocation(location);
        return ResponseEntity.ok(calendars);
    }

    /**
     * Create a new calendar event
     * POST /api/calendars
     */
    @PostMapping
    public ResponseEntity<CalendarDto> createCalendar(@Valid @RequestBody CalendarRequestDto calendarRequestDto) {
        CalendarDto createdCalendar = calendarService.createCalendar(calendarRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCalendar);
    }

    /**
     * Update an existing calendar event
     * PUT /api/calendars/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<CalendarDto> updateCalendar(
            @PathVariable Long id,
            @Valid @RequestBody CalendarRequestDto calendarRequestDto) {
        CalendarDto updatedCalendar = calendarService.updateCalendar(id, calendarRequestDto);
        return ResponseEntity.ok(updatedCalendar);
    }

    /**
     * Delete a calendar event
     * DELETE /api/calendars/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable Long id) {
        calendarService.deleteCalendar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Join a calendar event (increment currentCapacity)
     * POST /api/calendars/{id}/join
     */
    @PostMapping("/{id}/join")
    public ResponseEntity<CalendarDto> joinEvent(@PathVariable Long id) {
        CalendarDto updated = calendarService.joinEvent(id);
        return ResponseEntity.ok(updated);
    }

    /**
     * Get upcoming events for the next 2 weeks
     * GET /api/calendars/upcoming-2weeks
     */
    @GetMapping("/upcoming-2weeks")
    public ResponseEntity<List<CalendarDto>> getUpcomingEventsNext2Weeks() {
        List<CalendarDto> events = calendarService.getUpcomingEventsNext2Weeks();
        return ResponseEntity.ok(events);
    }
} 