package com.mssd.repository;

import com.mssd.model.CalendarReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalendarReservationRepository extends JpaRepository<CalendarReservation, Long> {

    List<CalendarReservation> findByStatus(CalendarReservation.ReservationStatus status);

    List<CalendarReservation> findByClientEmailIgnoreCase(String email);

    @Query("SELECT cr FROM CalendarReservation cr WHERE cr.eventDate >= :startDate AND cr.eventDate <= :endDate ORDER BY cr.eventDate ASC")
    List<CalendarReservation> findByEventDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT cr FROM CalendarReservation cr WHERE cr.clientName LIKE %:keyword% OR cr.clientEmail LIKE %:keyword% OR cr.eventTitle LIKE %:keyword%")
    List<CalendarReservation> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(cr) FROM CalendarReservation cr WHERE cr.status = :status")
    long countByStatus(@Param("status") CalendarReservation.ReservationStatus status);

    List<CalendarReservation> findAllByOrderByCreatedAtDesc();
}