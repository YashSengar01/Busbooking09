package com.Yash.Busbookingapp.controller;

import com.Yash.Busbookingapp.model.Bus;
import com.Yash.Busbookingapp.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PageBookingController {

    @Autowired
    private BusRepository busRepository;

    @GetMapping("/booking")
    public String showBookingPage(Model model) {
        List<Bus> buses = busRepository.findAll();
        model.addAttribute("buses", buses);
        return "booking"; // refers to booking.html Thymeleaf page
    }
}
