package com.example.demo.model.service;

import com.example.demo.model.ClubHead;
import com.example.demo.model.Event;
import com.example.demo.model.EventRequest;
import com.example.demo.model.Faculty;
import com.example.demo.model.HOD;
import com.example.demo.model.Room;
import com.example.demo.model.RoomRequest;
import com.example.demo.model.RoomManager;
import com.example.demo.model.factory.EventCreator;
import com.example.demo.model.factory.EventFactory;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.EventRequestRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for ClubHead functionality
 * Implements GRASP Creator pattern - ClubHead creates events, event requests, and room requests
 */
@Service
public class ClubHeadService {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private EventRequestRepository eventRequestRepository;
    
    @Autowired
    @Qualifier("standardEventCreator") // Specifying which implementation to inject
    private EventCreator eventCreator;
    
    @Autowired
    @Qualifier("workshopEventCreator") // Specifying workshop event creator
    private EventCreator workshopEventCreator;
    
    /**
     * Create a new event and add it to the club head's list of events
     */
    @Transactional
    public Event createEvent(ClubHead clubHead, String name, String description, LocalDateTime startDateTime, 
                            LocalDateTime endDateTime, String venue, int maxParticipants, Faculty faculty) {
        
        // Using the factory to create the event - Factory pattern
        Event event = eventCreator.createEvent(name, description, startDateTime, endDateTime, 
                                              venue, maxParticipants, clubHead, faculty);
        
        // Add the event to the club head's list - Creator pattern (GRASP)
        clubHead.addEvent(event);
        
        // Save the updated clubhead and event to the database
        userRepository.save(clubHead);
        return eventRepository.save(event);
    }
    
    /**
     * Create a new workshop event with specific workshop configuration
     */
    @Transactional
    public Event createWorkshopEvent(ClubHead clubHead, String name, String description, LocalDateTime startDateTime, 
                                    LocalDateTime endDateTime, String venue, int maxParticipants, Faculty faculty) {
        
        // Prepend "Workshop:" to name for special treatment
        String workshopName = "Workshop: " + name;
        
        // Use factory to create the event
        Event event = EventFactory.createEvent(
            workshopName, description, startDateTime, endDateTime,
            venue, maxParticipants, clubHead, faculty
        );
        
        // Additional workshop-specific setup
        event.setStatus("PENDING");
        event.setCreatedAt(LocalDateTime.now());
        event.setDepartment(clubHead.getDepartment());
        // Maybe add workshop tags or properties
        
        // Save event
        eventRepository.save(event);
        
        // Create and save request
        EventRequest request = EventFactory.createEventRequest(event);
        eventRequestRepository.save(request);
        
        return event;
    }
    
    /**
     * Create an event request and send it to HOD for approval
     */
    @Transactional
    public EventRequest createEventRequest(Event event, HOD hod) {
        // Using the factory to create the event request
        EventRequest eventRequest = EventFactory.createEventRequest(event);
        
        // Send the request to HOD
        hod.addEventRequest(eventRequest);
        
        // Save the updated HOD to the database
        userRepository.save(hod);
        
        return eventRequest;
    }
    
    /**
     * Create a room request and send it to Room Manager
     */
    @Transactional
    public RoomRequest createRoomRequest(Event event, Room room, ClubHead clubHead, 
                                        String requiredServices, RoomManager roomManager) {
        
        // Using the factory to create the room request
        RoomRequest roomRequest = EventFactory.createRoomRequest(event, room, clubHead, requiredServices);
        
        // Send the request to Room Manager
        roomManager.addRoomRequest(roomRequest);
        
        // Save the updated room manager to the database
        userRepository.save(roomManager);
        
        return roomRequest;
    }
    
    /**
     * Get all events created by a club head
     */
    public List<Event> getClubHeadEvents(ClubHead clubHead) {
        try {
            if (clubHead == null || eventRepository == null) {
                return new ArrayList<>();
            }
            
            // First try to get events from the club head object directly
            if (clubHead.getCreatedEvents() != null && !clubHead.getCreatedEvents().isEmpty()) {
                return clubHead.getCreatedEvents();
            }
            
            // If not available there, try the repository
            return eventRepository.findByOrganizer(clubHead);
        } catch (Exception e) {
            System.err.println("Error fetching events for club head: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Notify students about an event
     * This would typically send emails or notifications to registered students
     */
    public void notifyStudentsAboutEvent(Event event) {
        // Notification logic here
        // This could send emails, push notifications, etc.
    }
}