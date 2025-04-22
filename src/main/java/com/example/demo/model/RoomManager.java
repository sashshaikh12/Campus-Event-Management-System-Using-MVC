package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "room_managers")
@DiscriminatorValue("room_manager")
public class RoomManager extends User {
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_manager_id")
    private List<Room> managedRooms;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_manager_id")
    private List<RoomRequest> pendingRequests;
    
    // Adding department field for compatibility
    private String department;

    public RoomManager() {
        super();
        this.managedRooms = new ArrayList<>();
        this.pendingRequests = new ArrayList<>();
    }

    public RoomManager(String username, String password, String name, String email) {
        super(username, password, name, email, "ROOM_MANAGER");
        this.managedRooms = new ArrayList<>();
        this.pendingRequests = new ArrayList<>();
    }

    public List<Room> getManagedRooms() {
        return managedRooms;
    }

    public void setManagedRooms(List<Room> managedRooms) {
        this.managedRooms = managedRooms;
    }

    public List<RoomRequest> getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(List<RoomRequest> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public void addRoom(Room room) {
        this.managedRooms.add(room);
    }

    public void addRoomRequest(RoomRequest request) {
        this.pendingRequests.add(request);
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