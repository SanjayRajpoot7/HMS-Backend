package com.hotel.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Username is required and cannot be empty.")
    @Size(min = 3, max = 15, message = "Username must be between 3 and 20 characters long.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 15, message = "Password must be at least 6 characters long.")
    private String password;

    // Made optional - defaults to USER in service layer
    @Pattern(
            regexp = "ADMIN|RECEPTIONIST|MANAGER|^$",  // Allows empty string
            message = "Invalid role. Role must be: ADMIN, RECEPTIONIST, or MANAGER."
    )
    private String role;
}