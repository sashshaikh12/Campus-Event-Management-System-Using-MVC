package com.example.demo.controller;

import com.example.demo.model.ClubHead;
import com.example.demo.model.Event;
import com.example.demo.model.Faculty;
import com.example.demo.model.service.EventService;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/clubhead")
public class ClubHeadController {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get the fixed ClubHead for demo purposes
     */
    private ClubHead getCurrentClubHead() {
        try {
            // Using a fixed club head ID: user-301 (Tech Club)
            Optional<ClubHead> clubHeadOpt = userRepository.findById("user-003")
                    .filter(user -> user instanceof ClubHead)
                    .map(user -> (ClubHead) user);
            
            return clubHeadOpt.orElseThrow(() -> new RuntimeException("Fixed ClubHead with ID user-003 not found"));
        } catch (Exception e) {
            System.err.println("Error getting current ClubHead: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            ClubHead clubHead = getCurrentClubHead();
            List<Event> events = eventService.getEventsByClubHead(clubHead.getId());
            
            // Count events by status
            long pendingEvents = events.stream().filter(e -> "PENDING".equals(e.getStatus())).count();
            long approvedEvents = events.stream().filter(e -> "APPROVED".equals(e.getStatus())).count();
            long rejectedEvents = events.stream().filter(e -> "REJECTED".equals(e.getStatus())).count();
            
            model.addAttribute("clubHead", clubHead);
            model.addAttribute("pendingEventsCount", pendingEvents);
            model.addAttribute("approvedEventsCount", approvedEvents);
            model.addAttribute("rejectedEventsCount", rejectedEvents);
            model.addAttribute("totalEventsCount", events.size());
            
            // Get recent events (limit to 5)
            List<Event> recentEvents = events.stream()
                .sorted((e1, e2) -> e2.getCreatedAt().compareTo(e1.getCreatedAt()))
                .limit(5)
                .collect(Collectors.toList());
                
            model.addAttribute("recentEvents", recentEvents);
            
            return "clubhead/dashboard";
        } catch (Exception e) {
            System.err.println("Error in dashboard: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading the dashboard");
            return "error";
        }
    }
    
    @GetMapping("/events")
    public String events(Model model, @RequestParam(required = false) String status, 
                        @RequestParam(required = false) String sort) {
        try {
            ClubHead clubHead = getCurrentClubHead();
            List<Event> events = eventService.getEventsByClubHead(clubHead.getId());
            
            // Filter by status if provided
            if (status != null && !status.isEmpty()) {
                events = events.stream()
                    .filter(event -> status.equals(event.getStatus()))
                    .collect(Collectors.toList());
            }
            
            // Sort events if sort parameter is provided
            if (sort != null && !sort.isEmpty()) {
                switch (sort) {
                    case "name":
                        events = events.stream()
                            .sorted((e1, e2) -> e1.getName().compareToIgnoreCase(e2.getName()))
                            .collect(Collectors.toList());
                        break;
                    case "startDateTime":
                        events = events.stream()
                            .sorted((e1, e2) -> e1.getStartDateTime().compareTo(e2.getStartDateTime()))
                            .collect(Collectors.toList());
                        break;
                    case "createdAt":
                    default:
                        events = events.stream()
                            .sorted((e1, e2) -> e2.getCreatedAt().compareTo(e1.getCreatedAt()))
                            .collect(Collectors.toList());
                        break;
                }
            } else {
                // Default sort by creation date (newest first)
                events = events.stream()
                    .sorted((e1, e2) -> e2.getCreatedAt().compareTo(e1.getCreatedAt()))
                    .collect(Collectors.toList());
            }
            
            model.addAttribute("clubHead", clubHead);
            model.addAttribute("events", events);
            model.addAttribute("selectedStatus", status);
            model.addAttribute("selectedSort", sort != null ? sort : "createdAt");
            
            return "clubhead/events";
        } catch (Exception e) {
            System.err.println("Error in events: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading events");
            return "error";
        }
    }
    
    @GetMapping("/events/create")
    public String createEventForm(Model model) {
        try {
            ClubHead clubHead = getCurrentClubHead();
            
            // Get all faculties for dropdown
            List<Faculty> faculties = userRepository.findAll().stream()
                .filter(user -> user instanceof Faculty)
                .map(user -> (Faculty) user)
                .collect(Collectors.toList());
                
            model.addAttribute("clubHead", clubHead);
            model.addAttribute("faculties", faculties);
            
            return "clubhead/create-event";
        } catch (Exception e) {
            System.err.println("Error in createEventForm: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading the create event form");
            return "error";
        }
    }
    
    @PostMapping("/events/create")
    public String createEvent(@RequestParam("name") String name,
                             @RequestParam("description") String description,
                             @RequestParam("startDateTime") String startDateTime,
                             @RequestParam("endDateTime") String endDateTime,
                             @RequestParam("venue") String venue,
                             @RequestParam("maxParticipants") int maxParticipants,
                             @RequestParam("facultyId") String facultyId,
                             RedirectAttributes redirectAttributes) {
        try {
            ClubHead clubHead = getCurrentClubHead();
            
            // Create the event using the service
            Event event = eventService.createEvent(
                name, description, startDateTime, endDateTime,
                venue, maxParticipants, facultyId, clubHead.getId()
            );
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Event created successfully! It is now pending approval from the HOD.");
            
            return "redirect:/clubhead/events";
        } catch (Exception e) {
            System.err.println("Error in createEvent: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create event: " + e.getMessage());
            return "redirect:/clubhead/events/create";
        }
    }
    
    @GetMapping("/events/{id}/edit")
    public String editEvent(@PathVariable("id") String id, Model model) {
        try {
            ClubHead clubHead = getCurrentClubHead();
            
            // Check if the event exists and belongs to this club head
            Event event = eventService.getEventById(id)
                    .orElseThrow(() -> new RuntimeException("Event not found"));
                    
            // Verify ownership
            if (!event.getOrganizer().getId().equals(clubHead.getId())) {
                throw new RuntimeException("Not authorized to edit this event");
            }
            
            // Get all faculties for dropdown
            List<Faculty> faculties = userRepository.findAll().stream()
                .filter(user -> user instanceof Faculty)
                .map(user -> (Faculty) user)
                .collect(Collectors.toList());
                
            model.addAttribute("clubHead", clubHead);
            model.addAttribute("event", event);
            model.addAttribute("faculties", faculties);
            
            return "clubhead/edit-event";
        } catch (Exception e) {
            System.err.println("Error in editEvent: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/events/{id}/attendance")
    public String eventAttendance(@PathVariable("id") String id, Model model) {
        try {
            ClubHead clubHead = getCurrentClubHead();
            
            // Check if the event exists and belongs to this club head
            Event event = eventService.getEventById(id)
                    .orElseThrow(() -> new RuntimeException("Event not found"));
                    
            // Verify ownership
            if (!event.getOrganizer().getId().equals(clubHead.getId())) {
                throw new RuntimeException("Not authorized to manage attendance for this event");
            }
            
            model.addAttribute("clubHead", clubHead);
            model.addAttribute("event", event);
            model.addAttribute("registeredStudents", event.getRegisteredStudents());
            model.addAttribute("attendedStudents", event.getAttendedStudents());
            
            return "clubhead/event-attendance";
        } catch (Exception e) {
            System.err.println("Error in eventAttendance: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/profile")
    public String profile(Model model) {
        try {
            ClubHead clubHead = getCurrentClubHead();
            model.addAttribute("clubHead", clubHead);
            return "clubhead/profile";
        } catch (Exception e) {
            System.err.println("Error in profile: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading the profile");
            return "error";
        }
    }
    
    @GetMapping("/room-requests")
    public String roomRequests(Model model) {
        try {
            ClubHead clubHead = getCurrentClubHead();
            model.addAttribute("clubHead", clubHead);
            return "clubhead/room-requests";
        } catch (Exception e) {
            System.err.println("Error in roomRequests: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading room requests");
            return "error";
        }
    }
}