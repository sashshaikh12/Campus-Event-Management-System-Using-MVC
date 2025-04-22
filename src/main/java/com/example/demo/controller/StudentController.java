package com.example.demo.controller;

import com.example.demo.model.Appeal;
import com.example.demo.model.Event;
import com.example.demo.model.HOD;
import com.example.demo.model.Student;
import com.example.demo.model.service.StudentService;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // For demo purposes, we'll assume a Student with id "5"
        Optional<Student> studentOpt = userRepository.findById("5")
                .map(user -> (Student) user);
        
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            List<Event> upcomingEvents = studentService.getUpcomingEvents(student);
            boolean isBlacklisted = studentService.isBlacklisted(student);
            int absenceCount = studentService.getAbsenceCount(student);
            
            model.addAttribute("student", student);
            model.addAttribute("upcomingEvents", upcomingEvents);
            model.addAttribute("isBlacklisted", isBlacklisted);
            model.addAttribute("absenceCount", absenceCount);
            
            return "student/dashboard";
        }
        
        return "redirect:/login";
    }
    
    @GetMapping("/events")
    public String listEvents(Model model) {
        // For demo purposes, we'll get all events
        // In a real application, you would filter events by criteria
        List<Event> events = userRepository.findAll().stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .findFirst()
                .map(Student::getRegisteredEvents)
                .orElse(List.of());
        
        // Get the current student
        Optional<Student> studentOpt = userRepository.findById("5")
                .map(user -> (Student) user);
        
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            boolean isBlacklisted = studentService.isBlacklisted(student);
            
            model.addAttribute("events", events);
            model.addAttribute("student", student);
            model.addAttribute("isBlacklisted", isBlacklisted);
            
            return "student/events";
        }
        
        return "redirect:/login";
    }
    
    @PostMapping("/events/{eventId}/register")
    public String registerForEvent(@PathVariable String eventId) {
        Optional<Student> studentOpt = userRepository.findById("5")
                .map(user -> (Student) user);
        
        Optional<Event> eventOpt = userRepository.findAll().stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .flatMap(student -> student.getRegisteredEvents().stream())
                .filter(event -> event.getId().equals(eventId))
                .findFirst();
        
        if (studentOpt.isPresent() && eventOpt.isPresent()) {
            Student student = studentOpt.get();
            Event event = eventOpt.get();
            
            studentService.registerForEvent(student, event);
            return "redirect:/student/events";
        }
        
        return "redirect:/student/dashboard";
    }
    
    @GetMapping("/blacklist-appeal")
    public String showAppealForm(Model model) {
        Optional<Student> studentOpt = userRepository.findById("5")
                .map(user -> (Student) user);
        
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            
            if (studentService.isBlacklisted(student)) {
                model.addAttribute("student", student);
                return "student/appeal-form";
            } else {
                return "redirect:/student/dashboard";
            }
        }
        
        return "redirect:/login";
    }
    
    @PostMapping("/blacklist-appeal")
    public String submitAppeal(@RequestParam String reason) {
        Optional<Student> studentOpt = userRepository.findById("5")
                .map(user -> (Student) user);
        
        // Find an HOD to submit the appeal to
        Optional<HOD> hodOpt = userRepository.findAll().stream()
                .filter(user -> "HOD".equals(user.getRole()))
                .map(user -> (HOD) user)
                .findFirst();
        
        if (studentOpt.isPresent() && hodOpt.isPresent()) {
            Student student = studentOpt.get();
            HOD hod = hodOpt.get();
            
            if (studentService.isBlacklisted(student)) {
                Appeal appeal = studentService.submitAppeal(student, hod, reason);
                return "redirect:/student/dashboard";
            }
        }
        
        return "redirect:/student/dashboard";
    }
    
    @GetMapping("/registered-events")
    public String listRegisteredEvents(Model model) {
        Optional<Student> studentOpt = userRepository.findById("5")
                .map(user -> (Student) user);
        
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            List<Event> registeredEvents = studentService.getRegisteredEvents(student);
            
            model.addAttribute("events", registeredEvents);
            return "student/registered-events";
        }
        
        return "redirect:/login";
    }
} 