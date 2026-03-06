package com.tradex.tradex.service;

import com.tradex.tradex.Repository.UserRepository;
import com.tradex.tradex.dotp.RegisterRequest;
import com.tradex.tradex.model.Role;
import com.tradex.tradex.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public String RegisterUser( RegisterRequest registerRequest ) {
        if(userRepository.existsByUsername(registerRequest.getUsername()))
        {
            throw new RuntimeException("Error: Username already exists");
        }
        if(userRepository.existsByEmail(registerRequest.getEmail()))
        {
            throw new RuntimeException("Error: Email already exists");
        }

        User newUser = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(newUser);

        return "User Registered Successfully!";
    }
}
