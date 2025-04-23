package com.example.demo.model.factory;

import com.example.demo.model.ClubHead;
import com.example.demo.model.Event;
import com.example.demo.model.Faculty;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;

/**
 * Standard implementation of EventCreator - part of Factory Design Pattern
 * Creates regular Event objects
 */
@Component
@Qualifier("standardEventCreator")
public class StandardEventCreator implements EventCreator {
    
    /**
     * Creates a standard event with the specified parameters
     * 
     * @param name Event name
     * @param description Event description
     * @param startDateTime Event start date and time
     * @param endDateTime Event end date and time
     * @param venue Event venue
     * @param maxParticipants Maximum number of participants
     * @param organizer ClubHead organizing the event
     * @param faculty Faculty supervising the event
     * @return Created Event object
     */
    @Override
    public Event createEvent(String name, String description, LocalDateTime startDateTime, 
                            LocalDateTime endDateTime, String venue, int maxParticipants, 
                            ClubHead organizer, Faculty faculty) {
        
        return new Event(name, description, startDateTime, endDateTime, venue, 
                        maxParticipants, organizer, faculty);
    }
}