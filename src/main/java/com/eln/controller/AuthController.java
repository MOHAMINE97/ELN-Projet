package com.eln.controller;

import com.eln.dto.AuthDTO;
import com.eln.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDTO.AuthResponse> inscrire(@Valid @RequestBody AuthDTO.RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.inscrire(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTO.AuthResponse> connecter(@Valid @RequestBody AuthDTO.LoginRequest request) {
        return ResponseEntity.ok(authService.connecter(request));
    }
}
