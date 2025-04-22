package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
@DiscriminatorValue("student")
public class Student extends User {
    @Column(nullable = false, unique = true)
    private String rollNumber;
    
    @Column(nullable = false)
    private String department;
    
    @Column(nullable = false)
    private int semester;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "student_event_registrations",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> registeredEvents;
    
    @Column(nullable = false)
    private boolean isBlacklisted;
    
    @Column(nullable = false)
    private int absenceCount;

    public Student() {
        super();
        this.registeredEvents = new ArrayList<>();
        this.isBlacklisted = false;
        this.absenceCount = 0;
        this.semester = 1;
    }

    public Student(String username, String password, String name, String email, String rollNumber, String department, int semester) {
        super(username, password, name, email, "STUDENT");
        this.rollNumber = rollNumber;
        this.department = department;
        this.semester = semester;
        this.registeredEvents = new ArrayList<>();
        this.isBlacklisted = false;
        this.absenceCount = 0;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    @Override
    public String getDepartment() {
        return department;
    }

    @Override
    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Event> getRegisteredEvents() {
        return registeredEvents;
    }

    public void setRegisteredEvents(List<Event> registeredEvents) {
        this.registeredEvents = registeredEvents;
    }

    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        isBlacklisted = blacklisted;
    }

    public int getAbsenceCount() {
        return absenceCount;
    }

    public void setAbsenceCount(int absenceCount) {
        this.absenceCount = absenceCount;
    }

    public void incrementAbsenceCount() {
        this.absenceCount++;
        if (this.absenceCount >= 3) {
            this.isBlacklisted = true;
        }
    }

    public void registerForEvent(Event event) {
        if (!isBlacklisted) {
            this.registeredEvents.add(event);
        }
    }

    public int getSemester() {
        return semester;
    }
    
    public void setSemester(int semester) {
        this.semester = semester;
    }
} 