package com.example.demo.model.command;

import com.example.demo.model.Appeal;

/**
 * Concrete command to approve a student appeal
 */
public class ApproveAppealCommand implements Command {
    private Appeal appeal;
    private String comments;
    
    public ApproveAppealCommand(Appeal appeal, String comments) {
        this.appeal = appeal;
        this.comments = comments;
    }
    
    @Override
    public void execute() {
        appeal.approve(comments);
    }
} 