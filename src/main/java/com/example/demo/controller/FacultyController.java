package com.example.demo.controller;

import com.example.demo.model.Event;
import com.example.demo.model.Faculty;
import com.example.demo.model.Student;
import com.example.demo.model.iterator.StudentIterator;
import com.example.demo.model.service.FacultyService;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/faculty")
public class FacultyController {

    @Autowired
    private FacultyService facultyService;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // In a real application, you would get the current user from authentication
        // For demo purposes, we'll assume a faculty with id "2"
        Optional<Faculty> facultyOpt = userRepository.findById("2")
                .map(user -> (Faculty) user);
        
        if (facultyOpt.isPresent()) {
            Faculty faculty = facultyOpt.get();
            List<Event> events = facultyService.getFacultyEvents(faculty);
            
            model.addAttribute("faculty", faculty);
            model.addAttribute("events", events);
            
            return "faculty/dashboard";
        }
        
        return "redirect:/login";
    }
    
    @GetMapping("/events")
    public String listEvents(Model model) {
        Optional<Faculty> facultyOpt = userRepository.findById("2")
                .map(user -> (Faculty) user);
        
        if (facultyOpt.isPresent()) {
            Faculty faculty = facultyOpt.get();
            List<Event> events = facultyService.getFacultyEvents(faculty);
            
            model.addAttribute("events", events);
            return "faculty/events";
        }
        
        return "redirect:/login";
    }
    
    @GetMapping("/events/{eventId}/students")
    public String listEventStudents(@PathVariable String eventId, Model model) {
        Optional<Faculty> facultyOpt = userRepository.findById("2")
                .map(user -> (Faculty) user);
        
        if (facultyOpt.isPresent()) {
            Faculty faculty = facultyOpt.get();
            Optional<Event> eventOpt = faculty.getManagedEvents().stream()
                    .filter(e -> e.getId().equals(eventId))
                    .findFirst();
            
            if (eventOpt.isPresent()) {
                Event event = eventOpt.get();
                
                // Get registered students using the iterator pattern
                List<Student> registeredStudents = facultyService.getRegisteredStudents(event);
                List<Student> attendedStudents = facultyService.getAttendedStudents(event);
                
                model.addAttribute("event", event);
                model.addAttribute("registeredStudents", registeredStudents);
                model.addAttribute("attendedStudents", attendedStudents);
                
                return "faculty/event-students";
            }
        }
        
        return "redirect:/faculty/events";
    }
    
    @GetMapping("/events/{eventId}/attendance")
    public String showAttendanceForm(@PathVariable String eventId, Model model) {
        Optional<Faculty> facultyOpt = userRepository.findById("2")
                .map(user -> (Faculty) user);
        
        if (facultyOpt.isPresent()) {
            Faculty faculty = facultyOpt.get();
            Optional<Event> eventOpt = faculty.getManagedEvents().stream()
                    .filter(e -> e.getId().equals(eventId))
                    .findFirst();
            
            if (eventOpt.isPresent()) {
                Event event = eventOpt.get();
                List<Student> registeredStudents = facultyService.getRegisteredStudents(event);
                
                model.addAttribute("event", event);
                model.addAttribute("students", registeredStudents);
                
                return "faculty/take-attendance";
            }
        }
        
        return "redirect:/faculty/events";
    }
    
    @PostMapping("/events/{eventId}/attendance")
    public String takeAttendance(@PathVariable String eventId, 
                                @RequestParam(value = "presentStudents", required = false) List<String> presentStudentIds) {
        
        Optional<Faculty> facultyOpt = userRepository.findById("2")
                .map(user -> (Faculty) user);
        
        if (facultyOpt.isPresent()) {
            Faculty faculty = facultyOpt.get();
            Optional<Event> eventOpt = faculty.getManagedEvents().stream()
                    .filter(e -> e.getId().equals(eventId))
                    .findFirst();
         
            if (eventOpt.isPresent()) {
                Event event = eventOpt.get();
                List<Student> registeredStudents = facultyService.getRegisteredStudents(event);
             
                // If no students are marked as present, initialize an empty list
                final List<String> finalPresentStudentIds = presentStudentIds != null ? presentStudentIds : new ArrayList<>();
                 
                // Filter present students based on selected IDs
                List<Student> presentStudents = registeredStudents.stream()
                    .filter(student -> finalPresentStudentIds.contains(student.getId()))
                    .collect(Collectors.toList());
             
                // Take attendance using the service
                facultyService.takeAttendance(event, presentStudents);
             
                return "redirect:/faculty/events/" + eventId + "/students";
            }
        }
         
        return "redirect:/faculty/events";
    }
    
    @GetMapping("/students/blacklisted")
    public String listBlacklistedStudents(Model model) {
        // Get all students who are blacklisted
        List<Student> blacklistedStudents = userRepository.findAll().stream()
                .filter(user -> "STUDENT".equals(user.getRole()))
                .map(user -> (Student) user)
                .filter(Student::isBlacklisted)
                .collect(Collectors.toList());
        
        model.addAttribute("students", blacklistedStudents);
        return "faculty/blacklisted-students";
    }
    
    @GetMapping("/students/{studentId}/attendance")
    public String viewStudentAttendance(@PathVariable String studentId, Model model) {
        Optional<Student> studentOpt = userRepository.findById(studentId)
                .map(user -> (Student) user);
        
        Optional<Faculty> facultyOpt = userRepository.findById("2")
                .map(user -> (Faculty) user);
        
        if (studentOpt.isPresent() && facultyOpt.isPresent()) {
            Student student = studentOpt.get();
            Faculty faculty = facultyOpt.get();
            
            List<Event> events = facultyService.getFacultyEvents(faculty);
            double attendancePercentage = facultyService.calculateAttendancePercentage(student, events);
            
            model.addAttribute("student", student);
            model.addAttribute("events", events);
            model.addAttribute("attendancePercentage", attendancePercentage);
            
            return "faculty/student-attendance";
        }
        
        return "redirect:/faculty/dashboard";
    }
} 