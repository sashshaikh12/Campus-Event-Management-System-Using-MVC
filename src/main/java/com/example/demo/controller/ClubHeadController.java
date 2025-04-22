package com.example.demo.controller;

import com.example.demo.model.ClubHead;
import com.example.demo.model.Event;
import com.example.demo.model.EventRequest;
import com.example.demo.model.Faculty;
import com.example.demo.model.HOD;
import com.example.demo.model.Room;
import com.example.demo.model.RoomManager;
import com.example.demo.model.RoomRequest;
import com.example.demo.model.service.ClubHeadService;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/clubhead")
public class ClubHeadController {

    @Autowired
    private ClubHeadService clubHeadService;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated() || 
                !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLUB_HEAD"))) {
                return "redirect:/login";
            }
            
            // Get current username from authentication
            String username = authentication.getName();
            
            // Find the ClubHead user in the repository
            Optional<ClubHead> clubHeadOpt = userRepository.findByUsername(username)
                    .map(user -> (ClubHead) user);
            
            if (clubHeadOpt.isPresent()) {
                ClubHead clubHead = clubHeadOpt.get();
                List<Event> events = clubHeadService.getClubHeadEvents(clubHead);
                
                model.addAttribute("clubHead", clubHead);
                model.addAttribute("events", events != null ? events : new ArrayList<>());
                
                return "clubhead/dashboard";
            } else {
                model.addAttribute("error", "ClubHead user not found");
                return "error";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
            model.addAttribute("stackTrace", e.getStackTrace());
            return "error";
        }
    }
    
    @GetMapping("/events/create")
    public String showCreateEventForm(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || 
            !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLUB_HEAD"))) {
            return "redirect:/login";
        }
        
        model.addAttribute("event", new Event());
        // Add faculties to the model for selection
        List<Faculty> faculties = userRepository.findAll().stream()
                .filter(user -> "FACULTY".equals(user.getRole()))
                .map(user -> (Faculty) user)
                .toList();
        model.addAttribute("faculties", faculties);
        
        return "clubhead/create-event";
    }
    
    @PostMapping("/events/create")
    public String createEvent(@ModelAttribute Event eventForm,
                              @RequestParam String facultyId,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime,
                              Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated() || 
            !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLUB_HEAD"))) {
            return "redirect:/login";
        }
        
        // Get the current club head based on authenticated username
        String username = authentication.getName();
        Optional<ClubHead> clubHeadOpt = userRepository.findByUsername(username)
                .map(user -> (ClubHead) user);
        
        // Get the selected faculty
        Optional<Faculty> facultyOpt = userRepository.findById(facultyId)
                .map(user -> (Faculty) user);
        
        if (clubHeadOpt.isPresent() && facultyOpt.isPresent()) {
            ClubHead clubHead = clubHeadOpt.get();
            Faculty faculty = facultyOpt.get();
            
            // Create the event using the service
            Event event = clubHeadService.createEvent(
                clubHead, 
                eventForm.getName(), 
                eventForm.getDescription(),
                startDateTime,
                endDateTime,
                eventForm.getVenue(),
                eventForm.getMaxParticipants(),
                faculty
            );
            
            // Get the HOD to send the event request
            Optional<HOD> hodOpt = userRepository.findAll().stream()
                    .filter(user -> "HOD".equals(user.getRole()))
                    .map(user -> (HOD) user)
                    .findFirst();
            
            if (hodOpt.isPresent()) {
                HOD hod = hodOpt.get();
                // Create and send event request
                EventRequest eventRequest = clubHeadService.createEventRequest(event, hod);
            }
            
            return "redirect:/clubhead/events";
        }
        
        return "redirect:/clubhead/dashboard";
    }
    
    @GetMapping("/events")
    public String listEvents(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || 
            !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLUB_HEAD"))) {
            return "redirect:/login";
        }
        
        // Get the current club head based on authenticated username
        String username = authentication.getName();
        Optional<ClubHead> clubHeadOpt = userRepository.findByUsername(username)
                .map(user -> (ClubHead) user);
        
        if (clubHeadOpt.isPresent()) {
            ClubHead clubHead = clubHeadOpt.get();
            List<Event> events = clubHeadService.getClubHeadEvents(clubHead);
            
            model.addAttribute("events", events);
            return "clubhead/events";
        }
        
        return "redirect:/login";
    }
    
    @GetMapping("/room-request/{eventId}")
    public String showRoomRequestForm(@PathVariable String eventId, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || 
            !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLUB_HEAD"))) {
            return "redirect:/login";
        }
        
        // Get the current club head based on authenticated username
        String username = authentication.getName();
        Optional<ClubHead> clubHeadOpt = userRepository.findByUsername(username)
                .map(user -> (ClubHead) user);
        
        if (clubHeadOpt.isPresent()) {
            ClubHead clubHead = clubHeadOpt.get();
            Optional<Event> eventOpt = clubHead.getCreatedEvents().stream()
                    .filter(e -> e.getId().equals(eventId))
                    .findFirst();
            
            if (eventOpt.isPresent()) {
                Event event = eventOpt.get();
                model.addAttribute("event", event);
                
                // Add rooms for selection
                // In a real app, this would come from a room repository
                List<Room> rooms = List.of(new Room()); // Placeholder
                model.addAttribute("rooms", rooms);
                
                return "clubhead/room-request";
            }
        }
        
        return "redirect:/clubhead/events";
    }
    
    @PostMapping("/room-request/{eventId}")
    public String createRoomRequest(@PathVariable String eventId,
                                   @RequestParam String roomId,
                                   @RequestParam String requiredServices,
                                   Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated() || 
            !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLUB_HEAD"))) {
            return "redirect:/login";
        }
        
        // Get the current club head based on authenticated username
        String username = authentication.getName();
        Optional<ClubHead> clubHeadOpt = userRepository.findByUsername(username)
                .map(user -> (ClubHead) user);
        
        if (clubHeadOpt.isPresent()) {
            ClubHead clubHead = clubHeadOpt.get();
            Optional<Event> eventOpt = clubHead.getCreatedEvents().stream()
                    .filter(e -> e.getId().equals(eventId))
                    .findFirst();
            
            if (eventOpt.isPresent()) {
                Event event = eventOpt.get();
                
                // Get the room
                // In a real app, this would come from a room repository
                Room room = new Room(); // Placeholder
                
                // Get the room manager
                Optional<RoomManager> roomManagerOpt = userRepository.findAll().stream()
                        .filter(user -> "ROOM_MANAGER".equals(user.getRole()))
                        .map(user -> (RoomManager) user)
                        .findFirst();
                
                if (roomManagerOpt.isPresent()) {
                    RoomManager roomManager = roomManagerOpt.get();
                    
                    // Create room request
                    RoomRequest roomRequest = clubHeadService.createRoomRequest(
                            event, room, clubHead, requiredServices, roomManager);
                    
                    return "redirect:/clubhead/events";
                }
            }
        }
        
        return "redirect:/clubhead/dashboard";
    }
    
    @GetMapping("/notify-students/{eventId}")
    public String notifyStudents(@PathVariable String eventId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || 
            !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLUB_HEAD"))) {
            return "redirect:/login";
        }
        
        // Get the current club head based on authenticated username
        String username = authentication.getName();
        Optional<ClubHead> clubHeadOpt = userRepository.findByUsername(username)
                .map(user -> (ClubHead) user);
        
        if (clubHeadOpt.isPresent()) {
            ClubHead clubHead = clubHeadOpt.get();
            Optional<Event> eventOpt = clubHead.getCreatedEvents().stream()
                    .filter(e -> e.getId().equals(eventId))
                    .findFirst();
            
            if (eventOpt.isPresent()) {
                Event event = eventOpt.get();
                clubHeadService.notifyStudentsAboutEvent(event);
                return "redirect:/clubhead/events";
            }
        }
        
        return "redirect:/clubhead/dashboard";
    }
} 