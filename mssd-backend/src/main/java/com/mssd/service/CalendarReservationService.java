package com.mssd.service;

import com.mssd.model.Calendar;
import com.mssd.model.CalendarReservation;
import com.mssd.repository.CalendarRepository;
import com.mssd.repository.CalendarReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CalendarReservationService {

    @Autowired
    private CalendarReservationRepository calendarReservationRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    public List<CalendarReservation> getAllReservations() {
        return calendarReservationRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<CalendarReservation> getReservationById(Long id) {
        return calendarReservationRepository.findById(id);
    }

    public CalendarReservation createReservation(CalendarReservation reservation) {
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());
        return calendarReservationRepository.save(reservation);
    }

    public CalendarReservation updateReservation(Long id, CalendarReservation reservationDetails) {
        Optional<CalendarReservation> optionalReservation = calendarReservationRepository.findById(id);
        
        if (optionalReservation.isPresent()) {
            CalendarReservation reservation = optionalReservation.get();
            
            if (reservationDetails.getClientName() != null) {
                reservation.setClientName(reservationDetails.getClientName());
            }
            if (reservationDetails.getClientEmail() != null) {
                reservation.setClientEmail(reservationDetails.getClientEmail());
            }
            if (reservationDetails.getClientPhone() != null) {
                reservation.setClientPhone(reservationDetails.getClientPhone());
            }
            if (reservationDetails.getEventTitle() != null) {
                reservation.setEventTitle(reservationDetails.getEventTitle());
            }
            if (reservationDetails.getEventDescription() != null) {
                reservation.setEventDescription(reservationDetails.getEventDescription());
            }
            if (reservationDetails.getEventDate() != null) {
                reservation.setEventDate(reservationDetails.getEventDate());
            }
            if (reservationDetails.getDuration() != null) {
                reservation.setDuration(reservationDetails.getDuration());
            }
            if (reservationDetails.getLocation() != null) {
                reservation.setLocation(reservationDetails.getLocation());
            }
            if (reservationDetails.getStatus() != null) {
                reservation.setStatus(reservationDetails.getStatus());
            }
            if (reservationDetails.getAdminNotes() != null) {
                reservation.setAdminNotes(reservationDetails.getAdminNotes());
            }
            
            reservation.setUpdatedAt(LocalDateTime.now());
            return calendarReservationRepository.save(reservation);
        }
        
        throw new RuntimeException("Calendar reservation not found with id: " + id);
    }

    public void deleteReservation(Long id) {
        if (calendarReservationRepository.existsById(id)) {
            calendarReservationRepository.deleteById(id);
        } else {
            throw new RuntimeException("Calendar reservation not found with id: " + id);
        }
    }

    public List<CalendarReservation> getReservationsByStatus(CalendarReservation.ReservationStatus status) {
        return calendarReservationRepository.findByStatus(status);
    }

    public List<CalendarReservation> getReservationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return calendarReservationRepository.findByEventDateBetween(startDate, endDate);
    }

    public List<CalendarReservation> searchReservations(String keyword) {
        return calendarReservationRepository.findByKeyword(keyword);
    }

    public Map<String, Long> getReservationStatistics() {
        return Map.of(
                "total", calendarReservationRepository.count(),
                "pending", calendarReservationRepository.countByStatus(CalendarReservation.ReservationStatus.PENDING),
                "confirmed", calendarReservationRepository.countByStatus(CalendarReservation.ReservationStatus.CONFIRMED),
                "cancelled", calendarReservationRepository.countByStatus(CalendarReservation.ReservationStatus.CANCELLED),
                "completed", calendarReservationRepository.countByStatus(CalendarReservation.ReservationStatus.COMPLETED)
        );
    }

    public CalendarReservation updateReservationStatus(Long id, CalendarReservation.ReservationStatus status) {
        Optional<CalendarReservation> optionalReservation = calendarReservationRepository.findById(id);
        
        if (optionalReservation.isPresent()) {
            CalendarReservation reservation = optionalReservation.get();
            CalendarReservation.ReservationStatus oldStatus = reservation.getStatus();
            
            reservation.setStatus(status);
            reservation.setUpdatedAt(LocalDateTime.now());
            CalendarReservation savedReservation = calendarReservationRepository.save(reservation);
            
            // Update calendar capacity when status changes to CONFIRMED
            if (status == CalendarReservation.ReservationStatus.CONFIRMED && oldStatus != CalendarReservation.ReservationStatus.CONFIRMED) {
                if (reservation.getCalendarId() != null) {
                    Optional<Calendar> optionalCalendar = calendarRepository.findById(reservation.getCalendarId());
                    if (optionalCalendar.isPresent()) {
                        Calendar calendar = optionalCalendar.get();
                        if (calendar.getCurrentCapacity() < calendar.getMaxCapacity()) {
                            calendar.setCurrentCapacity(calendar.getCurrentCapacity() + 1);
                            if (calendar.getCurrentCapacity() == calendar.getMaxCapacity()) {
                                calendar.setStatus(Calendar.CalendarStatus.FULL);
                            }
                            calendarRepository.save(calendar);
                        }
                    }
                }
            }
            // Decrement capacity if status changes from CONFIRMED to CANCELLED
            else if (status == CalendarReservation.ReservationStatus.CANCELLED && oldStatus == CalendarReservation.ReservationStatus.CONFIRMED) {
                if (reservation.getCalendarId() != null) {
                    Optional<Calendar> optionalCalendar = calendarRepository.findById(reservation.getCalendarId());
                    if (optionalCalendar.isPresent()) {
                        Calendar calendar = optionalCalendar.get();
                        if (calendar.getCurrentCapacity() > 0) {
                            calendar.setCurrentCapacity(calendar.getCurrentCapacity() - 1);
                            if (calendar.getStatus() == Calendar.CalendarStatus.FULL) {
                                calendar.setStatus(Calendar.CalendarStatus.AVAILABLE);
                            }
                            calendarRepository.save(calendar);
                        }
                    }
                }
            }
            
            return savedReservation;
        }
        
        throw new RuntimeException("Calendar reservation not found with id: " + id);
    }
}