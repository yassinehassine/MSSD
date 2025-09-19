package com.mssd.dto;

import com.mssd.model.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomRequestResponseDto {
    private Long id;
    private String companyName;
    private String contactPerson;
    private String email;
    private String phone;
    private String subject;
    private String details;
    private BigDecimal budget;
    private LocalDate preferredStartDate;
    private RequestStatus status;
    private String adminNotes;
    private LocalDateTime dateSubmitted;
    private LocalDateTime dateUpdated;
    private boolean isExistingProgram;
    private Long formationId;
    private String formationTitle;
} 