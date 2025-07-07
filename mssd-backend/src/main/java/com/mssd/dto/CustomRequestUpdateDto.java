package com.mssd.dto;

import com.mssd.model.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomRequestUpdateDto {
    @NotNull(message = "Status is required")
    private RequestStatus status;
    
    private String adminNotes;
} 