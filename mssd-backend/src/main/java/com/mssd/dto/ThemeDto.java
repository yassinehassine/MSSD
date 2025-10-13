package com.mssd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String iconUrl;
    private boolean active;
    private List<FormationSummaryDto> formations;
}