package com.mssd.dto;

import com.mssd.model.Formation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormationDto {
    private Long id;
    private String title;
    private String slug;
    private String description;
    private String category;
    private BigDecimal price;
    private String duration;
    private String imageUrl;
    private Formation.Level level;
    private boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ThemeDto theme;
} 