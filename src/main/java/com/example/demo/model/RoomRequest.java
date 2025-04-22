package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "room_requests")
public class RoomRequest {
    @Id
    @Column(name = "id")
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    
    @ManyToOne
    @JoinColumn(name = "club_head_id")
    private ClubHead requestedBy;
    
    @Column(name = "request_date")
    private LocalDateTime requestDate;
    
    @Column(name = "required_services")
    private String requiredServices; // e.g., "projector, audio, chairs"
    
    @Column(name = "status")
    private String status; // PENDING, APPROVED, REJECTED
    
    @Column(name = "comments")
    private String comments;
    
    @Column(name = "response_date")
    private LocalDateTime responseDate;

    public RoomRequest() {
        this.id = UUID.randomUUID().toString();
        this.requestDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    public RoomRequest(Event event, Room room, ClubHead requestedBy, String requiredServices) {
        this.id = UUID.randomUUID().toString();
        this.event = event;
        this.room = room;
        this.requestedBy = requestedBy;
        this.requiredServices = requiredServices;
        this.requestDate = LocalDateTime.now();
        this.status = "PENDING";
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public ClubHead getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(ClubHead requestedBy) {
        this.requestedBy = requestedBy;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequiredServices() {
        return requiredServices;
    }

    public void setRequiredServices(String requiredServices) {
        this.requiredServices = requiredServices;
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
        this.event.setRoom(this.room);
        this.room.addEvent(this.event);
    }

    public void reject(String comments) {
        this.status = "REJECTED";
        this.comments = comments;
        this.responseDate = LocalDateTime.now();
    }
} 