package com.mssd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
} 