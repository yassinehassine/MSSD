package com.mssd.repository;

import com.mssd.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    List<Reservation> findByCalendarId(Long calendarId);
    
    List<Reservation> findByVisitorEmail(String visitorEmail);
    
    List<Reservation> findByStatus(Reservation.ReservationStatus status);
    
    List<Reservation> findByReservationDateBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT r FROM Reservation r WHERE r.calendar.id = :calendarId AND r.status = 'CONFIRMED'")
    List<Reservation> findConfirmedReservationsByCalendarId(@Param("calendarId") Long calendarId);
    
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.calendar.id = :calendarId AND r.status = 'CONFIRMED'")
    int countConfirmedReservationsByCalendarId(@Param("calendarId") Long calendarId);
    
    boolean existsByVisitorEmailAndCalendarId(String visitorEmail, Long calendarId);
} 