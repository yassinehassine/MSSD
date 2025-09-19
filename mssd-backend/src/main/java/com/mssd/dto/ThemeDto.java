package com.mssd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FormationDto> formations;
}