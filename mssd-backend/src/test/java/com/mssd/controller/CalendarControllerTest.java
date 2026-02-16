package com.mssd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mssd.dto.CalendarDto;
import com.mssd.dto.CalendarRequestDto;
import com.mssd.model.Calendar;
import com.mssd.service.CalendarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalendarController.class)
class CalendarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalendarService calendarService;

    @Autowired
    private ObjectMapper objectMapper;

    private CalendarDto testCalendarDto;
    private CalendarRequestDto testCalendarRequestDto;

    @BeforeEach
    void setUp() {
        testCalendarDto = new CalendarDto();
        testCalendarDto.setId(1L);
        testCalendarDto.setTitle("Test Event");
        testCalendarDto.setDescription("Test Description");
        testCalendarDto.setStartTime(LocalDateTime.now().plusDays(1));
        testCalendarDto.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        testCalendarDto.setLocation("Test Location");
        testCalendarDto.setMaxCapacity(50);
        testCalendarDto.setCurrentCapacity(0);
        testCalendarDto.setStatus(Calendar.CalendarStatus.AVAILABLE);
        testCalendarDto.setAvailableSpots(50);

        testCalendarRequestDto = new CalendarRequestDto();
        testCalendarRequestDto.setTitle("Test Event");
        testCalendarRequestDto.setDescription("Test Description");
        testCalendarRequestDto.setStartTime(LocalDateTime.now().plusDays(1));
        testCalendarRequestDto.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        testCalendarRequestDto.setLocation("Test Location");
        testCalendarRequestDto.setMaxCapacity(50);
        testCalendarRequestDto.setStatus(Calendar.CalendarStatus.AVAILABLE);
    }

    @Test
    void getAllCalendars_ShouldReturnCalendarList() throws Exception {
        List<CalendarDto> calendars = Arrays.asList(testCalendarDto);
        when(calendarService.getAllCalendars()).thenReturn(calendars);

        mockMvc.perform(get("/api/calendars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Event"));
    }

    @Test
    void getCalendarById_ShouldReturnCalendar() throws Exception {
        when(calendarService.getCalendarById(1L)).thenReturn(testCalendarDto);

        mockMvc.perform(get("/api/calendars/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Event"));
    }

    @Test
    void getAvailableCalendars_ShouldReturnAvailableCalendars() throws Exception {
        List<CalendarDto> calendars = Arrays.asList(testCalendarDto);
        when(calendarService.getAvailableCalendars()).thenReturn(calendars);

        mockMvc.perform(get("/api/calendars/available"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void createCalendar_ShouldReturnCreatedCalendar() throws Exception {
        when(calendarService.createCalendar(any(CalendarRequestDto.class))).thenReturn(testCalendarDto);

        mockMvc.perform(post("/api/calendars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCalendarRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Event"));
    }

    @Test
    void updateCalendar_ShouldReturnUpdatedCalendar() throws Exception {
        when(calendarService.updateCalendar(eq(1L), any(CalendarRequestDto.class))).thenReturn(testCalendarDto);

        mockMvc.perform(put("/api/calendars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCalendarRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteCalendar_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/calendars/1"))
                .andExpect(status().isNoContent());
    }
} 