package com.mssd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mssd.dto.ReservationDto;
import com.mssd.dto.ReservationRequestDto;
import com.mssd.model.Reservation;
import com.mssd.service.ReservationService;
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

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    private ReservationDto testReservationDto;
    private ReservationRequestDto testReservationRequestDto;

    @BeforeEach
    void setUp() {
        testReservationDto = new ReservationDto();
        testReservationDto.setId(1L);
        testReservationDto.setCalendarId(1L);
        testReservationDto.setCalendarTitle("Test Event");
        testReservationDto.setVisitorName("John Doe");
        testReservationDto.setVisitorEmail("john@example.com");
        testReservationDto.setVisitorPhone("1234567890");
        testReservationDto.setNumberOfPeople(2);
        testReservationDto.setNotes("Test notes");
        testReservationDto.setStatus(Reservation.ReservationStatus.PENDING);
        testReservationDto.setReservationDate(LocalDateTime.now());

        testReservationRequestDto = new ReservationRequestDto();
        testReservationRequestDto.setCalendarId(1L);
        testReservationRequestDto.setVisitorName("John Doe");
        testReservationRequestDto.setVisitorEmail("john@example.com");
        testReservationRequestDto.setVisitorPhone("1234567890");
        testReservationRequestDto.setNumberOfPeople(2);
        testReservationRequestDto.setNotes("Test notes");
    }

    @Test
    void getAllReservations_ShouldReturnReservationList() throws Exception {
        List<ReservationDto> reservations = Arrays.asList(testReservationDto);
        when(reservationService.getAllReservations()).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].visitorName").value("John Doe"));
    }

    @Test
    void getReservationById_ShouldReturnReservation() throws Exception {
        when(reservationService.getReservationById(1L)).thenReturn(testReservationDto);

        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.visitorName").value("John Doe"));
    }

    @Test
    void getReservationsByCalendarId_ShouldReturnReservations() throws Exception {
        List<ReservationDto> reservations = Arrays.asList(testReservationDto);
        when(reservationService.getReservationsByCalendarId(1L)).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/calendar/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getReservationsByVisitorEmail_ShouldReturnReservations() throws Exception {
        List<ReservationDto> reservations = Arrays.asList(testReservationDto);
        when(reservationService.getReservationsByVisitorEmail("john@example.com")).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/visitor/john@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void createReservation_ShouldReturnCreatedReservation() throws Exception {
        when(reservationService.createReservation(any(ReservationRequestDto.class))).thenReturn(testReservationDto);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testReservationRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.visitorName").value("John Doe"));
    }

    @Test
    void updateReservation_ShouldReturnUpdatedReservation() throws Exception {
        when(reservationService.updateReservation(eq(1L), any(ReservationRequestDto.class))).thenReturn(testReservationDto);

        mockMvc.perform(put("/api/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testReservationRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void confirmReservation_ShouldReturnConfirmedReservation() throws Exception {
        testReservationDto.setStatus(Reservation.ReservationStatus.CONFIRMED);
        when(reservationService.confirmReservation(1L)).thenReturn(testReservationDto);

        mockMvc.perform(put("/api/reservations/1/confirm"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void cancelReservation_ShouldReturnCancelledReservation() throws Exception {
        testReservationDto.setStatus(Reservation.ReservationStatus.CANCELLED);
        when(reservationService.cancelReservation(1L)).thenReturn(testReservationDto);

        mockMvc.perform(put("/api/reservations/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void deleteReservation_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void isReservationPossible_ShouldReturnBoolean() throws Exception {
        when(reservationService.isReservationPossible(1L, 2)).thenReturn(true);

        mockMvc.perform(get("/api/reservations/check")
                        .param("calendarId", "1")
                        .param("numberOfPeople", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
} 