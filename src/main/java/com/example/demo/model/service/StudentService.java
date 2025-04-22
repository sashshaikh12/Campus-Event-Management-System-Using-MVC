package com.example.demo.model.service;

import com.example.demo.model.Appeal;
import com.example.demo.model.Event;
import com.example.demo.model.HOD;
import com.example.demo.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Student functionality
 */
@Service
public class StudentService {
    
    /**
     * Register a student for an event
     */
    public boolean registerForEvent(Student student, Event event) {
        if (student.isBlacklisted()) {
            return false;
        }
        
        if (event.getRegisteredStudents().size() >= event.getMaxParticipants()) {
            return false;
        }
        
        event.addRegisteredStudent(student);
        return true;
    }
    
    /**
     * Submit an appeal to be removed from blacklist
     */
    public Appeal submitAppeal(Student student, HOD hod, String reason) {
        if (!student.isBlacklisted()) {
            return null; // Can't appeal if not blacklisted
        }
        
        Appeal appeal = new Appeal();
        appeal.setStudentId(student.getId());
        appeal.setHodId(hod.getId());
        appeal.setReason(reason);
        appeal.setStatus("PENDING");
        appeal.setDescription("Appeal to remove blacklisting");
        
        return appeal;
    }
    
    /**
     * Get all events a student is registered for
     */
    public List<Event> getRegisteredEvents(Student student) {
        return student.getRegisteredEvents();
    }
    
    /**
     * Get upcoming events a student is registered for (events that haven't started yet)
     */
    public List<Event> getUpcomingEvents(Student student) {
        return student.getRegisteredEvents().stream()
                .filter(event -> event.getStartDateTime().isAfter(java.time.LocalDateTime.now()))
                .collect(Collectors.toList());
    }
    
    /**
     * Check if a student is blacklisted
     */
    public boolean isBlacklisted(Student student) {
        return student.isBlacklisted();
    }
    
    /**
     * Get the number of absences for a student
     */
    public int getAbsenceCount(Student student) {
        return student.getAbsenceCount();
    }
}