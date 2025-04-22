package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "appeals")
public class Appeal {
    @Id
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @Column(nullable = false)
    private String reason;
    
    @Column(nullable = false)
    private String status; // PENDING, APPROVED, REJECTED
    
    private String hodComments;
    
    @Column(name = "appeal_date", nullable = false)
    private LocalDateTime appealDate;
    
    @Column(name = "response_date")
    private LocalDateTime responseDate;

    public Appeal() {
        this.id = UUID.randomUUID().toString();
        this.appealDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    public Appeal(Student student, String reason) {
        this.id = UUID.randomUUID().toString();
        this.student = student;
        this.reason = reason;
        this.appealDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHodComments() {
        return hodComments;
    }

    public void setHodComments(String hodComments) {
        this.hodComments = hodComments;
    }

    public LocalDateTime getAppealDate() {
        return appealDate;
    }

    public void setAppealDate(LocalDateTime appealDate) {
        this.appealDate = appealDate;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }

    public void approve(String comments) {
        this.status = "APPROVED";
        this.hodComments = comments;
        this.responseDate = LocalDateTime.now();
        
        // Remove blacklist status
        this.student.setBlacklisted(false);
        this.student.setAbsenceCount(0);
    }

    public void reject(String comments) {
        this.status = "REJECTED";
        this.hodComments = comments;
        this.responseDate = LocalDateTime.now();
    }
} 