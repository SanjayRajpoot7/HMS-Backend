package com.example.billservice.controller;

import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.dto.PaymentResponse;
import com.example.billservice.service.BillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillControllerTest {

    @Mock
    private BillService billService;

    @InjectMocks
    private BillController billController;

    private PaymentResponse paymentResponse;
    private BillResponseDTO billResponseDTO;

    @BeforeEach
    void setUp() {
        // Proper initialization of PaymentResponse
        paymentResponse = new PaymentResponse();
        paymentResponse.setCustomerName("John Doe");
        paymentResponse.setSessionId("abc123");
        paymentResponse.setSessionUrl("https://payment.example.com");

        // Correct initialization of BillResponseDTO
        billResponseDTO = new BillResponseDTO();
        billResponseDTO.setBillId(1L);
        billResponseDTO.setBookingId(1L);
        billResponseDTO.setCustomerName("John Doe");
        billResponseDTO.setSessionId("abc123");
        billResponseDTO.setTotalAmount(200.0);
        billResponseDTO.setPaymentMode("Credit Card");
        billResponseDTO.setPaymentStatus("Success");
        billResponseDTO.setPaymentDate(LocalDateTime.now());
    }

    @Test
    void createBill_ShouldReturnGeneratedBill() throws Exception {
        when(billService.createBill(1L)).thenReturn(paymentResponse);

        ResponseEntity<PaymentResponse> response = billController.createBill(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("John Doe", response.getBody().getCustomerName());
        assertEquals("abc123", response.getBody().getSessionId());
    }

    @Test
    void getBill_ShouldReturnBillDetails() {
        when(billService.getBill(1L)).thenReturn(billResponseDTO);

        ResponseEntity<BillResponseDTO> response = billController.getBill(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getBillId());
        assertEquals("John Doe", response.getBody().getCustomerName());
        assertEquals("Credit Card", response.getBody().getPaymentMode());
    }

    @Test
    void getAllBills_ShouldReturnListOfBills() {
        List<BillResponseDTO> bills = new ArrayList<>();
        bills.add(billResponseDTO);

        when(billService.getAllBills()).thenReturn(bills);

        ResponseEntity<List<BillResponseDTO>> response = billController.getAllBills();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("John Doe", response.getBody().get(0).getCustomerName());
    }
}