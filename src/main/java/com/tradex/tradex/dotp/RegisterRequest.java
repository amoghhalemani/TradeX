package com.tradex.tradex.dotp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username is Required")
    private String username;

    @NotBlank(message = "Password is Required")
    @Size(min = 8, message = "Message size must be minimum 8 characters long")
    private String password;

    @NotBlank (message = "Email is Required" )
    @Email( message="Invalid Email Format" )
    private String email;
}
