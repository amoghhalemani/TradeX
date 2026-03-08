package com.tradex.tradex.controller;

import com.tradex.tradex.dotp.AuthResponse;
import com.tradex.tradex.dotp.LoginRequest;
import com.tradex.tradex.dotp.RegisterRequest;
import com.tradex.tradex.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/health")
    public String check(){
        return "Auth is up and running";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        String response = authService.RegisterUser(registerRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest)
    {
        AuthResponse response = authService.loginUser(loginRequest);
        return ResponseEntity.ok(response);
    }
}
