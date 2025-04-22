package com.example.demo.model.command;

import com.example.demo.model.EventRequest;

/**
 * Concrete command to approve an event request
 */
public class ApproveEventCommand implements Command {
    private EventRequest eventRequest;
    private String comments;
    
    public ApproveEventCommand(EventRequest eventRequest, String comments) {
        this.eventRequest = eventRequest;
        this.comments = comments;
    }
    
    @Override
    public void execute() {
        eventRequest.approve(comments);
    }
} 