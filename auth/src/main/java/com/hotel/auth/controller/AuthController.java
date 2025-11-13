package com.hotel.auth.controller;

import com.hotel.auth.dto.LoginRequestDTO;
import com.hotel.auth.dto.LoginResponseDTO;
import com.hotel.auth.dto.RegisterRequestDTO;
import com.hotel.auth.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Endpoint to register a new user
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequestDTO registerUser) {
        logger.info("Register endpoint called");
        String message = authService.register(registerUser);
        System.out.println("Running Register Route in ");
        Map<String, String> response = new HashMap<>();
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

    // Endpoint to log in a user
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO user) {
        logger.info("Login endpoint called");
        LoginResponseDTO loginResponse = authService.login(user);
        return ResponseEntity.ok(loginResponse);
    }

//    @PostMapping("/register/admin")
//    public ResponseEntity<Map<String, String>> registerAdmin(
//            @RequestHeader("Authorization") String token,
//            @Valid @RequestBody RegisterRequestDTO registerUser
//    ) {
//        String message = authService.registerAdmin(token, registerUser);
//        return ResponseEntity.ok(Map.of("message", message));
//    }
}
