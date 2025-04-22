package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "hods")
@DiscriminatorValue("HOD")
public class HOD extends User {
    
    @Column(nullable = false)
    private String department;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "hod_id")
    private List<EventRequest> pendingRequests;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "hod_id")
    private List<Appeal> pendingAppeals;

    public HOD() {
        super();
        this.pendingRequests = new ArrayList<>();
        this.pendingAppeals = new ArrayList<>();
    }

    public HOD(String username, String password, String name, String email, String department) {
        super(username, password, name, email, "HOD");
        this.department = department;
        this.pendingRequests = new ArrayList<>();
        this.pendingAppeals = new ArrayList<>();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<EventRequest> getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(List<EventRequest> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public List<Appeal> getPendingAppeals() {
        return pendingAppeals;
    }

    public void setPendingAppeals(List<Appeal> pendingAppeals) {
        this.pendingAppeals = pendingAppeals;
    }

    public void addEventRequest(EventRequest eventRequest) {
        this.pendingRequests.add(eventRequest);
    }

    public void addAppeal(Appeal appeal) {
        this.pendingAppeals.add(appeal);
    }
} 