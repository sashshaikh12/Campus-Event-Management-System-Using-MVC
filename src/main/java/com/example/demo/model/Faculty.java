package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "faculties")
@DiscriminatorValue("faculty")
public class Faculty extends User {
    
    @Column(nullable = false)
    private String department;
    
    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> managedEvents;
    
    @OneToMany
    @JoinColumn(name = "faculty_id")
    private List<Student> students;

    public Faculty() {
        super();
        this.managedEvents = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public Faculty(String username, String password, String name, String email, String department) {
        super(username, password, name, email, "FACULTY");
        this.department = department;
        this.managedEvents = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    @Override
    public String getDepartment() {
        return department;
    }

    @Override
    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Event> getManagedEvents() {
        return managedEvents;
    }

    public void setManagedEvents(List<Event> managedEvents) {
        this.managedEvents = managedEvents;
    }

    public void addManagedEvent(Event event) {
        this.managedEvents.add(event);
    }
    
    public List<Student> getStudents() {
        return students;
    }
    
    public void setStudents(List<Student> students) {
        this.students = students;
    }
} 