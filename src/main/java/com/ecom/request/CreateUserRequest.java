package com.ecom.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message="First name cannot be empty")
    private String firstName;
    @NotBlank(message="Last name cannot be empty")
    private String lastName;
    @Email
    private String email;
    @NotBlank(message="Password cannot be empty")
    @Size(min=6, max=16 ,message = "password must be at least 6 character")
    private String password;
}
