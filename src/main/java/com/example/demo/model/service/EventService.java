package com.example.demo.model.service;

import com.example.demo.model.ClubHead;
import com.example.demo.model.Event;
import com.example.demo.model.EventRequest;
import com.example.demo.model.Faculty;
import com.example.demo.model.Student;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.EventRequestRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for handling Event-related operations
 */
@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private EventRequestRepository eventRequestRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Create a new event and corresponding event request
     * @param name Event name
     * @param description Event description
     * @param startDateTimeStr Start date and time string (format: yyyy-MM-dd HH:mm)
     * @param endDateTimeStr End date and time string (format: yyyy-MM-dd HH:mm)
     * @param venue Event venue
     * @param maxParticipants Maximum number of participants
     * @param facultyId ID of the faculty coordinator
     * @param clubHeadId ID of the club head creating the event
     * @return The created Event object, or null if creation failed
     */
    @Transactional
    public Event createEvent(String name, String description, String startDateTimeStr, String endDateTimeStr,
                             String venue, int maxParticipants, String facultyId, String clubHeadId) {
        try {
            // Parse date strings to LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeStr, formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeStr, formatter);
            
            // Validate dates
            if (startDateTime.isAfter(endDateTime)) {
                throw new IllegalArgumentException("Start date must be before end date");
            }
            
            if (startDateTime.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Event cannot start in the past");
            }
            
            // Get the ClubHead user
            Optional<ClubHead> clubHeadOpt = userRepository.findById(clubHeadId)
                    .filter(user -> user instanceof ClubHead)
                    .map(user -> (ClubHead) user);
                    
            if (clubHeadOpt.isEmpty()) {
                throw new IllegalArgumentException("Invalid Club Head ID");
            }
            
            // Get the Faculty user
            Optional<Faculty> facultyOpt = userRepository.findById(facultyId)
                    .filter(user -> user instanceof Faculty)
                    .map(user -> (Faculty) user);
                    
            if (facultyOpt.isEmpty()) {
                throw new IllegalArgumentException("Invalid Faculty ID");
            }
            
            ClubHead clubHead = clubHeadOpt.get();
            Faculty faculty = facultyOpt.get();
            
            // Create the Event
            Event event = new Event();
            event.setName(name);
            event.setDescription(description);
            event.setStartDateTime(startDateTime);
            event.setEndDateTime(endDateTime);
            event.setVenue(venue);
            event.setMaxParticipants(maxParticipants);
            event.setOrganizer(clubHead);
            event.setFaculty(faculty);
            event.setDepartment(faculty.getDepartment());
            event.setStatus("PENDING");
            event.setCreatedAt(LocalDateTime.now());
            
            // Save the event
            Event savedEvent = eventRepository.save(event);
            
            // Create an event request
            EventRequest eventRequest = new EventRequest();
            eventRequest.setEvent(savedEvent);
            eventRequest.setStatus("PENDING");
            eventRequest.setRequestDate(LocalDateTime.now());
            
            // Save the event request
            eventRequestRepository.save(eventRequest);
            
            return savedEvent;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create event: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get events organized by a specific club head
     * @param clubHeadId ID of the club head
     * @return List of events organized by the club head
     */
    public List<Event> getEventsByClubHead(String clubHeadId) {
        try {
            Optional<ClubHead> clubHeadOpt = userRepository.findById(clubHeadId)
                    .filter(user -> user instanceof ClubHead)
                    .map(user -> (ClubHead) user);
                    
            if (clubHeadOpt.isEmpty()) {
                throw new IllegalArgumentException("Invalid Club Head ID");
            }
            
            return eventRepository.findByOrganizer(clubHeadOpt.get());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get events: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get an event by its ID
     * @param eventId ID of the event
     * @return Optional containing the event if found
     */
    public Optional<Event> getEventById(String eventId) {
        return eventRepository.findById(eventId);
    }
    
    /**
     * Get all approved events
     */
    public List<Event> getAllApprovedEvents() {
        return eventRepository.findByStatus("APPROVED");
    }
    
    /**
     * Get all upcoming approved events (events that haven't started yet)
     */
    public List<Event> getUpcomingApprovedEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findByStatus("APPROVED")
                .stream()
                .filter(event -> event.getStartDateTime().isAfter(now))
                .collect(Collectors.toList());
    }
    
    /**
     * Get popular events (approved events with the most registrations)
     */
    public List<Event> getPopularEvents(int limit) {
        return eventRepository.findByStatus("APPROVED")
                .stream()
                .sorted((e1, e2) -> Integer.compare(
                    e2.getRegisteredStudents().size(), 
                    e1.getRegisteredStudents().size()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Get events by department
     */
    public List<Event> getEventsByDepartment(String department) {
        return eventRepository.findByStatusAndDepartment("APPROVED", department);
    }
    
    /**
     * Get events registered by a student
     */
    public List<Event> getEventsRegisteredByStudent(Student student) {
        if (student == null) {
            System.err.println("Warning: Null student passed to getEventsRegisteredByStudent");
            return new ArrayList<>();
        }
        
        List<Event> registeredEvents = student.getRegisteredEvents();
        if (registeredEvents == null) {
            System.err.println("Warning: Student " + student.getName() + " has null registeredEvents list");
            return new ArrayList<>();
        }
        
        return registeredEvents;
    }
    
    /**
     * Get upcoming events registered by a student
     */
    public List<Event> getUpcomingEventsRegisteredByStudent(Student student) {
        LocalDateTime now = LocalDateTime.now();
        return student.getRegisteredEvents()
                .stream()
                .filter(event -> event.getStartDateTime().isAfter(now))
                .collect(Collectors.toList());
    }
    
    /**
     * Register a student for an event
     */
    public boolean registerStudentForEvent(Student student, Event event) {
        if (student.isBlacklisted() || 
            event.getRegisteredStudents().size() >= event.getMaxParticipants()) {
            return false;
        }
        event.addRegisteredStudent(student);
        eventRepository.save(event);
        return true;
    }
    
    /**
     * Get the total count of registered events for a student
     */
    public int getRegisteredEventsCount(Student student) {
        return student.getRegisteredEvents().size();
    }
    
    /**
     * Get the total count of attended events for a student
     */
    public int getAttendedEventsCount(Student student) {
        return student.getRegisteredEvents()
                .stream()
                .filter(event -> event.getAttendedStudents().contains(student))
                .collect(Collectors.toList())
                .size();
    }
    
    /**
     * Get total number of upcoming approved events
     */
    public int getUpcomingEventsCount() {
        LocalDateTime now = LocalDateTime.now();
        return (int) eventRepository.findByStatus("APPROVED")
                .stream()
                .filter(event -> event.getStartDateTime().isAfter(now))
                .count();
    }
} 