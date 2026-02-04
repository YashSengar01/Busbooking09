package com.Yash.Busbookingapp.controller;

import com.Yash.Busbookingapp.dto.AuthRequest;
import com.Yash.Busbookingapp.dto.UserDTO;
import com.Yash.Busbookingapp.model.Users;
import com.Yash.Busbookingapp.repository.UserRepository;
import com.Yash.Busbookingapp.security.JwtUtil;
import com.Yash.Busbookingapp.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    private Users user;
    private UserDetails userDetails;
    private UserDTO userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new Users();
        user.setId(1L);
        user.setUsername("alice");
        user.setPassword("password123");
        user.setEmail("alice@example.com");
        user.setName("Alice");

        userDto = new UserDTO();
        userDto.setId(1L);
        userDto.setUsername("alice");
        userDto.setName("Alice");
        userDto.setEmail("alice@example.com");

        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("alice");
    }

    // -------------------- LOGIN --------------------
    @Test
    void testLogin_Success() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(userDetailsService.loadUserByUsername("alice")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("fake-jwt-token");

        AuthRequest request = new AuthRequest("alice", "password123");
        ResponseEntity<?> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("fake-jwt-token", body.get("token"));
    }

    @Test
    void testLogin_BadCredentials() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad"));

        AuthRequest request = new AuthRequest("alice", "wrong");
        ResponseEntity<?> response = authController.login(request);

        assertEquals(401, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertTrue(body.containsKey("error"));
    }

    // -------------------- REGISTER --------------------
    @Test
    void testRegister_Success() {
        when(userService.userExists("alice")).thenReturn(false);
        when(userService.registerNewUser("Alice", "alice@example.com", "alice", "password123"))
                .thenReturn(userDto); // return UserDTO to match method signature

        Map<String, String> req = Map.of(
                "name", "Alice",
                "email", "alice@example.com",
                "username", "alice",
                "password", "password123");

        ResponseEntity<?> response = authController.registerUser(req);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("message"));
        verify(userService, times(1)).registerNewUser("Alice", "alice@example.com", "alice", "password123");
    }

    @Test
    void testRegister_UserExists() {
        when(userService.userExists("alice")).thenReturn(true);

        Map<String, String> req = Map.of(
                "name", "Alice",
                "email", "alice@example.com",
                "username", "alice",
                "password", "password123");

        ResponseEntity<?> response = authController.registerUser(req);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
        verify(userService, never()).registerNewUser(any(), any(), any(), any());
    }

    // -------------------- PROFILE --------------------
    @Test
    void testGetProfile_Success() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = authController.getProfile(userDetails);

        assertEquals(200, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Alice", body.get("name"));
        assertEquals("alice@example.com", body.get("email"));
        assertEquals("alice", body.get("username"));
    }

    @Test
    void testGetProfile_Unauthorized() {
        ResponseEntity<?> response = authController.getProfile(null);
        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    void testUpdateProfile_Success() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(userRepository.save(any(Users.class))).thenReturn(user);

        Map<String, String> updates = Map.of(
                "name", "AliceUpdated",
                "email", "alice2@example.com");

        ResponseEntity<?> response = authController.updateProfile(userDetails, updates);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("AliceUpdated", user.getName());
        assertEquals("alice2@example.com", user.getEmail());
    }

    @Test
    void testUpdateProfile_Unauthorized() {
        ResponseEntity<?> response = authController.updateProfile(null, Map.of());
        assertEquals(401, response.getStatusCodeValue());
    }

    // -------------------- CHANGE PASSWORD --------------------
    @Test
    void testChangePassword_Success() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(userService.passwordMatches("oldPass", user)).thenReturn(true);
        when(userService.encodePassword("newPass")).thenReturn("encodedNewPass");
        when(userRepository.save(any(Users.class))).thenReturn(user);

        Map<String, String> body = Map.of(
                "oldPassword", "oldPass",
                "newPassword", "newPass");

        ResponseEntity<?> response = authController.changePassword(userDetails, body);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("encodedNewPass", user.getPassword());
    }

    @Test
    void testChangePassword_WrongOldPassword() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(userService.passwordMatches("wrongOld", user)).thenReturn(false);

        Map<String, String> body = Map.of(
                "oldPassword", "wrongOld",
                "newPassword", "newPass");

        ResponseEntity<?> response = authController.changePassword(userDetails, body);

        assertEquals(401, response.getStatusCodeValue());
        Map<?, ?> respBody = (Map<?, ?>) response.getBody();
        assertTrue(respBody.containsKey("error"));
    }
}
