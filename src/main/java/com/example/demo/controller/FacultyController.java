package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/faculty")
public class FacultyController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "faculty/dashboard";
    }
} 