package com.tradex.tradex.dotp;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Username or Email or Phone Number is Required")
    private String identifier;

    @NotBlank(message = "Password is Required")
    private String password;

}
