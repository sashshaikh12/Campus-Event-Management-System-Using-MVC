package com.example.demo.repository;

import com.example.demo.model.EventRequest;
import com.example.demo.model.HOD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRequestRepository extends JpaRepository<EventRequest, String> {
    
    // Find event requests by status
    List<EventRequest> findByStatus(String status);
    
    // Find event requests by department (from the associated event)
    @Query("SELECT er FROM EventRequest er JOIN er.event e WHERE e.department = :department")
    List<EventRequest> findByEventDepartment(@Param("department") String department);
    
    // Find pending event requests for a specific department
    @Query("SELECT er FROM EventRequest er JOIN er.event e WHERE er.status = 'PENDING' AND e.department = :department")
    List<EventRequest> findPendingByEventDepartment(@Param("department") String department);
    
    // Find all event requests with their events ordered by request date (newest first)
    List<EventRequest> findAllByOrderByRequestDateDesc();
    
    // Find pending event requests ordered by request date
    List<EventRequest> findByStatusOrderByRequestDateDesc(String status);
} 