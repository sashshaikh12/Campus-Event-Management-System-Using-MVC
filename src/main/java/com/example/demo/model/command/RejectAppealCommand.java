package com.example.demo.model.command;

import com.example.demo.model.Appeal;

/**
 * Concrete command to reject a student appeal
 */
public class RejectAppealCommand implements Command {
    private Appeal appeal;
    private String comments;
    
    public RejectAppealCommand(Appeal appeal, String comments) {
        this.appeal = appeal;
        this.comments = comments;
    }
    
    @Override
    public void execute() {
        appeal.reject(comments);
    }
} 