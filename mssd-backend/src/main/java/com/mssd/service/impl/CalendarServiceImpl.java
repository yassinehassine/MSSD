package com.mssd.service.impl;

import com.mssd.dto.CalendarDto;
import com.mssd.dto.CalendarRequestDto;
import com.mssd.mapper.CalendarMapper;
import com.mssd.model.Calendar;
import com.mssd.repository.CalendarRepository;
import com.mssd.repository.ReservationRepository;
import com.mssd.service.CalendarService;
import com.mssd.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CalendarServiceImpl implements CalendarService {
    private final CalendarRepository calendarRepository;
    private final ReservationRepository reservationRepository;
    private final CalendarMapper calendarMapper;

    @Override
    public List<CalendarDto> getAllCalendars() {
        return calendarRepository.findAll().stream()
                .map(calendarMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CalendarDto getCalendarById(Long id) {
        Calendar calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar not found with id: " + id));
        return calendarMapper.toDto(calendar);
    }

    @Override
    public List<CalendarDto> getAvailableCalendars() {
        return calendarRepository.findAvailableUpcomingEvents(LocalDateTime.now()).stream()
                .map(calendarMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalendarDto> getCalendarsByDateRange(LocalDateTime start, LocalDateTime end) {
        return calendarRepository.findByStartTimeBetween(start, end).stream()
                .map(calendarMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalendarDto> getCalendarsByLocation(String location) {
        return calendarRepository.findByLocationContainingIgnoreCase(location).stream()
                .map(calendarMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CalendarDto createCalendar(CalendarRequestDto dto) {
        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        
        if (calendarRepository.existsByStartTimeAndLocation(dto.getStartTime(), dto.getLocation())) {
            throw new IllegalArgumentException("Calendar event already exists at this time and location");
        }
        
        Calendar calendar = calendarMapper.toEntity(dto);
        return calendarMapper.toDto(calendarRepository.save(calendar));
    }

    @Override
    public CalendarDto updateCalendar(Long id, CalendarRequestDto dto) {
        Calendar calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar not found with id: " + id));
        
        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        
        calendarMapper.updateEntity(calendar, dto);
        return calendarMapper.toDto(calendarRepository.save(calendar));
    }

    @Override
    public void deleteCalendar(Long id) {
        if (!calendarRepository.existsById(id)) {
            throw new ResourceNotFoundException("Calendar not found with id: " + id);
        }
        calendarRepository.deleteById(id);
    }

    @Override
    public void updateCalendarCapacity(Long calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar not found with id: " + calendarId));
        
        int confirmedReservations = reservationRepository.countConfirmedReservationsByCalendarId(calendarId);
        calendar.setCurrentCapacity(confirmedReservations);
        
        if (confirmedReservations >= calendar.getMaxCapacity()) {
            calendar.setStatus(Calendar.CalendarStatus.FULL);
        } else {
            calendar.setStatus(Calendar.CalendarStatus.AVAILABLE);
        }
        
        calendarRepository.save(calendar);
    }
} 