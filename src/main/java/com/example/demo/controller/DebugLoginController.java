package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class DebugLoginController {

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/debug-login")
    public String showDebugLoginForm(Model model) {
        List<User> allUsers = userRepository.findAll();
        model.addAttribute("users", allUsers);
        return "debug-login";
    }
    
    @PostMapping("/debug-login")
    public String debugLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request,
            Model model) {
        
        System.out.println("Debug login attempt with username: " + username + ", password: " + password);
        
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("User found: " + user.getUsername() + ", stored password: " + user.getPassword());
            
            if (password.equals(user.getPassword())) {
                System.out.println("Password matches. Setting authentication in security context.");
                
                // Create authentication token
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                );
                
                // Set authentication in security context
                SecurityContext sc = new SecurityContextImpl();
                sc.setAuthentication(auth);
                SecurityContextHolder.setContext(sc);
                
                // Set security context in session
                HttpSession session = request.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
                
                System.out.println("Authentication set in security context and session.");
                
                // Redirect based on role
                switch (user.getRole()) {
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
                        return "redirect:/";
                }
            } else {
                System.out.println("Password does not match.");
                model.addAttribute("error", "Invalid password");
                model.addAttribute("users", userRepository.findAll());
                return "debug-login";
            }
        } else {
            System.out.println("User not found in database: " + username);
            model.addAttribute("error", "User not found");
            model.addAttribute("users", userRepository.findAll());
            return "debug-login";
        }
    }
} 