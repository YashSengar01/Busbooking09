package com.Yash.Busbookingapp.controller;

import com.Yash.Busbookingapp.dto.UserDTO;
import com.Yash.Busbookingapp.dto.UserRegistrationRequest;
import com.Yash.Busbookingapp.model.Users;
import com.Yash.Busbookingapp.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserRegistrationRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(request.getName());
        userDTO.setEmail(request.getEmail());
        userDTO.setUsername(request.getUsername());
        userDTO.setRoles(List.of("ROLE_USER")); // default role

        UserDTO savedUser = userService.registerUser(userDTO, request.getPassword());
        return ResponseEntity.ok(savedUser);
    }

}
