package com.mssd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThemeRequestDto {
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private boolean published;
}