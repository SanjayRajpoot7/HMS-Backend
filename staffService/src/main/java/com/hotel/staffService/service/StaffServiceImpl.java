package com.hotel.staffService.service;

import com.hotel.staffService.dto.StaffRequestDTO;
import com.hotel.staffService.dto.StaffResponseDTO;
import com.hotel.staffService.exception.StaffNotFoundException;
import com.hotel.staffService.model.Staff;
import com.hotel.staffService.repository.StaffRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    private static final Logger logger = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public StaffResponseDTO createStaff(StaffRequestDTO staffRequestDTO) {
        logger.info("Creating new staff with name: {}", staffRequestDTO.getFullName());

        Staff staff = new Staff();
        staff.setFullName(staffRequestDTO.getFullName());
        staff.setEmail(staffRequestDTO.getEmail());
        staff.setSalary(staffRequestDTO.getSalary());
        staff.setAddress(staffRequestDTO.getAddress());
        staff.setAge(staffRequestDTO.getAge());
        staff.setOccupation(staffRequestDTO.getOccupation());
        staff.setIdProof(staffRequestDTO.getIdProof());
        staff.setIdProofNumber(staffRequestDTO.getIdProofNumber());
        staff.setPhoneNumber(staffRequestDTO.getPhoneNumber());
        staff.setJoiningDate(staffRequestDTO.getJoiningDate());
        staff.setDepartment(staffRequestDTO.getDepartment());

        Staff savedStaff = staffRepository.save(staff);

        logger.info("Staff created successfully with ID: {}", savedStaff.getId());
        return modelMapper.map(savedStaff, StaffResponseDTO.class);
    }

    @Override
    public StaffResponseDTO getStaffById(Long id) {
        logger.info("Fetching staff with ID: {}", id);
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Staff not found with ID: {}", id);
                    return new StaffNotFoundException("Staff not found with id: " + id);
                });
        return modelMapper.map(staff, StaffResponseDTO.class);
    }

    @Override
    public List<StaffResponseDTO> getAllStaff() {
        logger.info("Fetching all staff");
        List<Staff> staffList = staffRepository.findAll();
        return staffList.stream()
                .map(staff -> modelMapper.map(staff, StaffResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public StaffResponseDTO updateStaff(Long id, StaffRequestDTO staffRequestDTO) {
        logger.info("Updating staff with ID: {}", id);
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Staff not found with ID: {}", id);
                    return new StaffNotFoundException("Staff not found with id: " + id);
                });

        staff.setFullName(staffRequestDTO.getFullName());
        staff.setEmail(staffRequestDTO.getEmail());
        staff.setSalary(staffRequestDTO.getSalary());
        staff.setAddress(staffRequestDTO.getAddress());
        staff.setAge(staffRequestDTO.getAge());
        staff.setOccupation(staffRequestDTO.getOccupation());
        staff.setIdProof(staffRequestDTO.getIdProof());
        staff.setIdProofNumber(staffRequestDTO.getIdProofNumber());
        staff.setPhoneNumber(staffRequestDTO.getPhoneNumber());
        staff.setJoiningDate(staffRequestDTO.getJoiningDate());
        staff.setDepartment(staffRequestDTO.getDepartment());

        Staff updatedStaff = staffRepository.save(staff);
        logger.info("Staff updated successfully with ID: {}", updatedStaff.getId());

        return modelMapper.map(updatedStaff, StaffResponseDTO.class);
    }

    @Override
    public void deleteStaff(Long id) {
        logger.info("Deleting staff with ID: {}", id);
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Staff not found with ID: {}", id);
                    return new StaffNotFoundException("Staff not found with id: " + id);
                });

        staffRepository.delete(staff);
        logger.info("Staff deleted successfully with ID: {}", id);
    }
}
