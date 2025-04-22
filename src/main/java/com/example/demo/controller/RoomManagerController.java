package com.example.demo.controller;

import com.example.demo.model.RoomManager;
import com.example.demo.model.RoomRequest;
import com.example.demo.model.service.RoomManagerService;
import com.example.demo.repository.RoomRequestRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/roommanager")
public class RoomManagerController {

    @Autowired
    private RoomManagerService roomManagerService;
    
    @Autowired
    private RoomRequestRepository roomRequestRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Using a fixed room manager ID for demo purposes
    private RoomManager getCurrentRoomManager() {
        try {
            // Fixed room manager ID: user-301 
            Optional<RoomManager> roomManagerOpt = userRepository.findById("user-301")
                    .filter(user -> user instanceof RoomManager)
                    .map(user -> (RoomManager) user);
            
            return roomManagerOpt.orElseThrow(() -> new RuntimeException("Fixed room manager with ID user-301 not found"));
        } catch (Exception e) {
            System.err.println("Error getting current room manager: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            RoomManager roomManager = getCurrentRoomManager();
            model.addAttribute("roomManager", roomManager);
            
            // Get pending room requests count
            List<RoomRequest> pendingRequests = roomRequestRepository.findByStatus("PENDING");
            model.addAttribute("pendingRequestsCount", pendingRequests.size());
            
            return "roommanager/dashboard";
        } catch (Exception e) {
            System.err.println("Error in dashboard: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading the dashboard");
            return "error";
        }
    }
    
    @GetMapping("/rooms")
    public String rooms(Model model) {
        try {
            RoomManager roomManager = getCurrentRoomManager();
            model.addAttribute("roomManager", roomManager);
            model.addAttribute("rooms", roomManager.getManagedRooms());
            
            return "roommanager/rooms";
        } catch (Exception e) {
            System.err.println("Error in rooms: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading rooms");
            return "error";
        }
    }
    
    @GetMapping("/rooms/create")
    public String createRoom() {
        return "roommanager/create-room";
    }
    
    @GetMapping("/room-requests")
    public String roomRequests(Model model, @RequestParam(required = false) String status) {
        try {
            RoomManager roomManager = getCurrentRoomManager();
            
            // Get room requests based on status filter
            List<RoomRequest> roomRequests;
            if (status != null && !status.isEmpty() && !status.equals("all")) {
                roomRequests = roomRequestRepository.findByStatus(status.toUpperCase());
            } else {
                roomRequests = roomRequestRepository.findAll();
            }
            
            // Separate requests by status for display
            List<RoomRequest> pendingRequests = roomRequests.stream()
                .filter(req -> "PENDING".equals(req.getStatus()))
                .toList();
                
            List<RoomRequest> approvedRequests = roomRequests.stream()
                .filter(req -> "APPROVED".equals(req.getStatus()))
                .toList();
                
            List<RoomRequest> rejectedRequests = roomRequests.stream()
                .filter(req -> "REJECTED".equals(req.getStatus()))
                .toList();
            
            model.addAttribute("roomManager", roomManager);
            model.addAttribute("pendingRequests", pendingRequests);
            model.addAttribute("approvedRequests", approvedRequests);
            model.addAttribute("rejectedRequests", rejectedRequests);
            model.addAttribute("selectedStatus", status);
            
            return "roommanager/room-requests";
        } catch (Exception e) {
            System.err.println("Error in roomRequests: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading room requests");
            return "error";
        }
    }
    
    @PostMapping("/room-requests/{id}/approve")
    public String approveRoomRequest(
            @PathVariable("id") String id,
            @RequestParam("comments") String comments,
            RedirectAttributes redirectAttributes) {
        try {
            RoomRequest request = roomRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room request not found"));
            
            request.approve(comments);
            roomRequestRepository.save(request);
            
            redirectAttributes.addFlashAttribute("successMessage", "Room request approved successfully");
            return "redirect:/roommanager/room-requests";
        } catch (Exception e) {
            System.err.println("Error in approveRoomRequest: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to approve room request: " + e.getMessage());
            return "redirect:/roommanager/room-requests";
        }
    }
    
    @PostMapping("/room-requests/{id}/reject")
    public String rejectRoomRequest(
            @PathVariable("id") String id,
            @RequestParam("reason") String reason,
            RedirectAttributes redirectAttributes) {
        try {
            RoomRequest request = roomRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room request not found"));
            
            request.reject(reason);
            roomRequestRepository.save(request);
            
            redirectAttributes.addFlashAttribute("successMessage", "Room request rejected successfully");
            return "redirect:/roommanager/room-requests";
        } catch (Exception e) {
            System.err.println("Error in rejectRoomRequest: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to reject room request: " + e.getMessage());
            return "redirect:/roommanager/room-requests";
        }
    }
    
    @GetMapping("/profile")
    public String profile(Model model) {
        try {
            RoomManager roomManager = getCurrentRoomManager();
            model.addAttribute("roomManager", roomManager);
            
            return "roommanager/profile";
        } catch (Exception e) {
            System.err.println("Error in profile: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading profile");
            return "error";
        }
    }
}