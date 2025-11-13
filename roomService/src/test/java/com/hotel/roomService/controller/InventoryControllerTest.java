package com.hotel.roomService.controller;

import com.hotel.roomService.dto.InventoryRequestDTO;
import com.hotel.roomService.dto.InventoryResponseDTO;
import com.hotel.roomService.model.InventoryItem;
import com.hotel.roomService.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private InventoryRequestDTO inventoryRequestDTO;
    private InventoryResponseDTO inventoryResponseDTO;
    private InventoryItem inventoryItem;

    @BeforeEach
    void setUp() {
        inventoryRequestDTO = new InventoryRequestDTO();
        inventoryRequestDTO.setItemName("Soap");

        inventoryResponseDTO = new InventoryResponseDTO();
        inventoryResponseDTO.setId(1L);
        inventoryResponseDTO.setItemName("Soap");

        inventoryItem = new InventoryItem();
        inventoryItem.setId(1L);
        inventoryItem.setItemName("Soap");
    }

    @Test
    void createItem_ShouldReturnCreatedItem() {
        when(inventoryService.createItem(inventoryRequestDTO)).thenReturn(inventoryItem);

        InventoryItem response = inventoryController.createItem(inventoryRequestDTO);

        assertEquals("Soap", response.getItemName());
    }

    @Test
    void getAllItems_ShouldReturnInventoryList() {
        List<InventoryResponseDTO> inventoryList = new ArrayList<>();
        inventoryList.add(inventoryResponseDTO);

        when(inventoryService.getAllItems()).thenReturn(inventoryList);

        List<InventoryResponseDTO> response = inventoryController.getAllItems();

        assertEquals(1, response.size());
        assertEquals("Soap", response.get(0).getItemName());
    }

    @Test
    void getItemById_ShouldReturnItemDetails() {
        when(inventoryService.getItemById(1L)).thenReturn(inventoryResponseDTO);

        InventoryResponseDTO response = inventoryController.getItem(1L);

        assertEquals(1L, response.getId());
        assertEquals("Soap", response.getItemName());
    }

    @Test
    void updateItem_ShouldReturnUpdatedItem() {
        when(inventoryService.updateItem(1L, inventoryRequestDTO)).thenReturn(inventoryItem);

        InventoryItem response = inventoryController.updateItem(1L, inventoryRequestDTO);

        assertEquals(1L, response.getId());
        assertEquals("Soap", response.getItemName());
    }
}