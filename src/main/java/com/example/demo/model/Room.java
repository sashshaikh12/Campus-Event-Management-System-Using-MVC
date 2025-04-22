package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    private String id;
    
    @Column(nullable = false, unique = true)
    private String roomNumber;
    
    @Column(nullable = false)
    private Integer capacity;
    
    @Column(nullable = false)
    private String building;
    
    @Column(nullable = false)
    private Boolean hasProjector;
    
    @Column(nullable = false)
    private Boolean hasAirConditioner;
    
    @ManyToOne
    @JoinColumn(name = "room_manager_id")
    private RoomManager manager;
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> events;

    public Room() {
        this.id = UUID.randomUUID().toString();
        this.events = new ArrayList<>();
    }

    public Room(String roomNumber, int capacity, String building, boolean hasProjector, boolean hasAirConditioner) {
        this.id = UUID.randomUUID().toString();
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.building = building;
        this.hasProjector = hasProjector;
        this.hasAirConditioner = hasAirConditioner;
        this.events = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public boolean hasProjector() {
        return hasProjector;
    }

    public void setHasProjector(boolean hasProjector) {
        this.hasProjector = hasProjector;
    }

    public boolean hasAirConditioner() {
        return hasAirConditioner;
    }

    public void setHasAirConditioner(boolean hasAirConditioner) {
        this.hasAirConditioner = hasAirConditioner;
    }

    public RoomManager getManager() {
        return manager;
    }

    public void setManager(RoomManager manager) {
        this.manager = manager;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        this.events.add(event);
        event.setRoom(this);
    }

    /**
     * Checks if the room is available during the specified time period
     */
    public boolean isAvailable(LocalDateTime startTime, LocalDateTime endTime) {
        // Check if there are any events that overlap with the requested time
        return events.stream().noneMatch(event -> 
            (startTime.isBefore(event.getEndDateTime()) && endTime.isAfter(event.getStartDateTime())));
    }
    
    /**
     * Reserves the room for the specified time period by creating a RoomRequest
     * @param event The event for which the room is being reserved
     * @param requestedBy The ClubHead who is requesting the room
     * @param requiredServices Any additional services required for the event
     * @return A new RoomRequest object in PENDING status
     */
    public RoomRequest reserve(Event event, ClubHead requestedBy, String requiredServices) {
        if (isAvailable(event.getStartDateTime(), event.getEndDateTime())) {
            RoomRequest request = new RoomRequest();
            request.setRoom(this);
            request.setEvent(event);
            request.setRequestedBy(requestedBy);
            request.setRequiredServices(requiredServices);
            return request;
        }
        return null;
    }

    public String getFullName() {
        return building + " - " + roomNumber;
    }
} 