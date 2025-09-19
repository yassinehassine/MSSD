package com.mssd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDto {
    private Long id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "Formation ID is required")
    @Positive(message = "Formation ID must be positive")
    private Long formationId;
    
    private String formationName;
    private String formationCategory;
    private String category; // Portfolio's own category field
    private String imageUrl;
    private String companyLogo;
    private String clientName;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectDate;
    
    private String projectUrl;
    
    @Builder.Default
    private Boolean active = true;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
