package com.eln.dto;

import com.eln.model.Enums.Role;
import jakarta.validation.constraints.*;
import lombok.*;

public class AuthDTO {

    @Getter
    @Setter
    public static class RegisterRequest {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
        private String motDePasse;

        @NotBlank
        private String nom;

        private Role role;
    }

    @Getter
    @Setter
    public static class LoginRequest {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        private String motDePasse;
    }

    @Getter
    @Setter
    public static class AuthResponse {
        private String token;
        private Long userId;
        private String email;
        private String nom;
        private Role role;

        public AuthResponse(String token, Long userId, String email, String nom, Role role) {
            this.token = token;
            this.userId = userId;
            this.email = email;
            this.nom = nom;
            this.role = role;
        }
    }
}

