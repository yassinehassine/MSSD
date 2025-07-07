package com.mssd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String mission;
    
    @Column(columnDefinition = "TEXT")
    private String vision;
    
    @Column(name = "company_values", columnDefinition = "TEXT")
    private String companyValues;
    
    @Column(name = "who_we_are", columnDefinition = "TEXT")
    private String whoWeAre;
    
    @Column(name = "why_choose_us", columnDefinition = "TEXT")
    private String whyChooseUs;
    
    @Column(name = "meta_description")
    private String metaDescription;
} 