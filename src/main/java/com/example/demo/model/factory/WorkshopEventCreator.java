package com.example.demo.model.factory;

import com.example.demo.model.ClubHead;
import com.example.demo.model.Event;
import com.example.demo.model.Faculty;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;

/**
 * Workshop event creator - specialized implementation of EventCreator
 * Creates events specifically configured for workshops
 */
@Component
@Qualifier("workshopEventCreator")
public class WorkshopEventCreator implements EventCreator {
    
    /**
     * Creates a workshop-specific event with default workshop settings
     * Workshops have specific properties like fixed maximum participants
     * and specialized description formatting
     * 
     * @param name Event name
     * @param description Event description
     * @param startDateTime Event start date and time
     * @param endDateTime Event end date and time
     * @param venue Event venue
     * @param maxParticipants Maximum number of participants (will be capped for workshops)
     * @param organizer ClubHead organizing the event
     * @param faculty Faculty supervising the event
     * @return Created Workshop Event object
     */
    @Override
    public Event createEvent(String name, String description, LocalDateTime startDateTime, 
                            LocalDateTime endDateTime, String venue, int maxParticipants, 
                            ClubHead organizer, Faculty faculty) {
        
        // For workshops, we enforce a standard format and certain constraints
        String workshopName = "Workshop: " + name;
        String workshopDesc = "WORKSHOP\n" + description + "\n\nThis is a hands-on workshop. Please bring your materials.";
        
        // Workshops typically have a smaller capacity for better interaction
        int actualMaxParticipants = Math.min(maxParticipants, 30); // Cap at 30 participants for workshops
        
        Event workshop = new Event(workshopName, workshopDesc, startDateTime, endDateTime, 
                                  venue, actualMaxParticipants, organizer, faculty);
        
        // Workshops are created with PENDING status by default
        workshop.setStatus("PENDING");
        // Special tag for workshop events
        workshop.setDepartment(faculty.getDepartment() + " - Workshop");
        
        return workshop;
    }
}