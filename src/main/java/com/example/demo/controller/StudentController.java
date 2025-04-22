package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "student/dashboard";
    }
    
    @GetMapping("/events")
    public String events() {
        return "student/events";
    }
    
    @GetMapping("/events/registered")
    public String registeredEvents() {
        return "student/registered-events";
    }
    
    @GetMapping("/appeals")
    public String appeals() {
        return "student/appeals";
    }
    
    @GetMapping("/appeals/create")
    public String createAppeal() {
        return "student/create-appeal";
    }
    
    @GetMapping("/profile")
    public String profile() {
        return "student/profile";
    }
} 