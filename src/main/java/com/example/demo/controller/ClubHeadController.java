package com.example.demo.controller;

import com.example.demo.model.ClubHead;
import com.example.demo.model.Event;
import com.example.demo.model.EventRequest;
import com.example.demo.model.Faculty;
import com.example.demo.model.HOD;
import com.example.demo.model.Room;
import com.example.demo.model.RoomManager;
import com.example.demo.model.RoomRequest;
import com.example.demo.model.User;
import com.example.demo.model.service.ClubHeadService;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoomRepository;
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
import java.util.UUID;

@Controller
@RequestMapping("/clubhead")
public class ClubHeadController {

    @Autowired
    private ClubHeadService clubHeadService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // Use a default club head
            Optional<User> userOpt = userRepository.findByUsername("clubhead");
            
            if (userOpt.isPresent() && userOpt.get() instanceof ClubHead) {
                ClubHead clubHead = (ClubHead) userOpt.get();
                List<Event> events = clubHeadService.getClubHeadEvents(clubHead);
                
                model.addAttribute("clubHead", clubHead);
                model.addAttribute("events", events != null ? events : new ArrayList<>());
                
                return "clubhead/dashboard";
            } else {
                // Create a temporary ClubHead if not found
                ClubHead clubHead = new ClubHead();
                clubHead.setId(UUID.randomUUID().toString());
                clubHead.setUsername("clubhead");
                clubHead.setName("Club Head User");
                clubHead.setEmail("clubhead@example.com");
                clubHead.setRole("CLUB_HEAD");
                clubHead.setClubName("Tech Club");
                
                model.addAttribute("clubHead", clubHead);
                model.addAttribute("events", new ArrayList<>());
                
                return "clubhead/dashboard";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
            model.addAttribute("stackTrace", e.getStackTrace());
            return "error";
        }
    }
    
    @GetMapping("/events/create")
    public String showCreateEventForm(Model model) {
        model.addAttribute("event", new Event());
        
        // Add faculties to the model for selection
        List<Faculty> faculties = userRepository.findAll().stream()
                .filter(user -> "FACULTY".equals(user.getRole()))
                .filter(user -> user instanceof Faculty)
                .map(user -> (Faculty) user)
                .toList();
        
        model.addAttribute("faculties", faculties);
        
        return "clubhead/create-event";
    }
    
    @PostMapping("/events/create")
    public String createEvent(@ModelAttribute Event eventForm,
                             @RequestParam String facultyId,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        
        // Use a default club head
        Optional<User> userOpt = userRepository.findByUsername("clubhead");
        Optional<ClubHead> clubHeadOpt = Optional.empty();
        if (userOpt.isPresent() && userOpt.get() instanceof ClubHead) {
            clubHeadOpt = Optional.of((ClubHead) userOpt.get());
        }
        
        // Get the selected faculty
        Optional<Faculty> facultyOpt = userRepository.findById(facultyId)
                .filter(user -> user instanceof Faculty)
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
                    .filter(user -> user instanceof HOD)
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
    public String listEvents(Model model) {
        // Use a default club head
        Optional<User> userOpt = userRepository.findByUsername("clubhead");
        
        if (userOpt.isPresent() && userOpt.get() instanceof ClubHead) {
            ClubHead clubHead = (ClubHead) userOpt.get();
            List<Event> events = clubHeadService.getClubHeadEvents(clubHead);
            
            model.addAttribute("events", events);
            return "clubhead/events";
        }
        
        // If clubhead not found, return empty list
        model.addAttribute("events", new ArrayList<>());
        return "clubhead/events";
    }
    
    @GetMapping("/room-request/{eventId}")
    public String showRoomRequestForm(@PathVariable String eventId, Model model) {
        // Use a default club head
        Optional<User> userOpt = userRepository.findByUsername("clubhead");
        
        if (userOpt.isPresent() && userOpt.get() instanceof ClubHead) {
            ClubHead clubHead = (ClubHead) userOpt.get();
            Optional<Event> eventOpt = clubHead.getCreatedEvents().stream()
                    .filter(e -> e.getId().equals(eventId))
                    .findFirst();
            
            if (eventOpt.isPresent()) {
                Event event = eventOpt.get();
                model.addAttribute("event", event);
                
                // Add rooms for selection from the database
                List<Room> rooms = roomRepository.findAll();
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
                                   Model model) {
        
        // Use a default club head
        Optional<User> userOpt = userRepository.findByUsername("clubhead");
        
        if (userOpt.isPresent() && userOpt.get() instanceof ClubHead) {
            ClubHead clubHead = (ClubHead) userOpt.get();
            Optional<Event> eventOpt = clubHead.getCreatedEvents().stream()
                    .filter(e -> e.getId().equals(eventId))
                    .findFirst();
            
            if (eventOpt.isPresent() && roomId != null) {
                Event event = eventOpt.get();
                
                // Get the room from the database
                Optional<Room> roomOpt = roomRepository.findById(roomId);
                if (roomOpt.isEmpty()) {
                    model.addAttribute("error", "Room not found");
                    return "error";
                }
                Room room = roomOpt.get();
                
                // Get the room manager from database
                Optional<RoomManager> roomManagerOpt = userRepository.findAll().stream()
                        .filter(user -> "ROOM_MANAGER".equals(user.getRole()))
                        .filter(user -> user instanceof RoomManager)
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
    public String notifyStudents(@PathVariable String eventId) {
        // Use a default club head
        Optional<User> userOpt = userRepository.findByUsername("clubhead");
        
        if (userOpt.isPresent() && userOpt.get() instanceof ClubHead) {
            ClubHead clubHead = (ClubHead) userOpt.get();
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