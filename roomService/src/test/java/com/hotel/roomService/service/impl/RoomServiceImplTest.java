package com.hotel.roomService.service.impl;

import com.hotel.roomService.dto.RoomRequestDto;
import com.hotel.roomService.dto.RoomResponseDto;
import com.hotel.roomService.exception.ResourceNotFoundException;
import com.hotel.roomService.exception.RoomAlreadyExistsException;
import com.hotel.roomService.model.Room;
import com.hotel.roomService.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room room;
    private RoomRequestDto roomRequestDto;
    private RoomResponseDto roomResponseDto;

    @BeforeEach
    void setUp() {
        roomRequestDto = new RoomRequestDto();
        roomRequestDto.setRoomNumber("101");
        roomRequestDto.setRoomType("Deluxe");

        room = new Room();
        room.setRoomId(1L);
        room.setRoomNumber("101");
        room.setRoomType("Deluxe");

        roomResponseDto = new RoomResponseDto();
        roomResponseDto.setRoomId(1L);
        roomResponseDto.setRoomNumber("101");
        roomResponseDto.setRoomType("Deluxe");
    }

    @Test
    void createRoom_ShouldReturnRoomResponseDto() {
        when(roomRepository.findByRoomNumber("101")).thenReturn(Optional.empty());
        when(modelMapper.map(roomRequestDto, Room.class)).thenReturn(room);
        when(roomRepository.save(room)).thenReturn(room);
        when(modelMapper.map(room, RoomResponseDto.class)).thenReturn(roomResponseDto);

        RoomResponseDto response = roomService.createRoom(roomRequestDto);

        assertNotNull(response);
        assertEquals("101", response.getRoomNumber());
    }

    @Test
    void createRoom_ShouldThrowExceptionIfRoomExists() {
        when(roomRepository.findByRoomNumber("101")).thenReturn(Optional.of(room));

        assertThrows(RoomAlreadyExistsException.class, () -> roomService.createRoom(roomRequestDto));
    }

    @Test
    void getRoomById_ShouldReturnRoomResponseDto() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(modelMapper.map(room, RoomResponseDto.class)).thenReturn(roomResponseDto);

        RoomResponseDto response = roomService.getRoomById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getRoomId());
    }

    @Test
    void getRoomById_ShouldThrowExceptionIfRoomNotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roomService.getRoomById(1L));
    }
}