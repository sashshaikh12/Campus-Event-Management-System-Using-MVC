package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    @GetMapping("/events/{id}/edit")
    public String editEvent(@PathVariable("id") String id) {
        return "clubhead/edit-event";
    }
    
    @GetMapping("/events/{id}/attendance")
    public String eventAttendance(@PathVariable("id") String id) {
        return "clubhead/event-attendance";
    }
    
    @GetMapping("/profile")
    public String profile() {
        return "clubhead/profile";
    }
    
    @GetMapping("/room-requests")
    public String roomRequests() {
        return "clubhead/room-requests";
    }
} 