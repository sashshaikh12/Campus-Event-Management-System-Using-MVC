package com.example.demo.model.facade;

import com.example.demo.model.ClubHead;
import com.example.demo.model.Event;
import com.example.demo.model.Room;
import com.example.demo.model.RoomManager;
import com.example.demo.model.RoomRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Facade for Room Management operations
 * Implements Facade Design Pattern and GRASP Low Coupling
 */
public class RoomManagementFacade {
    private RoomManager roomManager;
    
    public RoomManagementFacade(RoomManager roomManager) {
        this.roomManager = roomManager;
    }
    
    /**
     * Get all pending room requests
     */
    public List<RoomRequest> getPendingRequests() {
        return roomManager.getPendingRequests();
    }
    
    /**
     * Get all rooms managed by the room manager
     */
    public List<Room> getAllRooms() {
        return roomManager.getManagedRooms();
    }
    
    /**
     * Find available rooms for a given time slot
     */
    public List<Room> findAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {
        return roomManager.getManagedRooms().stream()
                .filter(room -> room.isAvailable(startTime, endTime))
                .collect(Collectors.toList());
    }
    
    /**
     * Process a room request with approval
     */
    public void approveRoomRequest(RoomRequest request, String comments) {
        request.approve(comments);
        // Notify the club head about the approval
        notifyClubHead(request.getRequestedBy(), request, true);
    }
    
    /**
     * Process a room request with rejection
     */
    public void rejectRoomRequest(RoomRequest request, String comments) {
        request.reject(comments);
        // Notify the club head about the rejection
        notifyClubHead(request.getRequestedBy(), request, false);
    }
    
    /**
     * Notify a club head about room request status
     */
    private void notifyClubHead(ClubHead clubHead, RoomRequest request, boolean approved) {
        // In a real implementation, this would send an email or notification
        String status = approved ? "approved" : "rejected";
        String message = "Your room request for event " + request.getEvent().getName() + 
                        " has been " + status + " with comments: " + request.getComments();
        
        // Notification logic here
    }
    
    /**
     * Add a new room to the system
     */
    public void addRoom(Room room) {
        roomManager.addRoom(room);
    }
    
    /**
     * Check if a room is available for a specific time
     */
    public boolean isRoomAvailable(Room room, LocalDateTime startTime, LocalDateTime endTime) {
        return room.isAvailable(startTime, endTime);
    }
    
    /**
     * Reserve a room for an event
     */
    public RoomRequest reserveRoom(Room room, Event event, ClubHead clubHead, String requiredServices) {
        return room.reserve(event, clubHead, requiredServices);
    }
} 