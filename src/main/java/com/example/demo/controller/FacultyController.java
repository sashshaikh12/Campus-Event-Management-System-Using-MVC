package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/faculty")
public class FacultyController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "faculty/dashboard";
    }
    
    @GetMapping("/events")
    public String events() {
        return "faculty/events";
    }
    
    @GetMapping("/students/blacklisted")
    public String blacklistedStudents() {
        return "faculty/blacklisted-students";
    }
    
    @GetMapping("/events/{id}/take-attendance")
    public String takeAttendance(@PathVariable("id") String id) {
        return "faculty/take-attendance";
    }
    
    @GetMapping("/profile")
    public String profile() {
        return "faculty/profile";
    }
} 