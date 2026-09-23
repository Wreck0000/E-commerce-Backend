package com.ecom.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @NotBlank(message="First name cannot be empty")
    private String firstName;
    @NotBlank(message="Last name cannot be empty")
    private String lastName;

}
