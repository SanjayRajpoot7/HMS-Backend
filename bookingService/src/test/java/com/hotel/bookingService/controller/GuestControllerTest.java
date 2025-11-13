package com.hotel.bookingService.controller;

import com.hotel.bookingService.dto.GuestRequestDTO;
import com.hotel.bookingService.dto.GuestResponseDTO;
import com.hotel.bookingService.service.GuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestControllerTest {

    @Mock
    private GuestService guestService;

    @InjectMocks
    private GuestController guestController;

    private GuestRequestDTO guestRequestDTO;
    private GuestResponseDTO guestResponseDTO;

    @BeforeEach
    void setUp() {
        guestRequestDTO = new GuestRequestDTO();
        guestRequestDTO.setFullName("John Doe");

        guestResponseDTO = new GuestResponseDTO();
        guestResponseDTO.setGuestId(1L);
        guestResponseDTO.setFullName("John Doe");
    }

    @Test
    void createGuest_ShouldReturnCreatedGuest() {
        when(guestService.createGuest(guestRequestDTO)).thenReturn(guestResponseDTO);

        ResponseEntity<GuestResponseDTO> response = guestController.createGuest(guestRequestDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getGuestId());
    }

    @Test
    void getGuestById_ShouldReturnGuestDetails() {
        when(guestService.getGuestById(1L)).thenReturn(guestResponseDTO);

        ResponseEntity<GuestResponseDTO> response = guestController.getGuestById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getGuestId());
    }

    @Test
    void getAllGuests_ShouldReturnGuestList() {
        List<GuestResponseDTO> guests = new ArrayList<>();
        guests.add(guestResponseDTO);

        when(guestService.getAllGuests()).thenReturn(guests);

        ResponseEntity<List<GuestResponseDTO>> response = guestController.getAllGuests();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void updateGuest_ShouldReturnUpdatedGuest() {
        when(guestService.updateGuest(1L, guestRequestDTO)).thenReturn(guestResponseDTO);

        ResponseEntity<GuestResponseDTO> response = guestController.updateGuest(1L, guestRequestDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("John Doe", response.getBody().getFullName());
    }

    @Test
    void deleteGuest_ShouldReturnNoContentStatus() {
        doNothing().when(guestService).deleteGuest(1L);

        ResponseEntity<Void> response = guestController.deleteGuest(1L);

        assertEquals(204, response.getStatusCode().value()); // No Content
    }
}