package com.example.demo.model.service;

import com.example.demo.model.ClubHead;
import com.example.demo.model.Event;
import com.example.demo.model.EventRequest;
import com.example.demo.model.Faculty;
import com.example.demo.model.HOD;
import com.example.demo.model.Room;
import com.example.demo.model.RoomRequest;
import com.example.demo.model.RoomManager;
import com.example.demo.model.factory.EventFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for ClubHead functionality
 * Implements GRASP Creator pattern - ClubHead creates events, event requests, and room requests
 */
@Service
public class ClubHeadService {
    
    /**
     * Create a new event and add it to the club head's list of events
     */
    public Event createEvent(ClubHead clubHead, String name, String description, LocalDateTime startDateTime, 
                            LocalDateTime endDateTime, String venue, int maxParticipants, Faculty faculty) {
        
        // Using the factory to create the event - Factory pattern
        Event event = EventFactory.createEvent(name, description, startDateTime, endDateTime, 
                                              venue, maxParticipants, clubHead, faculty);
        
        // Add the event to the club head's list - Creator pattern (GRASP)
        clubHead.addEvent(event);
        
        return event;
    }
    
    /**
     * Create an event request and send it to HOD for approval
     */
    public EventRequest createEventRequest(Event event, HOD hod) {
        // Using the factory to create the event request
        EventRequest eventRequest = EventFactory.createEventRequest(event);
        
        // Send the request to HOD
        hod.addEventRequest(eventRequest);
        
        return eventRequest;
    }
    
    /**
     * Create a room request and send it to Room Manager
     */
    public RoomRequest createRoomRequest(Event event, Room room, ClubHead clubHead, 
                                        String requiredServices, RoomManager roomManager) {
        
        // Using the factory to create the room request
        RoomRequest roomRequest = EventFactory.createRoomRequest(event, room, clubHead, requiredServices);
        
        // Send the request to Room Manager
        roomManager.addRoomRequest(roomRequest);
        
        return roomRequest;
    }
    
    /**
     * Get all events created by a club head
     */
    public List<Event> getClubHeadEvents(ClubHead clubHead) {
        return clubHead.getCreatedEvents();
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