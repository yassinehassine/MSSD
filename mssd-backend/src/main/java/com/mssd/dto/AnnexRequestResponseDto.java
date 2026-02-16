package com.mssd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.mssd.model.AnnexRequest.Modality;
import com.mssd.model.AnnexRequest.RequestStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnexRequestResponseDto {
    private Long id;
    private String companyName;
    private String email;
    private String phone;
    private Long formationId;
    private String formationTitle;
    private Boolean isCustom;
    private String customDescription;
    private Integer numParticipants;
    private Modality modality;
    private String preferredDate;
    private String notes;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}