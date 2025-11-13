package com.example.billservice.controller;

import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.dto.PaymentResponse;
import com.example.billservice.service.BillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/bills") // Base URL for all bill-related endpoints
public class BillController {

    private static final Logger logger = LoggerFactory.getLogger(BillController.class);

    private final BillService billService;

    // Constructor injection of BillService
    public BillController(BillService billService) {
        this.billService = billService;
    }

    // Endpoint to generate a new bill based on bookingId
    @GetMapping("/generate")
    public ResponseEntity<PaymentResponse> createBill(@RequestParam Long bookingId) throws Exception {
        logger.info("Generating bill for bookingId: {}", bookingId);
        return ResponseEntity.ok(billService.createBill(bookingId));
    }

    // Endpoint to fetch a specific bill by its ID
    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDTO> getBill(@PathVariable Long id) {
        logger.info("Fetching bill by ID: {}", id);
        return ResponseEntity.ok(billService.getBill(id));
    }

    // Endpoint to fetch all bills
    @GetMapping
    public ResponseEntity<List<BillResponseDTO>> getAllBills() {
        logger.info("Fetching all bills");
        return ResponseEntity.ok(billService.getAllBills());
    }
}
