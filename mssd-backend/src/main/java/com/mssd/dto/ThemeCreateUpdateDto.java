package com.mssd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ThemeCreateUpdateDto {
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank
    @Size(min = 2, max = 120)
    private String slug; // Provided explicitly to allow stable URLs

    @Size(max = 1000)
    private String description;

    private String iconUrl;

    private Boolean active = true; // Optional toggle
}