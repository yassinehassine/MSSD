package com.mssd.controller;

import com.mssd.dto.ReservationDto;
import com.mssd.dto.ReservationRequestDto;
import com.mssd.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservationController {
    private final ReservationService reservationService;

    /**
     * Get all reservations
     * GET /api/reservations
     */
    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        List<ReservationDto> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    /**
     * Get reservation by ID
     * GET /api/reservations/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {
        ReservationDto reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    /**
     * Get reservations by calendar ID
     * GET /api/reservations/calendar/{calendarId}
     */
    @GetMapping("/calendar/{calendarId}")
    public ResponseEntity<List<ReservationDto>> getReservationsByCalendarId(@PathVariable Long calendarId) {
        List<ReservationDto> reservations = reservationService.getReservationsByCalendarId(calendarId);
        return ResponseEntity.ok(reservations);
    }

    /**
     * Get reservations by visitor email
     * GET /api/reservations/visitor/{email}
     */
    @GetMapping("/visitor/{email}")
    public ResponseEntity<List<ReservationDto>> getReservationsByVisitorEmail(@PathVariable String email) {
        List<ReservationDto> reservations = reservationService.getReservationsByVisitorEmail(email);
        return ResponseEntity.ok(reservations);
    }

    /**
     * Get reservations by status
     * GET /api/reservations/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReservationDto>> getReservationsByStatus(@PathVariable String status) {
        List<ReservationDto> reservations = reservationService.getReservationsByStatus(status);
        return ResponseEntity.ok(reservations);
    }

    /**
     * Create a new reservation (Reservation Form)
     * POST /api/reservations
     */
    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(@Valid @RequestBody ReservationRequestDto reservationRequestDto) {
        ReservationDto createdReservation = reservationService.createReservation(reservationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    /**
     * Update an existing reservation
     * PUT /api/reservations/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequestDto reservationRequestDto) {
        ReservationDto updatedReservation = reservationService.updateReservation(id, reservationRequestDto);
        return ResponseEntity.ok(updatedReservation);
    }

    /**
     * Confirm a reservation
     * PUT /api/reservations/{id}/confirm
     */
    @PutMapping("/{id}/confirm")
    public ResponseEntity<ReservationDto> confirmReservation(@PathVariable Long id) {
        ReservationDto confirmedReservation = reservationService.confirmReservation(id);
        return ResponseEntity.ok(confirmedReservation);
    }

    /**
     * Cancel a reservation
     * PUT /api/reservations/{id}/cancel
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ReservationDto> cancelReservation(@PathVariable Long id) {
        ReservationDto cancelledReservation = reservationService.cancelReservation(id);
        return ResponseEntity.ok(cancelledReservation);
    }

    /**
     * Delete a reservation
     * DELETE /api/reservations/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if reservation is possible
     * GET /api/reservations/check?calendarId=1&numberOfPeople=2
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> isReservationPossible(
            @RequestParam Long calendarId,
            @RequestParam int numberOfPeople) {
        boolean isPossible = reservationService.isReservationPossible(calendarId, numberOfPeople);
        return ResponseEntity.ok(isPossible);
    }
} 