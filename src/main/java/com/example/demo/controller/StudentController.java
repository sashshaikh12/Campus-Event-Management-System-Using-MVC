package com.example.demo.controller;

import com.example.demo.model.Appeal;
import com.example.demo.model.Event;
import com.example.demo.model.Student;
import com.example.demo.model.service.AppealService;
import com.example.demo.model.service.EventService;
import com.example.demo.model.service.StudentService;
import com.example.demo.repository.AppealRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private AppealService appealService;
    
    @Autowired
    private AppealRepository appealRepository;

    // Using a fixed student ID instead of authentication
    private Student getCurrentStudent() {
        try {
            // Fixed student ID: user-402 (Mary Student)
            Optional<Student> studentOpt = userRepository.findById("user-402")
                    .filter(user -> user instanceof Student)
                    .map(user -> (Student) user);
            
            return studentOpt.orElseThrow(() -> new RuntimeException("Fixed student with ID user-402 not found"));
        } catch (Exception e) {
            System.err.println("Error getting current student: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            Student student = getCurrentStudent();
            
            // Load data for the dashboard
            int registeredEventsCount = eventService.getRegisteredEventsCount(student);
            int upcomingEventsCount = eventService.getUpcomingEventsCount();
            int attendedEventsCount = eventService.getAttendedEventsCount(student);
            
            List<Event> upcomingStudentEvents = eventService.getUpcomingEventsRegisteredByStudent(student);
            List<Event> popularEvents = eventService.getPopularEvents(5);
            
            // Add data to the model
            model.addAttribute("student", student);
            model.addAttribute("registeredEventsCount", registeredEventsCount);
            model.addAttribute("upcomingEventsCount", upcomingEventsCount);
            model.addAttribute("attendedEventsCount", attendedEventsCount);
            model.addAttribute("upcomingStudentEvents", upcomingStudentEvents);
            model.addAttribute("popularEvents", popularEvents);
            
            // Add absence warning if needed
            if (student.getAbsenceCount() > 0) {
                model.addAttribute("showAbsenceWarning", true);
                model.addAttribute("absenceCount", student.getAbsenceCount());
            }
            
            return "student/dashboard";
        } catch (Exception e) {
            System.err.println("Error in dashboard: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading the dashboard");
            return "error";
        }
    }
    
    @GetMapping("/events")
    public String events(Model model) {
        try {
            Student student = getCurrentStudent();
            
            // Get all approved events (not just upcoming ones)
            List<Event> events = eventService.getAllApprovedEvents();
            System.out.println("Fetched " + events.size() + " approved events from database");
            
            // Add data to the model
            model.addAttribute("student", student);
            model.addAttribute("events", events);
            
            return "student/events";
        } catch (Exception e) {
            System.err.println("Error in events: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading events");
            return "error";
        }
    }
    
    @GetMapping("/events/registered")
    public String registeredEvents(Model model) {
        try {
            System.out.println("Starting registeredEvents method");
            Student student = getCurrentStudent();
            System.out.println("Got current student: " + student.getName());
            
            List<Event> registeredEvents = new ArrayList<>();
            
            if (student.getRegisteredEvents() != null) {
                registeredEvents = student.getRegisteredEvents();
                System.out.println("Found " + registeredEvents.size() + " registered events for student");
            } else {
                System.out.println("Student has no registered events (null)");
            }
            
            model.addAttribute("student", student);
            model.addAttribute("registeredEvents", registeredEvents);
            
            System.out.println("Completed registeredEvents method");
            return "student/registered-events";
        } catch (Exception e) {
            System.err.println("Error in registeredEvents: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading registered events");
            return "error";
        }
    }
    
    @PostMapping("/events/{id}/register")
    public String registerForEvent(@PathVariable("id") String eventId) {
        try {
            Student student = getCurrentStudent();
            
            Event event = eventService.getAllApprovedEvents().stream()
                .filter(e -> e.getId().equals(eventId))
                .findFirst()
                .orElse(null);
            
            if (event != null) {
                eventService.registerStudentForEvent(student, event);
                System.out.println("Registered student for event: " + event.getName());
            } else {
                System.err.println("Event with ID " + eventId + " not found");
            }
            
            return "redirect:/student/events";
        } catch (Exception e) {
            System.err.println("Error in registerForEvent: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/student/events?error=true";
        }
    }
    
    @GetMapping("/appeals")
    public String appeals(Model model) {
        try {
            Student student = getCurrentStudent();
            
            // Get all appeals submitted by this student
            List<Appeal> appeals = appealService.getAppealsByStudentId(student.getId());
            System.out.println("Found " + appeals.size() + " appeals for student: " + student.getName());
            
            model.addAttribute("student", student);
            model.addAttribute("appeals", appeals);
            
            return "student/appeals";
        } catch (Exception e) {
            System.err.println("Error in appeals: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading appeals");
            return "error";
        }
    }
    
    @PostMapping("/appeals/submit")
    public String submitAppeal(
            @RequestParam("studentId") String studentId,
            @RequestParam("hodId") String hodId,
            @RequestParam("reason") String reason,
            @RequestParam("description") String description,
            @RequestParam("status") String status,
            @RequestParam("isResolved") boolean isResolved,
            RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Submitting appeal for student ID: " + studentId);
            
            // Get the student object
            Student student = getCurrentStudent();
            
            // Get one of the student's registered events to use as eventId
            // If no registered events, get any event from the database
            String eventId = null;
            
            if (student.getRegisteredEvents() != null && !student.getRegisteredEvents().isEmpty()) {
                // Use first registered event
                eventId = student.getRegisteredEvents().get(0).getId();
                System.out.println("Using registered event ID: " + eventId);
            } else {
                // If no registered events, get any event from database
                List<Event> events = eventService.getAllApprovedEvents();
                if (events != null && !events.isEmpty()) {
                    eventId = events.get(0).getId();
                    System.out.println("Using approved event ID: " + eventId);
                }
            }
            
            if (eventId == null) {
                throw new RuntimeException("No events found in the database to associate with the appeal");
            }
            
            Appeal appeal = new Appeal();
            appeal.setStudentId(studentId);
            appeal.setHodId(hodId);
            appeal.setReason(reason);
            appeal.setDescription(description);
            appeal.setEventId(eventId); // Using a valid event ID from the database
            appeal.setStatus(status);
            appeal.setResolved(isResolved);
            
            // Set time-related fields
            LocalDateTime now = LocalDateTime.now();
            appeal.setAppealDate(now);
            appeal.setSubmissionTime(now);
            
            // Save to database
            appealRepository.save(appeal);
            
            System.out.println("Appeal created with ID: " + appeal.getId());
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Your appeal has been submitted successfully. The HOD will review your case shortly.");
            
            return "redirect:/student/appeals";
        } catch (Exception e) {
            System.err.println("Error in submitAppeal: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", 
                "An error occurred while submitting your appeal: " + e.getMessage());
            return "redirect:/student/appeals";
        }
    }
    
    @GetMapping("/appeals/create")
    public String createAppeal() {
        return "student/create-appeal";
    }
    
    @GetMapping("/profile")
    public String profile(Model model) {
        try {
            Student student = getCurrentStudent();
            model.addAttribute("student", student);
            return "student/profile";
        } catch (Exception e) {
            System.err.println("Error in profile: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading profile");
            return "error";
        }
    }
}