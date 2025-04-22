package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Allow all requests without authentication - completely disable security for demo
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF protection
            .cors(cors -> cors.disable())  // Disable CORS
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll()  // Allow all requests without authentication
            )
            .formLogin(formLogin -> formLogin.disable())  // Disable form login
            .httpBasic(basic -> basic.disable())  // Disable HTTP Basic
            .logout(logout -> logout.disable())   // Disable logout
            .anonymous(anonymous -> anonymous.disable());  // Disable anonymous
        
        return http.build();
    }
} 