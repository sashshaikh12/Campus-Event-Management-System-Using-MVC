package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "club_heads")
@DiscriminatorValue("club_head")
public class ClubHead extends User {
    
    @Column(nullable = false)
    private String clubName;
    
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> createdEvents;
    
    // Add department for compilation
    private String department;

    public ClubHead() {
        super();
        this.createdEvents = new ArrayList<>();
    }

    public ClubHead(String username, String password, String name, String email, String clubName) {
        super(username, password, name, email, "CLUB_HEAD");
        this.clubName = clubName;
        this.createdEvents = new ArrayList<>();
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public List<Event> getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(List<Event> createdEvents) {
        this.createdEvents = createdEvents;
    }
    
    public void addEvent(Event event) {
        this.createdEvents.add(event);
    }
    
    @Override
    public String getDepartment() {
        return department;
    }
    
    @Override
    public void setDepartment(String department) {
        this.department = department;
    }
} 