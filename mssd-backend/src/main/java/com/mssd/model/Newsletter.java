package com.mssd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "newsletters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Newsletter {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "date_subscribed", nullable = false)
    private LocalDateTime dateSubscribed;
    
    @PrePersist
    protected void onCreate() {
        dateSubscribed = LocalDateTime.now();
    }
} 