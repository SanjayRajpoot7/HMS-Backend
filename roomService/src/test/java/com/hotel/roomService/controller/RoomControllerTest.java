package com.hotel.roomService.controller;

import com.hotel.roomService.dto.RoomRequestDto;
import com.hotel.roomService.dto.RoomResponseDto;
import com.hotel.roomService.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private RoomRequestDto roomRequestDto;
    private RoomResponseDto roomResponseDto;

    @BeforeEach
    void setUp() {
        roomRequestDto = new RoomRequestDto();
        roomRequestDto.setRoomType("Deluxe");

        roomResponseDto = new RoomResponseDto();
        roomResponseDto.setRoomId(1L);
        roomResponseDto.setRoomType("Deluxe");
    }

    @Test
    void createRoom_ShouldReturnCreatedRoom() {
        when(roomService.createRoom(roomRequestDto)).thenReturn(roomResponseDto);

        ResponseEntity<RoomResponseDto> response = roomController.createRoom(roomRequestDto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Deluxe", response.getBody().getRoomType());
    }

    @Test
    void getRoomById_ShouldReturnRoomDetails() {
        when(roomService.getRoomById(1L)).thenReturn(roomResponseDto);

        ResponseEntity<RoomResponseDto> response = roomController.getRoomById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getRoomId());
    }
}