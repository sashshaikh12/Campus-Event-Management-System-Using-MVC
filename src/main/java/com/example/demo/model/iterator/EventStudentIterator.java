package com.example.demo.model.iterator;

import com.example.demo.model.Event;
import com.example.demo.model.Student;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Concrete iterator implementation for traversing students registered for an event
 * Implements Iterator Design Pattern
 */
public class EventStudentIterator implements StudentIterator {
    private List<Student> students;
    private int position;
    
    public EventStudentIterator(Event event) {
        this.students = event.getRegisteredStudents();
        this.position = 0;
    }

    @Override
    public boolean hasNext() {
        return position < students.size();
    }

    @Override
    public Student next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more students to iterate");
        }
        return students.get(position++);
    }
} 