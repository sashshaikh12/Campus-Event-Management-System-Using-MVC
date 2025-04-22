package com.example.demo.model.factory;

import com.example.demo.model.ClubHead;
import com.example.demo.model.Event;
import com.example.demo.model.EventRequest;
import com.example.demo.model.Faculty;
import com.example.demo.model.RoomRequest;
import com.example.demo.model.Room;

import java.time.LocalDateTime;

/**
 * Factory class for creating Event related objects - implements Factory Design Pattern
 */
public class EventFactory {
    
    // Factory method to create events
    public static Event createEvent(String name, String description, LocalDateTime startDateTime, 
                                   LocalDateTime endDateTime, String venue, int maxParticipants, 
                                   ClubHead organizer, Faculty faculty) {
        
        return new Event(name, description, startDateTime, endDateTime, venue, maxParticipants, organizer, faculty);
    }
    
    // Factory method to create event requests
    public static EventRequest createEventRequest(Event event) {
        return new EventRequest(event);
    }
    
    // Factory method to create room requests
    public static RoomRequest createRoomRequest(Event event, Room room, ClubHead requestedBy, String requiredServices) {
        return new RoomRequest(event, room, requestedBy, requiredServices);
    }
} 