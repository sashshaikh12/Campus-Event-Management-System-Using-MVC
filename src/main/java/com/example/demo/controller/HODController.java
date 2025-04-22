package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/hod")
public class HODController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "hod/dashboard";
    }

    @GetMapping("/event-requests")
    public String eventRequests() {
        return "hod/event-requests";
    }

    @PostMapping("/event-requests/{id}/approve")
    public String approveEvent(@PathVariable("id") String id) {
        // Will implement the actual approval logic later
        return "redirect:/hod/event-requests";
    }

    @PostMapping("/event-requests/{id}/reject")
    public String rejectEvent(@PathVariable("id") String id, @RequestParam("reason") String reason) {
        // Will implement the actual rejection logic later
        return "redirect:/hod/event-requests";
    }

    @GetMapping("/appeals")
    public String appeals() {
        return "hod/appeals";
    }
    
    @PostMapping("/appeals/{id}/approve")
    public String approveAppeal(@PathVariable("id") Long id, @RequestParam(value = "response", required = false) String response) {
        // Will implement the actual approval logic later
        return "redirect:/hod/appeals";
    }
    
    @PostMapping("/appeals/{id}/reject")
    public String rejectAppeal(@PathVariable("id") Long id, @RequestParam("reason") String reason) {
        // Will implement the actual rejection logic later
        return "redirect:/hod/appeals";
    }
    
    @PostMapping("/appeals/{id}/request-info")
    public String requestMoreInfo(@PathVariable("id") Long id, @RequestParam("message") String message) {
        // Will implement the actual request for more info logic later
        return "redirect:/hod/appeals";
    }
    
    @GetMapping("/profile")
    public String profile() {
        return "hod/profile";
    }
} 