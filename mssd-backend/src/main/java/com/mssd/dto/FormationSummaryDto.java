package com.mssd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.mssd.model.Formation.Level;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormationSummaryDto {
    private Long id;
    private String title;
    private String slug;
    private String description;
    private BigDecimal price;
    private String duration;
    private String imageUrl;
    private Level level;
    private Long themeId;
    private String themeName;
}