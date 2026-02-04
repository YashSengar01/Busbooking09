package com.Yash.Busbookingapp.config;

import com.Yash.Busbookingapp.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

        @Autowired
        private JwtRequestFilter jwtRequestFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .authorizeHttpRequests(auth -> auth

                                                // 🌍 PUBLIC PAGES + STATIC FILES
                                                .requestMatchers(
                                                                "/",
                                                                "/login",
                                                                "/login.html",
                                                                "/register",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/img/**",
                                                                "/favicon.ico",
                                                                "/admin/**",
                                                                "/users/**")
                                                .permitAll()

                                                // 🔓 LOGIN / REGISTER
                                                .requestMatchers(HttpMethod.POST,
                                                                "/api/auth/login",
                                                                "/api/auth/register")
                                                .permitAll()

                                                .requestMatchers(
                                                                "/api/auth/profile",
                                                                "/api/bookings/**")
                                                .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/api/auth/change-password")
                                                .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                                                .requestMatchers("/user/api/**")
                                                .hasAuthority("ROLE_USER") // Only normal users can access

                                                // 🛠 ADMIN APIs
                                                .requestMatchers("/api/admin/**")
                                                .hasAuthority("ROLE_ADMIN")

                                                .anyRequest().authenticated())

                                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(
                        AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
