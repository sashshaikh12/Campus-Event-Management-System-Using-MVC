package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clubhead")
public class ClubHeadController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "clubhead/dashboard";
    }
    
    @GetMapping("/events")
    public String events() {
        return "clubhead/events";
    }
    
    @GetMapping("/events/create")
    public String createEvent() {
        return "clubhead/create-event";
    }
} 