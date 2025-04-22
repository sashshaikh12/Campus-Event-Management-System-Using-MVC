package com.example.demo.model.service;

import com.example.demo.model.Appeal;
import com.example.demo.model.EventRequest;
import com.example.demo.model.HOD;
import com.example.demo.model.command.ApproveAppealCommand;
import com.example.demo.model.command.ApproveEventCommand;
import com.example.demo.model.command.Command;
import com.example.demo.model.command.RejectAppealCommand;
import com.example.demo.model.command.RejectEventCommand;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for HOD functionality
 * Implements Command Pattern and GRASP Polymorphism
 */
@Service
public class HODService {
    
    /**
     * Get all pending event requests for a HOD
     */
    public List<EventRequest> getPendingEventRequests(HOD hod) {
        return hod.getPendingRequests();
    }
    
    /**
     * Get all pending appeals for a HOD
     */
    public List<Appeal> getPendingAppeals(HOD hod) {
        return hod.getPendingAppeals();
    }
    
    /**
     * Create and execute a command to approve an event request
     * Implements Command Pattern
     */
    public void approveEventRequest(EventRequest request, String comments) {
        Command command = new ApproveEventCommand(request, comments);
        command.execute();
    }
    
    /**
     * Create and execute a command to reject an event request
     * Implements Command Pattern
     */
    public void rejectEventRequest(EventRequest request, String comments) {
        Command command = new RejectEventCommand(request, comments);
        command.execute();
    }
    
    /**
     * Create and execute a command to approve a student appeal
     * Implements Command Pattern
     */
    public void approveAppeal(Appeal appeal, String comments) {
        Command command = new ApproveAppealCommand(appeal, comments);
        command.execute();
    }
    
    /**
     * Create and execute a command to reject a student appeal
     * Implements Command Pattern
     */
    public void rejectAppeal(Appeal appeal, String comments) {
        Command command = new RejectAppealCommand(appeal, comments);
        command.execute();
    }
    
    /**
     * Process a request or appeal using polymorphism
     * This demonstrates GRASP Polymorphism pattern
     */
    public void processRequest(Command command) {
        command.execute();
    }
} 