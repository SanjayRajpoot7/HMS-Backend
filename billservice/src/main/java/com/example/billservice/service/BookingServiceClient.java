package com.example.billservice.service;

import com.example.billservice.dto.BookingResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "BOOKING-SERVICE")
// Declares a Feign client for communicating with the Booking Service
public interface BookingServiceClient {

    // Fetch booking details by booking ID
    @GetMapping("/api/bookings/{id}")
    BookingResponseDTO getBooking(@PathVariable("id") Long id);


    // Update booking status (e.g., mark as paid)
    @PutMapping("/api/bookings/update-status/{bookingId}")
    void updateBookingStatus(@PathVariable Long bookingId, String confirmed);
}
