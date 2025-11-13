package com.hotel.staffService.controller;

import com.hotel.staffService.dto.StaffRequestDTO;
import com.hotel.staffService.dto.StaffResponseDTO;
import com.hotel.staffService.model.Staff;
import com.hotel.staffService.repository.StaffRepository;
import com.hotel.staffService.service.StaffService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff") // Base URL for staff APIs
public class StaffController {

    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @Autowired
    private StaffService staffService;

    @Autowired
    private StaffRepository staffRepository;

    // Create new staff
    @PostMapping("/add")
    public ResponseEntity<StaffResponseDTO> createStaff(@Valid @RequestBody StaffRequestDTO staffRequestDTO) {
        logger.info("Creating new staff with data: {}", staffRequestDTO);
        StaffResponseDTO response = staffService.createStaff(staffRequestDTO);
        logger.info("Created staff successfully with id: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    public Staff saveStaff(Staff staff) {
        logger.debug("Saving staff: {}", staff);
        return staffRepository.save(staff);
    }

    // Get staff by ID
    @GetMapping("/{id}")
    public ResponseEntity<StaffResponseDTO> getStaffById(@PathVariable Long id) {
        logger.info("Fetching staff with id: {}", id);
        StaffResponseDTO response = staffService.getStaffById(id);
        logger.info("Fetched staff details: {}", response);
        return ResponseEntity.ok(response);
    }

    // Get all staff
    @GetMapping("/getAll")
    public ResponseEntity<List<StaffResponseDTO>> getAllStaff() {
        logger.info("Fetching all staff records");
        List<StaffResponseDTO> staffList = staffService.getAllStaff();
        logger.info("Total staff found: {}", staffList.size());
        return ResponseEntity.ok(staffList);
    }

    // Update staff by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<StaffResponseDTO> updateStaff(@PathVariable Long id, @Valid @RequestBody StaffRequestDTO staffRequestDTO) {
        logger.info("Updating staff with id: {}, data: {}", id, staffRequestDTO);
        StaffResponseDTO response = staffService.updateStaff(id, staffRequestDTO);
        logger.info("Updated staff successfully: {}", response);
        return ResponseEntity.ok(response);
    }

    // Delete staff by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStaff(@PathVariable Long id) {
        logger.info("Deleting staff with id: {}", id);
        staffService.deleteStaff(id);
        logger.info("Deleted staff successfully with id: {}", id);
        return ResponseEntity.ok("Staff deleted successfully");
    }
}
