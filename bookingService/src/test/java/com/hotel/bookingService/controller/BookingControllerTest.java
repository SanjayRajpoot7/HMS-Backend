package com.hotel.bookingService.controller;

import com.hotel.bookingService.dto.BookingRequestDTO;
import com.hotel.bookingService.dto.BookingResponseDTO;
import com.hotel.bookingService.service.BookingService;
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
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private BookingRequestDTO bookingRequestDTO;
    private BookingResponseDTO bookingResponseDTO;

    @BeforeEach
    void setUp() {
        bookingRequestDTO = new BookingRequestDTO();
        bookingRequestDTO.setCheckinDate(java.time.LocalDate.now().plusDays(1));
        bookingRequestDTO.setCheckoutDate(java.time.LocalDate.now().plusDays(2));
        bookingRequestDTO.setNumGuests(2);

        bookingResponseDTO = new BookingResponseDTO();
        bookingResponseDTO.setBookingId(1L);
    }

    @Test
    void createBooking_ShouldReturnCreatedBooking() {
        when(bookingService.createBooking("Deluxe", bookingRequestDTO)).thenReturn(bookingResponseDTO);

        ResponseEntity<?> response = bookingController.createBooking("Deluxe", bookingRequestDTO);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(1L, ((BookingResponseDTO) response.getBody()).getBookingId());
    }

    @Test
    void getBooking_ShouldReturnBookingDetails() {
        when(bookingService.getBookingById(1L)).thenReturn(bookingResponseDTO);

        ResponseEntity<?> response = bookingController.getBooking(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, ((BookingResponseDTO) response.getBody()).getBookingId());
    }

    @Test
    void getAllBookings_ShouldReturnBookingList() {
        List<BookingResponseDTO> bookings = new ArrayList<>();
        bookings.add(bookingResponseDTO);

        when(bookingService.getAllBookings()).thenReturn(bookings);

        ResponseEntity<List<?>> response = bookingController.getAllBookings();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void updateBooking_ShouldReturnUpdatedBooking() {
        when(bookingService.updateBooking(1L, bookingRequestDTO)).thenReturn(bookingResponseDTO);

        ResponseEntity<?> response = bookingController.updateBooking(1L, bookingRequestDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, ((BookingResponseDTO) response.getBody()).getBookingId());
    }

    @Test
    void deleteBooking_ShouldReturnSuccessMessage() {
        doNothing().when(bookingService).deleteBooking(1L);

        ResponseEntity<String> response = bookingController.deleteBooking(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Booking deleted successfully", response.getBody());
    }
}