package com.Yash.Busbookingapp.dto;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String name;
    private String email;
    private String username;
    private String password; // Only needed during registration
}
