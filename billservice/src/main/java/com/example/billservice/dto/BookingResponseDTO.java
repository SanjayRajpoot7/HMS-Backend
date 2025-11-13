package com.example.billservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponseDTO {

    private Long bookingId;

    private GuestResponseDTO guest;

    private RoomResponseDto room;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    private Integer numGuests;

    private String bookingStatus;

    public BookingResponseDTO(String message) {
        this.bookingStatus = message; // Use bookingStatus to store the fallback message
    }
}
