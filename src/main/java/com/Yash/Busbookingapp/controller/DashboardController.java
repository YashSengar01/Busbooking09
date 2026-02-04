package com.Yash.Busbookingapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        // Extract role from authentication
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "admin/Admindashboard"; // Thymeleaf template: templates/admin/dashboard.html
        } else {
            return "users/dashboard"; // templates/users/dashboard.html
        }
    }
}
