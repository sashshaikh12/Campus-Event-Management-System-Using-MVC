package com.example.demo.model.service;

import com.example.demo.model.Event;
import com.example.demo.model.Faculty;
import com.example.demo.model.Student;
import com.example.demo.model.iterator.EventStudentIterator;
import com.example.demo.model.iterator.StudentIterator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Faculty functionality
 * Implements GRASP Information Expert pattern - Faculty manages attendance and student status
 * as it has the most information about student attendance
 */
@Service
public class FacultyService {
    
    /**
     * Get all events managed by a faculty
     */
    public List<Event> getFacultyEvents(Faculty faculty) {
        return faculty.getManagedEvents();
    }
    
    /**
     * Mark attendance for a student in an event
     */
    public void markAttendance(Event event, Student student) {
        event.markAttendance(student);
    }
    
    /**
     * Mark a student as absent from an event
     */
    public void markAbsent(Event event, Student student) {
        event.markAbsent(student);
    }
    
    /**
     * Get a student iterator for an event
     * Implements Iterator Pattern
     */
    public StudentIterator getEventStudentIterator(Event event) {
        return new EventStudentIterator(event);
    }
    
    /**
     * Take attendance for an event using the iterator pattern
     */
    public void takeAttendance(Event event, List<Student> presentStudents) {
        // Using Iterator pattern to iterate through all registered students
        StudentIterator iterator = getEventStudentIterator(event);
        
        while (iterator.hasNext()) {
            Student student = iterator.next();
            
            if (presentStudents.contains(student)) {
                event.markAttendance(student);
            } else {
                event.markAbsent(student);
            }
        }
    }
    
    /**
     * Get all registered students for an event
     */
    public List<Student> getRegisteredStudents(Event event) {
        return event.getRegisteredStudents();
    }
    
    /**
     * Get all students who attended an event
     */
    public List<Student> getAttendedStudents(Event event) {
        return event.getAttendedStudents();
    }
    
    /**
     * Calculate attendance percentage for a student in all events
     * Information Expert pattern - Faculty has the expertise to calculate attendance
     */
    public double calculateAttendancePercentage(Student student, List<Event> events) {
        int totalEvents = 0;
        int attendedEvents = 0;
        
        for (Event event : events) {
            if (event.getRegisteredStudents().contains(student)) {
                totalEvents++;
                if (event.getAttendedStudents().contains(student)) {
                    attendedEvents++;
                }
            }
        }
        
        if (totalEvents == 0) {
            return 0.0;
        }
        
        return (double) attendedEvents / totalEvents * 100.0;
    }
} 