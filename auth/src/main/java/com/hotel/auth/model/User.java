package com.hotel.auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(unique = true)
    private String username;

    @Getter
    @Setter
    private String password;

    @Pattern(regexp = "ADMIN|RECEPTIONIST|MANAGER", message = "Role must be ADMIN, RECEPTIONIST, or MANAGER")
    private String role;

    public @Pattern(regexp = "ADMIN|RECEPTIONIST|MANAGER", message = "Role must be ADMIN, RECEPTIONIST, or MANAGER")
    String getRole() {
        return role;
    }

    public void setRole(
            @Pattern(regexp = "ADMIN|RECEPTIONIST|MANAGER", message = "Role must be ADMIN, RECEPTIONIST, or MANAGER")
            String role
    ) {
        this.role = role;
    }
}