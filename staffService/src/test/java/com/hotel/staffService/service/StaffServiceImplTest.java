package com.hotel.staffService.service;

import com.hotel.staffService.dto.StaffRequestDTO;
import com.hotel.staffService.dto.StaffResponseDTO;
import com.hotel.staffService.exception.StaffNotFoundException;
import com.hotel.staffService.model.Staff;
import com.hotel.staffService.repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffServiceImplTest {

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StaffServiceImpl staffService;

    private StaffRequestDTO staffRequestDTO;
    private StaffResponseDTO staffResponseDTO;
    private Staff staff;

    @BeforeEach
    void setUp() {
        staffRequestDTO = new StaffRequestDTO();
        staffRequestDTO.setFullName("Jane Doe");
        staffRequestDTO.setEmail("jane.doe@example.com");
        staffRequestDTO.setDepartment("Reception");

        staff = new Staff();
        staff.setId(1L);
        staff.setFullName("Jane Doe");
        staff.setEmail("jane.doe@example.com");
        staff.setDepartment("Reception");

        staffResponseDTO = new StaffResponseDTO();
        staffResponseDTO.setId(1L);
        staffResponseDTO.setFullName("Jane Doe");
        staffResponseDTO.setEmail("jane.doe@example.com");
        staffResponseDTO.setDepartment("Reception");
    }

    @Test
    void createStaff_ShouldReturnCreatedStaffResponseDTO() {
        when(staffRepository.save(any(Staff.class))).thenReturn(staff);
        when(modelMapper.map(staff, StaffResponseDTO.class)).thenReturn(staffResponseDTO);

        StaffResponseDTO response = staffService.createStaff(staffRequestDTO);

        assertNotNull(response);
        assertEquals("Jane Doe", response.getFullName());
    }

    @Test
    void getStaffById_ShouldReturnStaffResponseDTO() {
        when(staffRepository.findById(1L)).thenReturn(Optional.of(staff));
        when(modelMapper.map(staff, StaffResponseDTO.class)).thenReturn(staffResponseDTO);

        StaffResponseDTO response = staffService.getStaffById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void getStaffById_ShouldThrowExceptionIfStaffNotFound() {
        when(staffRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StaffNotFoundException.class, () -> staffService.getStaffById(1L));
    }

    @Test
    void getAllStaff_ShouldReturnStaffList() {
        List<Staff> staffList = new ArrayList<>();
        staffList.add(staff);

        when(staffRepository.findAll()).thenReturn(staffList);
        when(modelMapper.map(staff, StaffResponseDTO.class)).thenReturn(staffResponseDTO);

        List<StaffResponseDTO> response = staffService.getAllStaff();

        assertEquals(1, response.size());
        assertEquals("Jane Doe", response.get(0).getFullName());
    }

    @Test
    void updateStaff_ShouldReturnUpdatedStaffResponseDTO() {
        when(staffRepository.findById(1L)).thenReturn(Optional.of(staff));
        when(staffRepository.save(any(Staff.class))).thenReturn(staff);
        when(modelMapper.map(staff, StaffResponseDTO.class)).thenReturn(staffResponseDTO);

        StaffResponseDTO response = staffService.updateStaff(1L, staffRequestDTO);

        assertNotNull(response);
        assertEquals("Jane Doe", response.getFullName());
    }

    @Test
    void updateStaff_ShouldThrowExceptionIfStaffNotFound() {
        when(staffRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StaffNotFoundException.class, () -> staffService.updateStaff(1L, staffRequestDTO));
    }

    @Test
    void deleteStaff_ShouldDeleteSuccessfully() {
        when(staffRepository.findById(1L)).thenReturn(Optional.of(staff));
        doNothing().when(staffRepository).delete(staff);

        assertDoesNotThrow(() -> staffService.deleteStaff(1L));
        verify(staffRepository, times(1)).delete(staff);
    }

    @Test
    void deleteStaff_ShouldThrowExceptionIfStaffNotFound() {
        when(staffRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StaffNotFoundException.class, () -> staffService.deleteStaff(1L));
    }
}