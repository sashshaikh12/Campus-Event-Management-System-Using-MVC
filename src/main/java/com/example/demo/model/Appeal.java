package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appeals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;
    
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    
    @Column(nullable = false)
    private String reason;
    
    @Column(length = 2000)
    private String description;
    
    @Column(nullable = false)
    private String status; // PENDING, APPROVED, REJECTED, MORE_INFO_REQUESTED
    
    private String hodResponse;
    
    @Column(nullable = false)
    private LocalDateTime submissionTime;
    
    private LocalDateTime responseTime;
    
    @Column(nullable = false)
    private Boolean isResolved;
    
    @PrePersist
    protected void onCreate() {
        submissionTime = LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
        isResolved = false;
    }
    
    public void approve(String response) {
        this.status = "APPROVED";
        this.hodResponse = response;
        this.responseTime = LocalDateTime.now();
        this.isResolved = true;
    }
    
    public void reject(String response) {
        this.status = "REJECTED";
        this.hodResponse = response;
        this.responseTime = LocalDateTime.now();
        this.isResolved = true;
    }
    
    public void requestMoreInfo(String response) {
        this.status = "MORE_INFO_REQUESTED";
        this.hodResponse = response;
        this.responseTime = LocalDateTime.now();
    }
} 