package com.example.demo.controller;

import com.example.demo.model.Room;
import com.example.demo.model.RoomManager;
import com.example.demo.model.RoomRequest;
import com.example.demo.model.service.RoomManagerService;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/roommanager")
public class RoomManagerController {

    @Autowired
    private RoomManagerService roomManagerService;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // For demo purposes, we'll assume a Room Manager with id "4"
        Optional<RoomManager> managerOpt = userRepository.findById("4")
                .map(user -> (RoomManager) user);
        
        if (managerOpt.isPresent()) {
            RoomManager manager = managerOpt.get();
            
            // Using the facade pattern through the service
            List<RoomRequest> pendingRequests = roomManagerService.getPendingRequests();
            List<Room> rooms = roomManagerService.getAllRooms();
            
            model.addAttribute("manager", manager);
            model.addAttribute("pendingRequests", pendingRequests);
            model.addAttribute("rooms", rooms);
            
            return "roommanager/dashboard";
        }
        
        return "redirect:/login";
    }
    
    @GetMapping("/rooms")
    public String listRooms(Model model) {
        // Using the facade pattern through the service
        List<Room> rooms = roomManagerService.getAllRooms();
        
        model.addAttribute("rooms", rooms);
        return "roommanager/rooms";
    }
    
    @GetMapping("/rooms/create")
    public String showCreateRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "roommanager/create-room";
    }
    
    @PostMapping("/rooms/create")
    public String createRoom(@ModelAttribute Room room) {
        // Using the facade pattern through the service
        roomManagerService.addRoom(room);
        return "redirect:/roommanager/rooms";
    }
    
    @GetMapping("/room-requests")
    public String listRoomRequests(Model model) {
        // Using the facade pattern through the service
        List<RoomRequest> pendingRequests = roomManagerService.getPendingRequests();
        
        model.addAttribute("requests", pendingRequests);
        return "roommanager/room-requests";
    }
    
    @GetMapping("/room-requests/{requestId}")
    public String viewRoomRequest(@PathVariable String requestId, Model model) {
        Optional<RoomManager> managerOpt = userRepository.findById("4")
                .map(user -> (RoomManager) user);
        
        if (managerOpt.isPresent()) {
            RoomManager manager = managerOpt.get();
            Optional<RoomRequest> requestOpt = manager.getPendingRequests().stream()
                    .filter(req -> req.getId().equals(requestId))
                    .findFirst();
            
            if (requestOpt.isPresent()) {
                model.addAttribute("request", requestOpt.get());
                return "roommanager/room-request-details";
            }
        }
        
        return "redirect:/roommanager/room-requests";
    }
    
    @PostMapping("/room-requests/{requestId}/approve")
    public String approveRoomRequest(@PathVariable String requestId, 
                                    @RequestParam String comments) {
        
        Optional<RoomManager> managerOpt = userRepository.findById("4")
                .map(user -> (RoomManager) user);
        
        if (managerOpt.isPresent()) {
            RoomManager manager = managerOpt.get();
            Optional<RoomRequest> requestOpt = manager.getPendingRequests().stream()
                    .filter(req -> req.getId().equals(requestId))
                    .findFirst();
            
            if (requestOpt.isPresent()) {
                RoomRequest request = requestOpt.get();
                
                // Using the facade pattern through the service
                roomManagerService.approveRoomRequest(request, comments);
                
                return "redirect:/roommanager/room-requests";
            }
        }
        
        return "redirect:/roommanager/dashboard";
    }
    
    @PostMapping("/room-requests/{requestId}/reject")
    public String rejectRoomRequest(@PathVariable String requestId, 
                                   @RequestParam String comments) {
        
        Optional<RoomManager> managerOpt = userRepository.findById("4")
                .map(user -> (RoomManager) user);
        
        if (managerOpt.isPresent()) {
            RoomManager manager = managerOpt.get();
            Optional<RoomRequest> requestOpt = manager.getPendingRequests().stream()
                    .filter(req -> req.getId().equals(requestId))
                    .findFirst();
            
            if (requestOpt.isPresent()) {
                RoomRequest request = requestOpt.get();
                
                // Using the facade pattern through the service
                roomManagerService.rejectRoomRequest(request, comments);
                
                return "redirect:/roommanager/room-requests";
            }
        }
        
        return "redirect:/roommanager/dashboard";
    }
    
    @GetMapping("/available-rooms")
    public String showAvailableRoomsForm(Model model) {
        return "roommanager/find-available-rooms";
    }
    
    @PostMapping("/available-rooms")
    public String findAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            Model model) {
        
        // Using the facade pattern through the service
        List<Room> availableRooms = roomManagerService.findAvailableRooms(startTime, endTime);
        
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("availableRooms", availableRooms);
        
        return "roommanager/available-rooms-results";
    }
} 