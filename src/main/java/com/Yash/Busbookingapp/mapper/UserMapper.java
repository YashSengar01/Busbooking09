package com.Yash.Busbookingapp.mapper;

import java.util.ArrayList;

import com.Yash.Busbookingapp.dto.UserDTO;
import com.Yash.Busbookingapp.model.Users;

public class UserMapper {

    public static UserDTO toDTO(Users user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setRoles(user.getRoles());
        return dto;
    }

    public static Users toEntity(UserDTO dto) {
        Users user = new Users();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        // Ensure roles list is never null
        if (dto.getRoles() != null) {
            user.setRoles(dto.getRoles());
        } else {
            user.setRoles(new ArrayList<>()); // fallback to empty list
        }

        return user;
    }
}
