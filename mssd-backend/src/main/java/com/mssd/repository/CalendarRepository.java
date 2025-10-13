package com.mssd.repository;

import com.mssd.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    
    List<Calendar> findByStatus(Calendar.CalendarStatus status);
    
    List<Calendar> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    
    List<Calendar> findByStartTimeAfter(LocalDateTime date);
    
    List<Calendar> findByLocationContainingIgnoreCase(String location);
    
    @Query("SELECT c FROM Calendar c WHERE c.startTime >= :now AND c.status = 'AVAILABLE' ORDER BY c.startTime")
    List<Calendar> findAvailableUpcomingEvents(@Param("now") LocalDateTime now);
    
    @Query("SELECT c FROM Calendar c WHERE c.currentCapacity < c.maxCapacity AND c.status = 'AVAILABLE'")
    List<Calendar> findEventsWithAvailableSpots();
    
    boolean existsByStartTimeAndLocation(LocalDateTime startTime, String location);
} 