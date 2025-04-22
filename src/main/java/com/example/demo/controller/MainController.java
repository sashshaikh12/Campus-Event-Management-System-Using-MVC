package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLUB_HEAD"))) {
                return "redirect:/clubhead/dashboard";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_FACULTY"))) {
                return "redirect:/faculty/dashboard";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_HOD"))) {
                return "redirect:/hod/dashboard";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ROOM_MANAGER"))) {
                return "redirect:/roommanager/dashboard";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"))) {
                return "redirect:/student/dashboard";
            }
        }
        return "redirect:/login";
    }
} 