package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "event_requests")
public class EventRequest {
    @Id
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    
    @Column(name = "status", nullable = false)
    private String status; // PENDING, APPROVED, REJECTED
    
    @Column(name = "comments")
    private String comments;
    
    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;
    
    @Column(name = "response_date")
    private LocalDateTime responseDate;

    public EventRequest() {
        this.id = UUID.randomUUID().toString();
        this.status = "PENDING";
        this.requestDate = LocalDateTime.now();
    }

    public EventRequest(Event event) {
        this.id = UUID.randomUUID().toString();
        this.event = event;
        this.status = "PENDING";
        this.requestDate = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }

    public void approve(String comments) {
        this.status = "APPROVED";
        this.comments = comments;
        this.responseDate = LocalDateTime.now();
        this.event.setStatus("APPROVED");
    }

    public void reject(String comments) {
        this.status = "REJECTED";
        this.comments = comments;
        this.responseDate = LocalDateTime.now();
        this.event.setStatus("REJECTED");
    }
} 