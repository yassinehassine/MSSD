package com.mssd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

@Entity
@Table(name = "annex_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnexRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Company name is required")
    @Column(nullable = false)
    private String companyName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    @Column(nullable = false)
    private String email;
    
    private String phone;
    
    // For existing formation selection
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formation_id")
    private Formation formation;
    
    // For custom training requests
    @Column(nullable = false)
    private boolean isCustom = false;
    
    @Column(columnDefinition = "TEXT")
    private String customDescription;
    
    @Min(value = 1, message = "Number of participants must be at least 1")
    @Column(nullable = false)
    private Integer numParticipants;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modality modality;
    
    @Column(nullable = false)
    private String preferredDate;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum Modality {
        IN_PERSON("En présentiel"), 
        REMOTE("À distance"), 
        HYBRID("Hybride");
        
        private final String displayName;
        
        Modality(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum RequestStatus {
        PENDING, APPROVED, REJECTED, IN_PROGRESS, COMPLETED
    }
}