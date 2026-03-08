package com.tradex.tradex.service;

import com.tradex.tradex.Repository.UserRepository;
import com.tradex.tradex.dotp.AuthResponse;
import com.tradex.tradex.dotp.LoginRequest;
import com.tradex.tradex.dotp.RegisterRequest;
import com.tradex.tradex.model.Role;
import com.tradex.tradex.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // injecting the jwt engine
    private final JwtService jwtService;



    public String RegisterUser( RegisterRequest registerRequest ) {
        // checking for duplicates in database
        if(userRepository.existsByUsername(registerRequest.getUsername()))
        {
            throw new RuntimeException("Error: Username already exists");
        }
        if(userRepository.existsByEmail(registerRequest.getEmail()))
        {
            throw new RuntimeException("Error: Email already exists");
        }
        if(userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber()))
        {
            throw new RuntimeException("Error: Phone number already exists");
        }

        User newUser = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .phoneNumber(registerRequest.getPhoneNumber())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(newUser);

        return "User Registered Successfully!";
    }

    public AuthResponse loginUser( LoginRequest loginRequest ) {
        String id = loginRequest.getIdentifier();

        User user = userRepository.findByUsernameEmailOrPhoneNumber(id, id, id)
                .orElseThrow(() -> new RuntimeException("Error: Invalid Login Credentials"));
        boolean passMatch = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if (!passMatch) {
            throw new RuntimeException("Error: Invalid Login Credentials");
        }

        // generating the token using username
        String jwtToken = jwtService.GenerateToken(user.getUsername());

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
