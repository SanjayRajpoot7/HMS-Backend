package com.hotel.auth;

//package com.hotel.auth.controller;

import com.hotel.auth.controller.AuthController;
import com.hotel.auth.dto.LoginRequestDTO;
import com.hotel.auth.dto.LoginResponseDTO;
import com.hotel.auth.dto.RegisterRequestDTO;
import com.hotel.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@Mock
	private AuthService authService;

	@InjectMocks
	private AuthController authController;

	private RegisterRequestDTO registerRequestDTO;
	private LoginRequestDTO loginRequestDTO;
	private LoginResponseDTO loginResponseDTO;



	@BeforeEach
	void setUp() {
		loginResponseDTO = new LoginResponseDTO("Login successful", "mockedJwtToken");
		registerRequestDTO = new RegisterRequestDTO();
		registerRequestDTO.setUsername("testUser");
//		registerRequestDTO.setEmail("test@example.com");
		registerRequestDTO.setPassword("securePassword");

		loginRequestDTO = new LoginRequestDTO();
//		loginRequestDTO.setEmail("test@example.com");
		loginRequestDTO.setPassword("securePassword");

		loginResponseDTO = new LoginResponseDTO(loginResponseDTO.getMessage(), loginResponseDTO.getToken());
		loginResponseDTO.setToken("mockedJwtToken");
	}

	@Test
	void register_ShouldReturnSuccessMessage() {
		when(authService.register(registerRequestDTO)).thenReturn("User registered successfully");

		ResponseEntity<Map<String, String>> response = authController.register(registerRequestDTO);

		assertEquals(200, response.getStatusCode().value());
		assertEquals("User registered successfully", response.getBody().get("message"));
	}

	@Test
	void login_ShouldReturnValidJwtToken() {
		when(authService.login(loginRequestDTO)).thenReturn(loginResponseDTO);

		ResponseEntity<LoginResponseDTO> response = authController.login(loginRequestDTO);

		assertEquals(200, response.getStatusCode().value());
		assertEquals("mockedJwtToken", response.getBody().getToken());
	}
}
