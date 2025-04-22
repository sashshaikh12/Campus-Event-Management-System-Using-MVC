package com.example.demo.model.service;

import com.example.demo.model.Appeal;
import com.example.demo.repository.AppealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppealService {

    @Autowired
    private AppealRepository appealRepository;
    
    /**
     * Create a new appeal from a student
     */
    public Appeal createAppeal(String studentId, String hodId, String reason, String description) {
        Appeal appeal = new Appeal();
        appeal.setStudentId(studentId);
        appeal.setHodId(hodId);
        appeal.setReason(reason);
        appeal.setDescription(description);
        appeal.setAppealDate(LocalDateTime.now());
        appeal.setSubmissionTime(LocalDateTime.now());
        appeal.setStatus("PENDING");
        appeal.setResolved(false);
        appeal.setEventId("no-event"); // Default value for eventId
        
        return appealRepository.save(appeal);
    }
    
    /**
     * Get all appeals submitted by a student
     */
    public List<Appeal> getAppealsByStudentId(String studentId) {
        return appealRepository.findByStudentIdOrderBySubmissionTimeDesc(studentId);
    }
    
    /**
     * Get a specific appeal by ID
     */
    public Appeal getAppealById(Long id) {
        return appealRepository.findById(id).orElse(null);
    }
}