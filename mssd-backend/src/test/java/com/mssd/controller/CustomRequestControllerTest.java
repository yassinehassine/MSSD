package com.mssd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mssd.dto.CustomRequestDto;
import com.mssd.dto.CustomRequestResponseDto;
import com.mssd.dto.CustomRequestUpdateDto;
import com.mssd.model.RequestStatus;
import com.mssd.service.CustomRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomRequestController.class)
class CustomRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomRequestService customRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomRequestDto validRequestDto;
    private CustomRequestResponseDto responseDto;
    private CustomRequestUpdateDto updateDto;

    @BeforeEach
    void setUp() {
        validRequestDto = new CustomRequestDto();
        validRequestDto.setCompanyName("Test Company");
        validRequestDto.setContactPerson("John Doe");
        validRequestDto.setEmail("john@testcompany.com");
        validRequestDto.setPhone("1234567890");
        validRequestDto.setSubject("Test Project");
        validRequestDto.setDetails("This is a test project description");
        validRequestDto.setBudget(new BigDecimal("5000.00"));
        validRequestDto.setPreferredStartDate(LocalDate.now().plusDays(30));

        responseDto = new CustomRequestResponseDto();
        responseDto.setId(1L);
        responseDto.setCompanyName("Test Company");
        responseDto.setContactPerson("John Doe");
        responseDto.setEmail("john@testcompany.com");
        responseDto.setPhone("1234567890");
        responseDto.setSubject("Test Project");
        responseDto.setDetails("This is a test project description");
        responseDto.setBudget(new BigDecimal("5000.00"));
        responseDto.setPreferredStartDate(LocalDate.now().plusDays(30));
        responseDto.setStatus(RequestStatus.PENDING);
        responseDto.setDateSubmitted(LocalDateTime.now());
        responseDto.setDateUpdated(LocalDateTime.now());

        updateDto = new CustomRequestUpdateDto();
        updateDto.setStatus(RequestStatus.APPROVED);
        updateDto.setAdminNotes("Approved for development");
    }

    @Test
    void submitCustomRequest_ValidRequest_ReturnsCreated() throws Exception {
        when(customRequestService.submitCustomRequest(any(CustomRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/custom-requests/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.companyName").value("Test Company"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(customRequestService).submitCustomRequest(any(CustomRequestDto.class));
    }

    @Test
    void submitCustomRequest_InvalidRequest_ReturnsBadRequest() throws Exception {
        CustomRequestDto invalidDto = new CustomRequestDto();
        // Missing required fields

        mockMvc.perform(post("/api/custom-requests/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCustomRequests_ReturnsList() throws Exception {
        List<CustomRequestResponseDto> requests = Arrays.asList(responseDto);
        when(customRequestService.getAllCustomRequests()).thenReturn(requests);

        mockMvc.perform(get("/api/custom-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].companyName").value("Test Company"));

        verify(customRequestService).getAllCustomRequests();
    }

    @Test
    void getCustomRequestById_ValidId_ReturnsRequest() throws Exception {
        when(customRequestService.getCustomRequestById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/custom-requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.companyName").value("Test Company"));

        verify(customRequestService).getCustomRequestById(1L);
    }

    @Test
    void updateCustomRequest_ValidRequest_ReturnsOk() throws Exception {
        CustomRequestResponseDto updatedResponse = new CustomRequestResponseDto();
        updatedResponse.setId(1L);
        updatedResponse.setStatus(RequestStatus.APPROVED);
        updatedResponse.setAdminNotes("Approved for development");

        when(customRequestService.updateCustomRequest(eq(1L), any(CustomRequestUpdateDto.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/custom-requests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.adminNotes").value("Approved for development"));

        verify(customRequestService).updateCustomRequest(eq(1L), any(CustomRequestUpdateDto.class));
    }

    @Test
    void deleteCustomRequest_ValidId_ReturnsNoContent() throws Exception {
        doNothing().when(customRequestService).deleteCustomRequest(1L);

        mockMvc.perform(delete("/api/custom-requests/1"))
                .andExpect(status().isNoContent());

        verify(customRequestService).deleteCustomRequest(1L);
    }

    @Test
    void getCustomRequestsByStatus_ValidStatus_ReturnsList() throws Exception {
        List<CustomRequestResponseDto> requests = Arrays.asList(responseDto);
        when(customRequestService.getCustomRequestsByStatus(RequestStatus.PENDING))
                .thenReturn(requests);

        mockMvc.perform(get("/api/custom-requests/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(customRequestService).getCustomRequestsByStatus(RequestStatus.PENDING);
    }

    @Test
    void getCustomRequestsByCompanyName_ValidName_ReturnsList() throws Exception {
        List<CustomRequestResponseDto> requests = Arrays.asList(responseDto);
        when(customRequestService.getCustomRequestsByCompanyName("Test Company"))
                .thenReturn(requests);

        mockMvc.perform(get("/api/custom-requests/company")
                        .param("companyName", "Test Company"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value("Test Company"));

        verify(customRequestService).getCustomRequestsByCompanyName("Test Company");
    }

    @Test
    void getCustomRequestsByEmail_ValidEmail_ReturnsList() throws Exception {
        List<CustomRequestResponseDto> requests = Arrays.asList(responseDto);
        when(customRequestService.getCustomRequestsByEmail("john@testcompany.com"))
                .thenReturn(requests);

        mockMvc.perform(get("/api/custom-requests/email")
                        .param("email", "john@testcompany.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john@testcompany.com"));

        verify(customRequestService).getCustomRequestsByEmail("john@testcompany.com");
    }

    @Test
    void getPendingCustomRequests_ReturnsList() throws Exception {
        List<CustomRequestResponseDto> requests = Arrays.asList(responseDto);
        when(customRequestService.getPendingCustomRequests()).thenReturn(requests);

        mockMvc.perform(get("/api/custom-requests/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(customRequestService).getPendingCustomRequests();
    }

    @Test
    void getCustomRequestsByMinBudget_ValidBudget_ReturnsList() throws Exception {
        List<CustomRequestResponseDto> requests = Arrays.asList(responseDto);
        when(customRequestService.getCustomRequestsByMinBudget(new BigDecimal("1000")))
                .thenReturn(requests);

        mockMvc.perform(get("/api/custom-requests/budget")
                        .param("minBudget", "1000"))
                .andExpect(status().isOk());

        verify(customRequestService).getCustomRequestsByMinBudget(new BigDecimal("1000"));
    }

    @Test
    void getCustomRequestCountByStatus_ValidStatus_ReturnsCount() throws Exception {
        when(customRequestService.getCustomRequestCountByStatus(RequestStatus.PENDING))
                .thenReturn(5L);

        mockMvc.perform(get("/api/custom-requests/stats/count-by-status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.count").value(5));

        verify(customRequestService).getCustomRequestCountByStatus(RequestStatus.PENDING);
    }

    @Test
    void getTotalCustomRequestCount_ReturnsCount() throws Exception {
        when(customRequestService.getTotalCustomRequestCount()).thenReturn(10L);

        mockMvc.perform(get("/api/custom-requests/stats/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(10));

        verify(customRequestService).getTotalCustomRequestCount();
    }

    @Test
    void getCustomRequestSummary_ReturnsSummary() throws Exception {
        when(customRequestService.getTotalCustomRequestCount()).thenReturn(10L);
        when(customRequestService.getCustomRequestCountByStatus(RequestStatus.PENDING)).thenReturn(3L);
        when(customRequestService.getCustomRequestCountByStatus(RequestStatus.APPROVED)).thenReturn(2L);
        when(customRequestService.getCustomRequestCountByStatus(RequestStatus.REJECTED)).thenReturn(1L);
        when(customRequestService.getCustomRequestCountByStatus(RequestStatus.IN_PROGRESS)).thenReturn(2L);
        when(customRequestService.getCustomRequestCountByStatus(RequestStatus.COMPLETED)).thenReturn(2L);

        mockMvc.perform(get("/api/custom-requests/stats/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(10))
                .andExpect(jsonPath("$.pendingCount").value(3))
                .andExpect(jsonPath("$.approvedCount").value(2))
                .andExpect(jsonPath("$.rejectedCount").value(1))
                .andExpect(jsonPath("$.inProgressCount").value(2))
                .andExpect(jsonPath("$.completedCount").value(2));

        verify(customRequestService).getTotalCustomRequestCount();
        verify(customRequestService).getCustomRequestCountByStatus(RequestStatus.PENDING);
        verify(customRequestService).getCustomRequestCountByStatus(RequestStatus.APPROVED);
        verify(customRequestService).getCustomRequestCountByStatus(RequestStatus.REJECTED);
        verify(customRequestService).getCustomRequestCountByStatus(RequestStatus.IN_PROGRESS);
        verify(customRequestService).getCustomRequestCountByStatus(RequestStatus.COMPLETED);
    }
} 