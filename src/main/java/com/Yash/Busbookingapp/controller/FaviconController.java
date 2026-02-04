package com.Yash.Busbookingapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FaviconController {
    @RequestMapping("favicon.ico")
    public void returnNoFavicon() {
        // Empty handler to avoid 404
    }
}
