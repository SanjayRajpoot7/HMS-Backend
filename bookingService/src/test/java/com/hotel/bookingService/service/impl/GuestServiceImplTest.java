package com.hotel.bookingService.service.impl;

//package com.hotel.bookingService.service.imple;

import com.hotel.bookingService.dto.GuestRequestDTO;
import com.hotel.bookingService.dto.GuestResponseDTO;
import com.hotel.bookingService.exception.GuestNotFoundException;
import com.hotel.bookingService.model.Guest;
import com.hotel.bookingService.repository.GuestRepository;
import com.hotel.bookingService.service.imple.GuestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestServiceImplTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GuestServiceImpl guestService;

    private GuestRequestDTO guestRequestDTO;
    private GuestResponseDTO guestResponseDTO;
    private Guest guest;

    @BeforeEach
    void setUp() {
        guestRequestDTO = new GuestRequestDTO();
        guestRequestDTO.setFullName("John Doe");
        guestRequestDTO.setEmail("john.doe@example.com");
        guestRequestDTO.setPhone("9876543210");

        guest = new Guest();
        guest.setGuestId(1L);
        guest.setFullName("John Doe");
        guest.setEmail("john.doe@example.com");
        guest.setPhone("9876543210");

        guestResponseDTO = new GuestResponseDTO();
        guestResponseDTO.setGuestId(1L);
        guestResponseDTO.setFullName("John Doe");
        guestResponseDTO.setEmail("john.doe@example.com");
        guestResponseDTO.setPhone("9876543210");
    }

    @Test
    void createGuest_ShouldReturnCreatedGuest() {
        when(guestRepository.save(any(Guest.class))).thenReturn(guest);
        when(modelMapper.map(guest, GuestResponseDTO.class)).thenReturn(guestResponseDTO);

        GuestResponseDTO response = guestService.createGuest(guestRequestDTO);

        assertNotNull(response);
        assertEquals("John Doe", response.getFullName());
    }

    @Test
    void getGuestById_ShouldReturnGuestDetails() {
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(modelMapper.map(guest, GuestResponseDTO.class)).thenReturn(guestResponseDTO);

        GuestResponseDTO response = guestService.getGuestById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getGuestId());
    }

    @Test
    void getGuestById_ShouldThrowExceptionWhenGuestNotFound() {
        when(guestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> guestService.getGuestById(1L));
    }

    @Test
    void getAllGuests_ShouldReturnGuestList() {
        List<Guest> guestList = new ArrayList<>();
        guestList.add(guest);

        when(guestRepository.findAll()).thenReturn(guestList);
        when(modelMapper.map(guest, GuestResponseDTO.class)).thenReturn(guestResponseDTO);

        List<GuestResponseDTO> response = guestService.getAllGuests();

        assertEquals(1, response.size());
        assertEquals("John Doe", response.get(0).getFullName());
    }

    @Test
    void updateGuest_ShouldReturnUpdatedGuest() {
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(guestRepository.save(any(Guest.class))).thenReturn(guest);
        when(modelMapper.map(guest, GuestResponseDTO.class)).thenReturn(guestResponseDTO);

        GuestResponseDTO response = guestService.updateGuest(1L, guestRequestDTO);

        assertNotNull(response);
        assertEquals("John Doe", response.getFullName());
    }

    @Test
    void updateGuest_ShouldThrowExceptionWhenGuestNotFound() {
        when(guestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> guestService.updateGuest(1L, guestRequestDTO));
    }

    @Test
    void deleteGuest_ShouldDeleteSuccessfully() {
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        doNothing().when(guestRepository).delete(guest);

        assertDoesNotThrow(() -> guestService.deleteGuest(1L));
        verify(guestRepository, times(1)).delete(guest);
    }

    @Test
    void deleteGuest_ShouldThrowExceptionWhenGuestNotFound() {
        when(guestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> guestService.deleteGuest(1L));
    }
}