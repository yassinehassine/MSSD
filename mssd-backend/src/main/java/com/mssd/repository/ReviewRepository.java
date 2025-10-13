package com.mssd.repository;

import com.mssd.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByFormation_Id(Long formationId);
    @Query("SELECT r FROM Review r JOIN FETCH r.formation")
    List<Review> findAllWithFormation();


}
