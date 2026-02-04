package com.Yash.Busbookingapp.service;

import com.Yash.Busbookingapp.dto.UserDTO;
import com.Yash.Busbookingapp.mapper.UserMapper;
import com.Yash.Busbookingapp.model.Users;
import com.Yash.Busbookingapp.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Register user with password encoding
    public UserDTO registerUser(UserDTO dto, String rawPassword) {
        Users user = UserMapper.toEntity(dto);

        // Always assign default role if not present
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(new ArrayList<>());
            user.getRoles().add("ROLE_USER");
        }

        user.setPassword(passwordEncoder.encode(rawPassword));
        Users saved = userRepository.save(user);
        return UserMapper.toDTO(saved);
    }

    // ✅ Alternative registration method
    public UserDTO registerNewUser(String name, String email, String username, String password) {
        if (userExists(username)) {
            throw new RuntimeException("Username already exists");
        }
        Users user = new Users();
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        // ✅ Assign default role here
        user.setRoles(List.of("ROLE_USER"));

        Users saved = userRepository.save(user);
        return UserMapper.toDTO(saved);
    }

    // ✅ Get all users
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Get user by ID
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ Get user entity by ID (not DTO)
    public Optional<Users> findById(Long id) {
        return userRepository.findById(id);
    }

    // ✅ Get by username (for login / auth)
    public Users getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ Check if user exists by username
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // ✅ Delete user by ID
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    // ✅ Update user (from DTO)
    public Users updateUser(UserDTO dto) {
        Users existingUser = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(dto.getName());
        existingUser.setEmail(dto.getEmail());
        existingUser.setUsername(dto.getUsername());
        // Don't update password here unless explicitly intended

        return userRepository.save(existingUser);
    }

    public boolean passwordMatches(String rawPassword, Users user) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
