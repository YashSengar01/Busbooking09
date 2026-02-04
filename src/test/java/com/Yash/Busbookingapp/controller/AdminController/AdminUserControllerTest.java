package com.Yash.Busbookingapp.controller.AdminController;

import com.Yash.Busbookingapp.controller.admincontroller.AdminUserController;
import com.Yash.Busbookingapp.dto.UserDTO;
import com.Yash.Busbookingapp.model.Users;
import com.Yash.Busbookingapp.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminUserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminUserController adminUserController;

    private UserDTO user1;
    private UserDTO user2;
    private Users updatedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Users setup
        user1 = new UserDTO();
        user1.setId(1L);
        user1.setUsername("alice");
        user1.setEmail("alice@example.com");
        user1.setRoles(Arrays.asList("ROLE_USER")); // ✅ List instead of Set

        user2 = new UserDTO();
        user2.setId(2L);
        user2.setUsername("bob");
        user2.setEmail("bob@example.com");
        user2.setRoles(Arrays.asList("ROLE_ADMIN")); // ✅ List

        // Updated user for PUT
        updatedUser = new Users();
        updatedUser.setUsername("alice_updated");
        updatedUser.setEmail("alice2@example.com");
        updatedUser.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN")); // ✅ List
    }

    @Test
    void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        ResponseEntity<List<UserDTO>> response = adminUserController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById_Found() {
        when(userService.getUserById(1L)).thenReturn(user1);

        ResponseEntity<UserDTO> response = adminUserController.getUserById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("alice", response.getBody().getUsername());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userService.getUserById(99L)).thenReturn(null);

        ResponseEntity<UserDTO> response = adminUserController.getUserById(99L);

        assertEquals(404, response.getStatusCodeValue());
        verify(userService, times(1)).getUserById(99L);
    }

    @Test
    void testUpdateUser_Found() {
        when(userService.getUserById(1L)).thenReturn(user1);
        when(userService.updateUser(any(UserDTO.class))).thenReturn(updatedUser);

        ResponseEntity<Users> response = adminUserController.updateUser(1L, updatedUser);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("alice_updated", response.getBody().getUsername());
        assertEquals("alice2@example.com", response.getBody().getEmail());
        verify(userService, times(1)).getUserById(1L);
        verify(userService, times(1)).updateUser(any(UserDTO.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userService.getUserById(99L)).thenReturn(null);

        ResponseEntity<Users> response = adminUserController.updateUser(99L, updatedUser);

        assertEquals(404, response.getStatusCodeValue());
        verify(userService, times(1)).getUserById(99L);
        verify(userService, never()).updateUser(any());
    }

    @Test
    void testDeleteUser_Found() {
        when(userService.getUserById(1L)).thenReturn(user1);
        doNothing().when(userService).deleteUserById(1L);

        ResponseEntity<Void> response = adminUserController.deleteUser(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).getUserById(1L);
        verify(userService, times(1)).deleteUserById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userService.getUserById(99L)).thenReturn(null);

        ResponseEntity<Void> response = adminUserController.deleteUser(99L);

        assertEquals(404, response.getStatusCodeValue());
        verify(userService, times(1)).getUserById(99L);
        verify(userService, never()).deleteUserById(anyLong());
    }
}
