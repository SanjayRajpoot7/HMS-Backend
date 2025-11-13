package com.hotel.staffService.controller;

import com.hotel.staffService.dto.StaffRequestDTO;
import com.hotel.staffService.dto.StaffResponseDTO;
import com.hotel.staffService.service.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffControllerTest {

    @Mock
    private StaffService staffService;

    @InjectMocks
    private StaffController staffController;

    private StaffRequestDTO staffRequestDTO;
    private StaffResponseDTO staffResponseDTO;

    @BeforeEach
    void setUp() {
        staffRequestDTO = new StaffRequestDTO();
        staffRequestDTO.setFullName("John Doe");
        staffRequestDTO.setDepartment("Housekeeping");

        staffResponseDTO = new StaffResponseDTO();
        staffResponseDTO.setId(1L);
        staffResponseDTO.setFullName("John Doe");
        staffResponseDTO.setDepartment("Housekeeping");
    }

    @Test
    void createStaff_ShouldReturnCreatedStaff() {
        when(staffService.createStaff(staffRequestDTO)).thenReturn(staffResponseDTO);

        ResponseEntity<StaffResponseDTO> response = staffController.createStaff(staffRequestDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("John Doe", response.getBody().getFullName());
    }

    @Test
    void getStaffById_ShouldReturnStaffDetails() {
        when(staffService.getStaffById(1L)).thenReturn(staffResponseDTO);

        ResponseEntity<StaffResponseDTO> response = staffController.getStaffById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getAllStaff_ShouldReturnStaffList() {
        List<StaffResponseDTO> staffList = new ArrayList<>();
        staffList.add(staffResponseDTO);

        when(staffService.getAllStaff()).thenReturn(staffList);

        ResponseEntity<List<StaffResponseDTO>> response = staffController.getAllStaff();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void updateStaff_ShouldReturnUpdatedStaff() {
        when(staffService.updateStaff(1L, staffRequestDTO)).thenReturn(staffResponseDTO);

        ResponseEntity<StaffResponseDTO> response = staffController.updateStaff(1L, staffRequestDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("John Doe", response.getBody().getFullName());
    }

    @Test
    void deleteStaff_ShouldReturnSuccessMessage() {
        doNothing().when(staffService).deleteStaff(1L);

        ResponseEntity<String> response = staffController.deleteStaff(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Staff deleted successfully", response.getBody());
    }
}