package com.example.demo.model.service;

import com.example.demo.model.EventRequest;
import com.example.demo.model.HOD;
import com.example.demo.repository.EventRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for EventRequest functionality
 */
@Service
public class EventRequestService {
    
    @Autowired
    private EventRequestRepository eventRequestRepository;
    
    /**
     * Get all event requests
     */
    public List<EventRequest> getAllEventRequests() {
        try {
            return eventRequestRepository.findAllByOrderByRequestDateDesc();
        } catch (Exception e) {
            System.err.println("Error fetching all event requests: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get event requests by status
     */
    public List<EventRequest> getEventRequestsByStatus(String status) {
        try {
            return eventRequestRepository.findByStatusOrderByRequestDateDesc(status);
        } catch (Exception e) {
            System.err.println("Error fetching event requests by status: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get event requests for a specific department
     */
    public List<EventRequest> getEventRequestsByDepartment(String department) {
        try {
            return eventRequestRepository.findByEventDepartment(department);
        } catch (Exception e) {
            System.err.println("Error fetching event requests by department: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get pending event requests for a specific department
     */
    public List<EventRequest> getPendingEventRequestsByDepartment(String department) {
        try {
            return eventRequestRepository.findPendingByEventDepartment(department);
        } catch (Exception e) {
            System.err.println("Error fetching pending event requests by department: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get event request by ID
     */
    public Optional<EventRequest> getEventRequestById(String id) {
        try {
            return eventRequestRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Error fetching event request by ID: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Approve an event request
     */
    @Transactional
    public boolean approveEventRequest(String id, String comments) {
        try {
            Optional<EventRequest> requestOpt = eventRequestRepository.findById(id);
            if (requestOpt.isPresent()) {
                EventRequest request = requestOpt.get();
                request.approve(comments);
                eventRequestRepository.save(request);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error approving event request: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Reject an event request
     */
    @Transactional
    public boolean rejectEventRequest(String id, String comments) {
        try {
            Optional<EventRequest> requestOpt = eventRequestRepository.findById(id);
            if (requestOpt.isPresent()) {
                EventRequest request = requestOpt.get();
                request.reject(comments);
                eventRequestRepository.save(request);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error rejecting event request: " + e.getMessage());
            return false;
        }
    }
} 