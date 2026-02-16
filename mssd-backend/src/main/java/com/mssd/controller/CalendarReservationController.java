package com.mssd.controller;

import com.mssd.model.CalendarReservation;
import com.mssd.service.CalendarReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/calendar-reservations")
@CrossOrigin(origins = "*")
public class CalendarReservationController {

    @Autowired
    private CalendarReservationService calendarReservationService;

    @GetMapping
    public ResponseEntity<List<CalendarReservation>> getAllReservations() {
        try {
            List<CalendarReservation> reservations = calendarReservationService.getAllReservations();
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalendarReservation> getReservationById(@PathVariable Long id) {
        try {
            Optional<CalendarReservation> reservation = calendarReservationService.getReservationById(id);
            return reservation.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<CalendarReservation> createReservation(@RequestBody CalendarReservation reservation) {
        try {
            CalendarReservation createdReservation = calendarReservationService.createReservation(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalendarReservation> updateReservation(@PathVariable Long id, @RequestBody CalendarReservation reservationDetails) {
        try {
            CalendarReservation updatedReservation = calendarReservationService.updateReservation(id, reservationDetails);
            return ResponseEntity.ok(updatedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        try {
            calendarReservationService.deleteReservation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CalendarReservation>> getReservationsByStatus(@PathVariable String status) {
        try {
            CalendarReservation.ReservationStatus reservationStatus = CalendarReservation.ReservationStatus.valueOf(status.toUpperCase());
            List<CalendarReservation> reservations = calendarReservationService.getReservationsByStatus(reservationStatus);
            return ResponseEntity.ok(reservations);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<CalendarReservation>> searchReservations(@RequestParam String keyword) {
        try {
            List<CalendarReservation> reservations = calendarReservationService.searchReservations(keyword);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getReservationStatistics() {
        try {
            Map<String, Long> statistics = calendarReservationService.getReservationStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CalendarReservation> updateReservationStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            CalendarReservation.ReservationStatus reservationStatus = CalendarReservation.ReservationStatus.valueOf(status.toUpperCase());
            CalendarReservation updatedReservation = calendarReservationService.updateReservationStatus(id, reservationStatus);
            return ResponseEntity.ok(updatedReservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}