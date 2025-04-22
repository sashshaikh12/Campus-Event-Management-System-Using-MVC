package com.example.demo.model.service;

import com.example.demo.model.Event;
import com.example.demo.model.Student;
import com.example.demo.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Event functionality
 */
@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
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