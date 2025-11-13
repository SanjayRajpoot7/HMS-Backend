package com.hotel.staffService.service;

import com.hotel.staffService.dto.StaffRequestDTO;
import com.hotel.staffService.dto.StaffResponseDTO;

import java.util.List;

public interface StaffService {

    // Create a new staff member
    StaffResponseDTO createStaff(StaffRequestDTO staffRequestDTO);

    // Get staff details by ID
    StaffResponseDTO getStaffById(Long id);

    // Get list of all staff members
    List<StaffResponseDTO> getAllStaff();

    // Update staff details by ID
    StaffResponseDTO updateStaff(Long id, StaffRequestDTO staffRequestDTO);

    // Delete a staff member by ID
    void deleteStaff(Long id);


}
