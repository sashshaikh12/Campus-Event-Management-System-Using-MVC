package com.example.demo.repository;

import com.example.demo.model.Appeal;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppealRepository extends JpaRepository<Appeal, Long> {
    
    List<Appeal> findByStudentOrderBySubmissionTimeDesc(User student);
    
    List<Appeal> findByStatusOrderBySubmissionTimeAsc(String status);
    
    List<Appeal> findByIsResolvedOrderBySubmissionTimeDesc(boolean isResolved);
    
    List<Appeal> findBySubmissionTimeBetweenOrderBySubmissionTimeDesc(LocalDateTime start, LocalDateTime end);
} 