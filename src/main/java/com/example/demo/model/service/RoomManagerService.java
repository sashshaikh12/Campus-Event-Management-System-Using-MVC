package com.example.demo.model.service;

import com.example.demo.model.ClubHead;
import com.example.demo.model.Event;
import com.example.demo.model.Room;
import com.example.demo.model.RoomManager;
import com.example.demo.model.RoomRequest;
import com.example.demo.model.facade.RoomManagementFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Service class for RoomManager functionality
 * Uses the Facade pattern and implements GRASP Low Coupling
 */
@Service
public class RoomManagerService {
    private RoomManagementFacade facade;
    
    @Autowired(required = false)
    public RoomManagerService(RoomManager roomManager) {
        // Initialize the facade with the room manager if available
        if (roomManager != null) {
            this.facade = new RoomManagementFacade(roomManager);
        } else {
            // Create a default room manager if none is available
            RoomManager defaultManager = new RoomManager();
            defaultManager.setId(UUID.randomUUID().toString());
            defaultManager.setUsername("default_manager");
            defaultManager.setPassword("password");
            defaultManager.setName("Default Room Manager");
            defaultManager.setEmail("roommanager@example.com");
            defaultManager.setRole("ROOM_MANAGER");
            
            this.facade = new RoomManagementFacade(defaultManager);
        }
    }
    
    // Default constructor for Spring
    public RoomManagerService() {
        RoomManager defaultManager = new RoomManager();
        defaultManager.setId(UUID.randomUUID().toString());
        defaultManager.setUsername("default_manager");
        defaultManager.setPassword("password");
        defaultManager.setName("Default Room Manager");
        defaultManager.setEmail("roommanager@example.com");
        defaultManager.setRole("ROOM_MANAGER");
        
        this.facade = new RoomManagementFacade(defaultManager);
    }
    
    /**
     * Get all pending room requests
     */
    public List<RoomRequest> getPendingRequests() {
        return facade.getPendingRequests();
    }
    
    /**
     * Get all managed rooms
     */
    public List<Room> getAllRooms() {
        return facade.getAllRooms();
    }
    
    /**
     * Approve a room request
     */
    public void approveRoomRequest(RoomRequest request, String comments) {
        facade.approveRoomRequest(request, comments);
    }
    
    /**
     * Reject a room request
     */
    public void rejectRoomRequest(RoomRequest request, String comments) {
        facade.rejectRoomRequest(request, comments);
    }
    
    /**
     * Find available rooms for an event time slot
     */
    public List<Room> findAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {
        return facade.findAvailableRooms(startTime, endTime);
    }
    
    /**
     * Check if a specific room is available for an event
     */
    public boolean isRoomAvailable(Room room, Event event) {
        return facade.isRoomAvailable(room, event.getStartDateTime(), event.getEndDateTime());
    }
    
    /**
     * Add a new room to the system
     */
    public void addRoom(Room room) {
        facade.addRoom(room);
    }
    
    /**
     * Reserve a room for an event
     */
    public RoomRequest reserveRoom(Room room, Event event, ClubHead clubHead, String requiredServices) {
        return facade.reserveRoom(room, event, clubHead, requiredServices);
    }
} 