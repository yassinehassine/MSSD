package com.mssd.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.mssd.model.AnnexRequest.Modality;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnexRequestDto {
    
    @NotBlank(message = "Company name is required")
    private String companyName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    private String email;
    
    private String phone;
    
    // For existing formation selection
    private Long formationId;
    
    // For custom training requests
    @NotNull(message = "Must specify if request is for custom training")
    private Boolean isCustom;
    
    private String customDescription;
    
    @NotNull(message = "Number of participants is required")
    @Min(value = 1, message = "Number of participants must be at least 1")
    private Integer numParticipants;
    
    @NotNull(message = "Training modality is required")
    private Modality modality;
    
    @NotBlank(message = "Preferred date is required")
    private String preferredDate;
    
    private String notes;
}