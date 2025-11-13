package com.hotel.auth.service;

import com.hotel.auth.dto.LoginRequestDTO;
import com.hotel.auth.dto.LoginResponseDTO;
import com.hotel.auth.dto.RegisterRequestDTO;
import com.hotel.auth.exception.UserAlreadyExistsException;
import com.hotel.auth.exception.UserNotFoundException;
import com.hotel.auth.model.User;
import com.hotel.auth.repository.UserRepository;
import com.hotel.auth.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Data
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String register(RegisterRequestDTO user) {
        logger.info("Registering user: {}", user.getUsername());


        if ("ADMIN".equalsIgnoreCase(user.getRole()) && userRepository.existsByRole("ADMIN")) {
            logger.warn("Attempt to register second admin user: {}", user.getUsername());
            throw new UserAlreadyExistsException("An admin already exists. Only one admin is allowed.");
        }

        // Check if the username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.warn("Username already exists: {}", user.getUsername());
            throw new UserAlreadyExistsException("Username already exists");
        }

        // Allow only one ADMIN user


        // Create and save new user
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(user.getRole());
        userRepository.save(newUser);

        logger.info("User registered successfully: {}", user.getUsername());
        return "User Register Successfully!";
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        logger.info("Login attempt for user: {}", request.getUsername());

        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    logger.warn("Login failed - user not found: {}", request.getUsername());
                    return new UserNotFoundException("Invalid username or password");
                });

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Login failed - invalid password for user: {}", request.getUsername());
            throw new UserNotFoundException("Invalid username or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());
        logger.info("User logged in successfully: {}", request.getUsername());
        return new LoginResponseDTO("Login Successfully!", token);
    }

//    @PostConstruct
//    public void createDefaultAdmin() {
//        if (userRepository.count() == 0) {
//            User admin = new User();
//            admin.setUsername("emergencyadmin");
//            admin.setPassword(passwordEncoder.encode("TempPass123"));
//            admin.setRole("ADMIN");
//            userRepository.save(admin);
//        }
//    }
}
