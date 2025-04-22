package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/roommanager")
public class RoomManagerController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "roommanager/dashboard";
    }
} 