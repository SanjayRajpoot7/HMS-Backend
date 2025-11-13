package com.hotel.roomService.service.impl;

import com.hotel.roomService.dto.InventoryRequestDTO;
import com.hotel.roomService.dto.InventoryResponseDTO;
import com.hotel.roomService.exception.ResourceNotFoundException;
import com.hotel.roomService.model.InventoryItem;
import com.hotel.roomService.model.Room;
import com.hotel.roomService.repository.InventoryRepository;
import com.hotel.roomService.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private InventoryRequestDTO inventoryRequestDTO;
    private InventoryItem inventoryItem;
    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setRoomId(1L);

        inventoryRequestDTO = new InventoryRequestDTO();
        inventoryRequestDTO.setItemName("Towel");
        inventoryRequestDTO.setCategory("Bathroom");
        inventoryRequestDTO.setQuantity(5);
        inventoryRequestDTO.setRoomId(1L);

        inventoryItem = new InventoryItem();
        inventoryItem.setId(1L);
        inventoryItem.setItemName("Towel");
        inventoryItem.setCategory("Bathroom");
        inventoryItem.setQuantity(5);
        inventoryItem.setRoom(room);
    }

    @Test
    void createItem_ShouldReturnSavedItem() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(inventoryRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);

        InventoryItem response = inventoryService.createItem(inventoryRequestDTO);

        assertNotNull(response);
        assertEquals("Towel", response.getItemName());
    }

    @Test
    void createItem_ShouldThrowExceptionIfRoomNotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.createItem(inventoryRequestDTO));
    }

    @Test
    void getAllItems_ShouldReturnInventoryList() {
        List<InventoryItem> items = new ArrayList<>();
        items.add(inventoryItem);

        when(inventoryRepository.findAll()).thenReturn(items);

        List<InventoryResponseDTO> response = inventoryService.getAllItems();

        assertEquals(1, response.size());
        assertEquals("Towel", response.get(0).getItemName());
    }

    @Test
    void getItemById_ShouldReturnInventoryItem() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventoryItem));

        InventoryResponseDTO response = inventoryService.getItemById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void getItemById_ShouldThrowExceptionIfNotFound() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.getItemById(1L));
    }

    @Test
    void updateItem_ShouldReturnUpdatedInventoryItem() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventoryItem));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(inventoryRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);

        InventoryItem response = inventoryService.updateItem(1L, inventoryRequestDTO);

        assertNotNull(response);
        assertEquals("Towel", response.getItemName());
    }

    @Test
    void updateItem_ShouldThrowExceptionIfItemNotFound() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.updateItem(1L, inventoryRequestDTO));
    }
}