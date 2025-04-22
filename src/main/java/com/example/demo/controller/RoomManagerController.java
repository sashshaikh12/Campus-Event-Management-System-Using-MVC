package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/roommanager")
public class RoomManagerController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "roommanager/dashboard";
    }
    
    @GetMapping("/rooms")
    public String rooms() {
        return "roommanager/rooms";
    }
    
    @GetMapping("/rooms/create")
    public String createRoom() {
        return "roommanager/create-room";
    }
    
    @GetMapping("/room-requests")
    public String roomRequests() {
        return "roommanager/room-requests";
    }
    
    @GetMapping("/profile")
    public String profile() {
        return "roommanager/profile";
    }
} 