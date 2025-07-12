package com.mssd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormationBookingDto {
    private Long id;
    private Long formationId;
    private String name;
    private String email;
    private String phone;
    private String company;
    private LocalDateTime createdAt;
} 