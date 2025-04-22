package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "home";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Get user roles
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .orElse("");
        
        switch (role) {
            case "CLUB_HEAD":
                return "redirect:/clubhead/dashboard";
            case "FACULTY":
                return "redirect:/faculty/dashboard";
            case "HOD":
                return "redirect:/hod/dashboard";
            case "ROOM_MANAGER":
                return "redirect:/roommanager/dashboard";
            case "STUDENT":
                return "redirect:/student/dashboard";
            default:
                return "redirect:/login";
        }
    }
} 