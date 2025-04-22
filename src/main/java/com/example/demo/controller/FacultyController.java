package com.example.demo.controller;

import com.example.demo.model.Event;
import com.example.demo.model.Faculty;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/faculty")
public class FacultyController {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Fixed faculty ID for demo purposes
    private Faculty getCurrentFaculty() {
        try {
            // Using a fixed faculty ID: user-101
            Optional<Faculty> facultyOpt = userRepository.findById("user-101")
                    .filter(user -> user instanceof Faculty)
                    .map(user -> (Faculty) user);
            
            return facultyOpt.orElseThrow(() -> new RuntimeException("Faculty with ID user-101 not found"));
        } catch (Exception e) {
            System.err.println("Error getting current faculty: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "faculty/dashboard";
    }
    
    @GetMapping("/events")
    public String events(Model model) {
        try {
            // Get current faculty
            Faculty faculty = getCurrentFaculty();
            model.addAttribute("faculty", faculty);
            
            // Get all events associated with this faculty
            List<Event> facultyEvents = eventRepository.findByFacultyOrderByStartDateTimeDesc(faculty);
            model.addAttribute("events", facultyEvents);
            
            // Count of events by status
            long pendingCount = facultyEvents.stream().filter(e -> "PENDING".equals(e.getStatus())).count();
            long completedCount = facultyEvents.stream().filter(e -> "COMPLETED".equals(e.getStatus())).count();
            long upcomingCount = facultyEvents.stream().filter(e -> "APPROVED".equals(e.getStatus())).count();
            
            model.addAttribute("pendingCount", pendingCount);
            model.addAttribute("completedCount", completedCount);
            model.addAttribute("upcomingCount", upcomingCount);
            
            return "faculty/events";
        } catch (Exception e) {
            System.err.println("Error in faculty events: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading events");
            return "error";
        }
    }
    
    @GetMapping("/students/blacklisted")
    public String blacklistedStudents() {
        return "faculty/blacklisted-students";
    }
    
    @GetMapping("/events/{id}/take-attendance")
    public String takeAttendance(@PathVariable("id") String id) {
        return "faculty/take-attendance";
    }
    
    @GetMapping("/profile")
    public String profile() {
        return "faculty/profile";
    }
}