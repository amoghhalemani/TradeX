package com.tradex.tradex.dotp;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username is Required")
    private String username;

    @NotBlank(message = "Password is Required")
    @Size(min = 8, message = "Message size must be minimum 8 characters long")
    private String password;

    @NotBlank(message = "Email is Required")
    @Email( message="Invalid Email Format" )
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    private String phoneNumber;
}
