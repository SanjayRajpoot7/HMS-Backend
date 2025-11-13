package com.example.billservice.controller;

import com.example.billservice.repository.BillRepository;
import com.example.billservice.service.imple.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment") // With leading slash
 // Base path for payment-related endpoints
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BillRepository billRepository;

    // Called when payment is successful, with sessionId from the payment gateway
    @GetMapping("/success")
    public ResponseEntity<?> handlePaymentSuccess(@RequestParam("sessionId") String sessionId) {
        // Process payment then redirect to frontend
        paymentService.handlePaymentSuccess(sessionId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "http://localhost:4200/payment-success?session_id=" + sessionId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // HTTP 302 redirect
    }


    // Called when payment fails or is cancelled
    @GetMapping("/failed")
    public ResponseEntity<?> paymentCancel(@RequestParam(value = "sessionId", required = false) String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "http://localhost:4200/payment-cancel?session_id=" + sessionId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // HTTP 302 redirect
    }

}
