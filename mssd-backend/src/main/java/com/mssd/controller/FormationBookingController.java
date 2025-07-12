package com.mssd.controller;

import com.mssd.dto.FormationBookingDto;
import com.mssd.dto.FormationBookingRequestDto;
import com.mssd.service.FormationBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formation-bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FormationBookingController {
    private final FormationBookingService bookingService;

    @PostMapping
    public ResponseEntity<FormationBookingDto> createBooking(@Valid @RequestBody FormationBookingRequestDto bookingRequestDto) {
        FormationBookingDto createdBooking = bookingService.createBooking(bookingRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }

    @GetMapping
    public ResponseEntity<List<FormationBookingDto>> getAllBookings() {
        // This assumes you have a service method to fetch all bookings
        List<FormationBookingDto> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
} 