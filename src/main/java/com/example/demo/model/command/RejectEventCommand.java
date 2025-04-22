package com.example.demo.model.command;

import com.example.demo.model.EventRequest;

/**
 * Concrete command to reject an event request
 */
public class RejectEventCommand implements Command {
    private EventRequest eventRequest;
    private String comments;
    
    public RejectEventCommand(EventRequest eventRequest, String comments) {
        this.eventRequest = eventRequest;
        this.comments = comments;
    }
    
    @Override
    public void execute() {
        eventRequest.reject(comments);
    }
} 