package com.Yash.Busbookingapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    @Column(unique = true)
    private String username;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_Roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "Role")
    private List<String> Roles = new ArrayList<>();

    // --- UserDetails Methods Implementation ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username; // or return email; depending on your login logic
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // customize if you plan to implement account expiration
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // customize if you plan to lock accounts
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // customize if you plan to expire credentials
    }

    @Override
    public boolean isEnabled() {
        return true; // toggle if user can be disabled
    }

    public boolean isPresent() {
        return id != null && id > 0; // Check if the user ID is valid
    }

    public Users orElseThrow() {
        return this; // Return the current user object if present
    }

    public boolean isEmpty() {
        return id == null || id <= 0; // Check if the user ID is empty or invalid
    }

}