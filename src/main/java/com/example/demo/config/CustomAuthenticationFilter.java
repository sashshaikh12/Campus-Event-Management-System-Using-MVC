package com.example.demo.config;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserRepository userRepository;
    private boolean postOnly = true;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
        
        // Set the success and failure handlers
        setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/dashboard"));
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error=true"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String role = request.getParameter("role");
        
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        if (role == null) {
            role = "";
        }
        
        username = username.trim();
        role = role.trim();
        
        System.out.println("Attempting authentication for username: " + username + ", role: " + role);
        
        // Find the user in the database
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            System.out.println("User found: " + user.getUsername() + ", role: " + user.getRole());
            
            // Check if the password and role match
            if (password.equals(user.getPassword()) && role.equals(user.getRole())) {
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    username, 
                    password,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                );
                
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } else {
                System.out.println("Invalid credentials. Password or role doesn't match.");
                throw new BadCredentialsException("Invalid username/password/role combination");
            }
        } else {
            System.out.println("User not found: " + username);
            throw new BadCredentialsException("User not found");
        }
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
                                            FilterChain chain, Authentication authResult) 
                                            throws IOException, ServletException {
        System.out.println("Authentication successful for user: " + authResult.getName());
        super.successfulAuthentication(request, response, chain, authResult);
    }
    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
                                             AuthenticationException failed) 
                                             throws IOException, ServletException {
        System.out.println("Authentication failed: " + failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }
} 