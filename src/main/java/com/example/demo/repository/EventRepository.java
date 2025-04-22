package com.example.demo.repository;

import com.example.demo.model.ClubHead;
import com.example.demo.model.Event;
import com.example.demo.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    List<Event> findByOrganizer(ClubHead organizer);
    List<Event> findByStartDateTimeAfter(LocalDateTime dateTime);
    List<Event> findByStatus(String status);
    
    // Add Faculty related methods
    List<Event> findByFaculty(Faculty faculty);
    List<Event> findByFacultyOrderByStartDateTimeDesc(Faculty faculty);
    List<Event> findByFacultyAndEndDateTimeBefore(Faculty faculty, LocalDateTime dateTime);
    List<Event> findByFacultyAndStartDateTimeAfter(Faculty faculty, LocalDateTime dateTime);
    List<Event> findTop5ByFacultyOrderByCreatedAtDesc(Faculty faculty);
    Long countByFaculty(Faculty faculty);
    
    // More basic methods
    List<Event> findByStatusAndDepartment(String status, String department);
    List<Event> findTop5ByDepartmentOrderByCreatedAtDesc(String department);
    Long countByStatusAndDepartment(String status, String department);
    Long countByDepartmentAndStartDateTimeAfter(String department, LocalDateTime dateTime);
    Long countByDepartmentAndEndDateTimeBefore(String department, LocalDateTime dateTime);
} 