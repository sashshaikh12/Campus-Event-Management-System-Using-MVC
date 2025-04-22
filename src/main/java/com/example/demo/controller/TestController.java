package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @GetMapping("/test-public")
    @ResponseBody
    public String testPublic() {
        return "This is a public endpoint that anyone can access";
    }
    
    @GetMapping("/test-private")
    @ResponseBody
    public String testPrivate() {
        return "This is a private endpoint that only authenticated users can access";
    }
} 