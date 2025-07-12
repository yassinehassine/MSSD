package com.mssd.repository;

import com.mssd.model.FormationBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormationBookingRepository extends JpaRepository<FormationBooking, Long> {
} 