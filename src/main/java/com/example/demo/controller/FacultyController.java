package com.example.demo.controller;

import com.example.demo.model.Event;
import com.example.demo.model.Faculty;
import com.example.demo.model.Student;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
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
    public String takeAttendance(@PathVariable("id") String id, Model model) {
        try {
            // Get the event by ID
            Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));
            
            // Add event and registered students to the model
            model.addAttribute("event", event);
            model.addAttribute("registeredStudents", event.getRegisteredStudents());
            model.addAttribute("faculty", getCurrentFaculty());
            
            return "faculty/take-attendance";
        } catch (Exception e) {
            System.err.println("Error in takeAttendance: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading attendance page: " + e.getMessage());
            return "error";
        }
    }
    
    @PostMapping("/events/{id}/save-attendance")
    public String saveAttendance(
            @PathVariable("id") String id, 
            @RequestParam Map<String, String> formData,
            RedirectAttributes redirectAttributes) {
        try {
            // Get the event by ID
            Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));
            
            // Clear the current attendance list
            event.getAttendedStudents().clear();
            
            // Process form parameters to mark attendance
            for (String key : formData.keySet()) {
                if (key.startsWith("attendance-")) {
                    String studentId = key.substring("attendance-".length());
                    String attendanceStatus = formData.get(key);
                    
                    if ("present".equals(attendanceStatus)) {
                        // Find the student and mark as attended
                        userRepository.findById(studentId)
                            .filter(user -> user instanceof Student)
                            .map(user -> (Student) user)
                            .ifPresent(student -> event.markAttendance(student));
                    } else if ("absent".equals(attendanceStatus)) {
                        // Find the student and mark as absent
                        userRepository.findById(studentId)
                            .filter(user -> user instanceof Student)
                            .map(user -> (Student) user)
                            .ifPresent(student -> event.markAbsent(student));
                    }
                }
            }
            
            // Save the updated event
            eventRepository.save(event);
            
            redirectAttributes.addFlashAttribute("successMessage", "Attendance has been recorded successfully");
            return "redirect:/faculty/events";
        } catch (Exception e) {
            System.err.println("Error in saveAttendance: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error recording attendance: " + e.getMessage());
            return "redirect:/faculty/events";
        }
    }
    
    @GetMapping("/profile")
    public String profile() {
        return "faculty/profile";
    }
}