package com.mssd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "highlights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Highlight {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    private String subtitle;
    
    @Column(name = "cta_text")
    private String ctaText;
    
    @Column(name = "cta_link")
    private String ctaLink;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(nullable = false)
    private boolean visible = true;
} 