package com.example.demo.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "appeals")
public class Appeal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "appeal_date", nullable = false)
    private LocalDateTime appealDate;
    
    @Column(name = "hod_comments")
    private String hodComments;
    
    @Column(nullable = false)
    private String reason;
    
    @Column(name = "response_date")
    private LocalDateTime responseDate;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "student_id", nullable = false)
    private String studentId;
    
    @Column(name = "hod_id")
    private String hodId;
    
    @Column(length = 2000)
    private String description;
    
    @Column(name = "hod_response")
    private String hodResponse;
    
    @Column(name = "is_resolved", nullable = false)
    private boolean isResolved;
    
    @Column(name = "response_time")
    private LocalDateTime responseTime;
    
    @Column(name = "submission_time", nullable = false)
    private LocalDateTime submissionTime;
    
    @Column(name = "event_id")
    private String eventId;
    
    public Appeal() {
        this.appealDate = LocalDateTime.now();
        this.submissionTime = LocalDateTime.now();
        this.status = "PENDING";
        this.isResolved = false;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getAppealDate() {
        return appealDate;
    }

    public void setAppealDate(LocalDateTime appealDate) {
        this.appealDate = appealDate;
    }

    public String getHodComments() {
        return hodComments;
    }

    public void setHodComments(String hodComments) {
        this.hodComments = hodComments;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getHodId() {
        return hodId;
    }

    public void setHodId(String hodId) {
        this.hodId = hodId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHodResponse() {
        return hodResponse;
    }

    public void setHodResponse(String hodResponse) {
        this.hodResponse = hodResponse;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }

    public LocalDateTime getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(LocalDateTime submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    
    /**
     * Approve an appeal and set relevant fields
     * @param comments Comments from HOD regarding approval
     */
    public void approve(String comments) {
        this.status = "APPROVED";
        this.hodResponse = comments;
        this.responseTime = LocalDateTime.now();
        this.responseDate = LocalDateTime.now();
        this.isResolved = true;
    }
    
    /**
     * Reject an appeal and set relevant fields
     * @param reason Reason for rejection from HOD
     */
    public void reject(String reason) {
        this.status = "REJECTED";
        this.hodResponse = reason;
        this.responseTime = LocalDateTime.now();
        this.responseDate = LocalDateTime.now();
        this.isResolved = true;
    }
}