package com.example.demo.model.iterator;

import com.example.demo.model.Student;

/**
 * Iterator interface for traversing students
 * Implements Iterator Design Pattern
 */
public interface StudentIterator {
    boolean hasNext();
    Student next();
} 