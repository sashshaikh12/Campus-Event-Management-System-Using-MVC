package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hod")
public class HODController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "hod/dashboard";
    }
} 