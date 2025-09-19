package com.mssd.mapper;

import com.mssd.dto.CustomRequestDto;
import com.mssd.dto.CustomRequestResponseDto;
import com.mssd.dto.CustomRequestUpdateDto;
import com.mssd.model.CustomRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomRequestMapper {
    
    public CustomRequestResponseDto toResponseDto(CustomRequest customRequest) {
        if (customRequest == null) {
            return null;
        }
        
        CustomRequestResponseDto dto = new CustomRequestResponseDto();
        dto.setId(customRequest.getId());
        dto.setCompanyName(customRequest.getCompanyName());
        dto.setContactPerson(customRequest.getContactPerson());
        dto.setEmail(customRequest.getEmail());
        dto.setPhone(customRequest.getPhone());
        dto.setSubject(customRequest.getSubject());
        dto.setDetails(customRequest.getDetails());
        dto.setBudget(customRequest.getBudget());
        dto.setPreferredStartDate(customRequest.getPreferredStartDate());
        dto.setStatus(customRequest.getStatus());
        dto.setAdminNotes(customRequest.getAdminNotes());
        dto.setDateSubmitted(customRequest.getDateSubmitted());
        dto.setDateUpdated(customRequest.getDateUpdated());
        dto.setExistingProgram(customRequest.isExistingProgram());
        dto.setFormationId(customRequest.getFormation() != null ? customRequest.getFormation().getId() : null);
        dto.setFormationTitle(customRequest.getFormation() != null ? customRequest.getFormation().getTitle() : null);
        
        return dto;
    }
    
    public CustomRequest toEntity(CustomRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        CustomRequest customRequest = new CustomRequest();
        customRequest.setCompanyName(dto.getCompanyName());
        customRequest.setContactPerson(dto.getContactPerson());
        customRequest.setEmail(dto.getEmail());
        customRequest.setPhone(dto.getPhone());
        customRequest.setSubject(dto.getSubject());
        customRequest.setDetails(dto.getDetails());
        customRequest.setBudget(dto.getBudget());
        customRequest.setPreferredStartDate(dto.getPreferredStartDate());
        customRequest.setExistingProgram(dto.isExistingProgram());
        // Note: Formation relationship should be set in the service layer
        
        return customRequest;
    }
    
    public void updateEntity(CustomRequest customRequest, CustomRequestUpdateDto updateDto) {
        if (customRequest == null || updateDto == null) {
            return;
        }
        
        customRequest.setStatus(updateDto.getStatus());
        customRequest.setAdminNotes(updateDto.getAdminNotes());
    }
} 