package com.Yash.Busbookingapp.service;

import com.Yash.Busbookingapp.model.Users;
import com.Yash.Busbookingapp.repository.UserRepository;
import com.Yash.Busbookingapp.security.CustomUserDetails;
import com.Yash.Busbookingapp.service.impl.UserDetailsServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private Users user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new Users();
        user.setId(1L);
        user.setUsername("alice");
        user.setPassword("password123");
        user.setEmail("alice@example.com");
    }

    // -------------------- SUCCESS: USER FOUND --------------------
    @Test
    void testLoadUserByUsername_UserFound() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("alice");

        assertNotNull(userDetails);
        assertTrue(userDetails instanceof CustomUserDetails);
        assertEquals("alice", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());

        verify(userRepository, times(1)).findByUsername("alice");
    }

    // -------------------- FAILURE: USER NOT FOUND --------------------
    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("bob"));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("bob");
    }
}
