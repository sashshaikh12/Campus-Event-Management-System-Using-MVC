package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AuthConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails clubhead = User.builder()
            .username("clubhead")
            .password(passwordEncoder().encode("password"))
            .roles("CLUB_HEAD")
            .build();
        
        UserDetails faculty = User.builder()
            .username("faculty")
            .password(passwordEncoder().encode("password"))
            .roles("FACULTY")
            .build();
        
        UserDetails hod = User.builder()
            .username("hod")
            .password(passwordEncoder().encode("password"))
            .roles("HOD")
            .build();
        
        UserDetails student = User.builder()
            .username("student")
            .password(passwordEncoder().encode("password"))
            .roles("STUDENT")
            .build();
        
        UserDetails roommanager = User.builder()
            .username("roommanager")
            .password(passwordEncoder().encode("password"))
            .roles("ROOM_MANAGER")
            .build();
        
        return new InMemoryUserDetailsManager(clubhead, faculty, hod, student, roommanager);
    }
} 