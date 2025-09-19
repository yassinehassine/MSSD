package com.mssd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "custom_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "company_name", nullable = false)
    private String companyName;
    
    @Column(name = "contact_person", nullable = false)
    private String contactPerson;
    
    @Column(nullable = false)
    private String email;
    
    private String phone;
    
    @Column(nullable = false)
    private String subject;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String details;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal budget;
    
    @Column(name = "preferred_start_date")
    private LocalDate preferredStartDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;
    
    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;
    
    @Column(name = "is_existing_program", nullable = false)
    private boolean isExistingProgram = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formation_id")
    private Formation formation;
    
    @Column(name = "date_submitted", nullable = false)
    private LocalDateTime dateSubmitted;
    
    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;
    
    @PrePersist
    protected void onCreate() {
        dateSubmitted = LocalDateTime.now();
        dateUpdated = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        dateUpdated = LocalDateTime.now();
    }
} 