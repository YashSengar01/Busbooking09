package com.Yash.Busbookingapp.controller;

import com.Yash.Busbookingapp.dto.UserDTO;
import com.Yash.Busbookingapp.dto.UserRegistrationRequest;
import com.Yash.Busbookingapp.model.Users;
import com.Yash.Busbookingapp.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private Users user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample user entity
        user = new Users();
        user.setId(1L);
        user.setUsername("alice");
        user.setName("Alice");
        user.setEmail("alice@example.com");

        // Sample DTO
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("alice");
        userDTO.setName("Alice");
        userDTO.setEmail("alice@example.com");
        userDTO.setRoles(List.of("ROLE_USER"));
    }

    // -------------------- GET USER --------------------
    @Test
    void testGetUser_Found() {
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<Users> response = userController.getUser(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Alice", response.getBody().getName());
        verify(userService, times(1)).findById(1L);
    }

    @Test
    void testGetUser_NotFound() {
        when(userService.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Users> response = userController.getUser(99L);

        assertEquals(404, response.getStatusCodeValue());
        verify(userService, times(1)).findById(99L);
    }

    // -------------------- REGISTER --------------------
    @Test
    void testRegister_Success() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setUsername("alice");
        request.setPassword("password123");

        when(userService.registerUser(any(UserDTO.class), eq("password123")))
                .thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.register(request);

        assertEquals(200, response.getStatusCodeValue());
        UserDTO body = response.getBody();
        assertNotNull(body);
        assertEquals("Alice", body.getName());
        assertEquals("ROLE_USER", body.getRoles().get(0));

        verify(userService, times(1)).registerUser(any(UserDTO.class), eq("password123"));
    }
}
