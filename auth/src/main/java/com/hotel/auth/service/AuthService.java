package com.hotel.auth.service;

import com.hotel.auth.dto.LoginRequestDTO;
import com.hotel.auth.dto.LoginResponseDTO;
import com.hotel.auth.dto.RegisterRequestDTO;
import lombok.Data;


public interface AuthService {

    // Registers a new user
    String register(RegisterRequestDTO registerReques);

    // Logs in a user and returns token
    LoginResponseDTO login(LoginRequestDTO loginRequest);
}
