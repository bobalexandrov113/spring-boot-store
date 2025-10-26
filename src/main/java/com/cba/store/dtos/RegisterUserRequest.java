package com.cba.store.dtos;

import com.cba.store.validation.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Size(max=255,message = "Name must be less than 255 characters")
    private String name;
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    @Lowercase(message = "email has to be in lowercase")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min=6,max=9,message = "Password must be between 6 and 9 characters")
    private String password;
}
