package com.mssd.repository;

import com.mssd.model.Highlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HighlightRepository extends JpaRepository<Highlight, Long> {
    List<Highlight> findByVisibleTrue();
} 