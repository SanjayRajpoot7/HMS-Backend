package com.hotel.bookingService.service.impl;


//package com.hotel.bookingService.service.imple;

import com.hotel.bookingService.dto.*;
import com.hotel.bookingService.exception.*;
import com.hotel.bookingService.model.Booking;
import com.hotel.bookingService.model.BookingStatus;
import com.hotel.bookingService.model.Guest;
import com.hotel.bookingService.repository.BookingRepository;
import com.hotel.bookingService.repository.GuestRepository;
import com.hotel.bookingService.service.GuestService;
import com.hotel.bookingService.service.RoomServiceClient;
import com.hotel.bookingService.service.imple.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomServiceClient roomServiceClient;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private GuestService guestService;

    @Mock
    private GuestRepository guestRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingRequestDTO bookingRequestDTO;
    private Booking booking;
    private Guest guest;
    private RoomResponseDto roomResponseDto;
    private BookingResponseDTO bookingResponseDTO;
    private GuestResponseDTO guestResponseDTO;

    @BeforeEach
    void setUp() {
        bookingRequestDTO = new BookingRequestDTO();
        bookingRequestDTO.setCheckinDate(LocalDate.now().plusDays(1));
        bookingRequestDTO.setCheckoutDate(LocalDate.now().plusDays(3));
        bookingRequestDTO.setNumGuests(2);

        // Ensure guest details are set properly in the request DTO
        GuestRequestDTO guestRequestDTO = new GuestRequestDTO();
        guestRequestDTO.setFullName("John Doe");
        guestRequestDTO.setEmail("john.doe@example.com");
        bookingRequestDTO.setGuest(guestRequestDTO);  // Add guest information

        guest = new Guest();
        guest.setGuestId(1L);
        guest.setFullName("John Doe");

        guestResponseDTO = new GuestResponseDTO();
        guestResponseDTO.setGuestId(1L);
        guestResponseDTO.setFullName("John Doe");

        roomResponseDto = new RoomResponseDto();
        roomResponseDto.setRoomId(101L);
        roomResponseDto.setRoomType("Deluxe");
        roomResponseDto.setAvailable(true);

        booking = new Booking();
        booking.setBookingId(1L);
        booking.setGuestId(1L);
        booking.setRoomId(101L);
        booking.setCheckinDate(LocalDate.now().plusDays(1));
        booking.setCheckoutDate(LocalDate.now().plusDays(3));
        booking.setNumGuests(2);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        bookingResponseDTO = new BookingResponseDTO(1L, guestResponseDTO, roomResponseDto,
                booking.getCheckinDate(), booking.getCheckoutDate(), booking.getNumGuests(), "PENDING");
    }

    @Test
    void createBooking_ShouldReturnBookingResponseDTO() {
        when(roomServiceClient.getAvailableRoomByType(anyString())).thenReturn(roomResponseDto);
        when(guestService.createGuest(any(GuestRequestDTO.class))).thenReturn(guestResponseDTO);
        when(modelMapper.map(guestResponseDTO, Guest.class)).thenReturn(guest);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        doNothing().when(roomServiceClient).updateRoomAvailability(anyLong(), anyBoolean());

        BookingResponseDTO response = bookingService.createBooking("Deluxe", bookingRequestDTO);

        assertNotNull(response);
        assertEquals("John Doe", response.getGuest().getFullName());
    }

    @Test
    void createBooking_ShouldThrowExceptionWhenRoomNotAvailable() {
        when(roomServiceClient.getAvailableRoomByType("Deluxe")).thenReturn(null);

        assertThrows(RoomNotAvailableException.class, () -> bookingService.createBooking("Deluxe", bookingRequestDTO));
    }

    @Test
    void getBookingById_ShouldReturnBookingResponseDTO() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(roomServiceClient.getRoomById(101L)).thenReturn(roomResponseDto);
        when(modelMapper.map(guest, GuestResponseDTO.class)).thenReturn(guestResponseDTO);

        BookingResponseDTO response = bookingService.getBookingById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getBookingId());
    }

    @Test
    void getBookingById_ShouldThrowExceptionWhenNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(1L));
    }

    @Test
    void updateBookingStatus_ShouldUpdateSuccessfully() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertDoesNotThrow(() -> bookingService.updateBookingStatus(1L));
        assertEquals(BookingStatus.BOOKED, booking.getBookingStatus());
    }

    @Test
    void deleteBooking_ShouldDeleteSuccessfully() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertDoesNotThrow(() -> bookingService.deleteBooking(1L));
        verify(bookingRepository, times(1)).delete(booking);
    }
}