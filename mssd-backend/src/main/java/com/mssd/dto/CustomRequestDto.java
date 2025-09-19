package com.mssd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomRequestDto {
    @NotBlank(message = "Company name is required")
    private String companyName;
    
    @NotBlank(message = "Contact person is required")
    private String contactPerson;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    private String phone;
    
    @NotBlank(message = "Subject is required")
    private String subject;
    
    @NotBlank(message = "Details are required")
    private String details;
    
    @Positive(message = "Budget must be positive")
    private BigDecimal budget;
    
    private LocalDate preferredStartDate;
    
    private boolean isExistingProgram = false;
    
    private Long formationId;
} 