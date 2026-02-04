package com.Yash.Busbookingapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String username;
    private List<String> roles;

    public boolean isAdmin() {
        return roles != null && roles.contains("ROLE_ADMIN");
    }

}
