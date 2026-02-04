package com.Yash.Busbookingapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PageController {

    @GetMapping("/register")
    public String registerpage() {
        return "register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Refers to "login.html" in the templates folder
    }

    @GetMapping("/users/profile")
    public String profilePage() {
        return "users/profile"; // profile.html in templates
    }

    @GetMapping("/users/booking")
    public String bookingsPage() {
        return "users/booking"; // bookings.html in templates
    }

    @GetMapping("/users/history")
    public String bookingHistoryPage() {
        return "users/history";
    }

    @GetMapping("/users/bus-finder")
    public String userBusFinder() {
        return "users/user-bus-finder";
    }

    @GetMapping("/favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
        // Do nothing or log if needed
    }

    @GetMapping("/admin/Adminbus")
    public String adminBusPage() {
        return "admin/Adminbus"; // maps to adminbus.html in /templates
    }

    @GetMapping("/admin/Adminprofile")
    public String adminProfilePage() {
        return "admin/Adminprofile"; // maps to adminprofile.html in /templates
    }

    @GetMapping("/users/dashboard")
    public String dashboard() {
        return "users/dashboard"; // Thymeleaf -> templates/users/dashboard.html
    }

    @GetMapping("/admin/Admindashboard")
    public String admindashboard() {
        return "admin/Admindashboard"; // templates/admin/Admindashboard.html
    }

}
