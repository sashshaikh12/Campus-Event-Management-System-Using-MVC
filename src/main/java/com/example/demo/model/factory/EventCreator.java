package com.example.demo.model.factory;

import com.example.demo.model.ClubHead;
import com.example.demo.model.Event;
import com.example.demo.model.Faculty;

import java.time.LocalDateTime;

/**
 * Interface for event creation - part of Factory Design Pattern
 */
public interface EventCreator {
    
    /**
     * Creates an event with the specified parameters
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
    Event createEvent(String name, String description, LocalDateTime startDateTime, 
                     LocalDateTime endDateTime, String venue, int maxParticipants, 
                     ClubHead organizer, Faculty faculty);
}