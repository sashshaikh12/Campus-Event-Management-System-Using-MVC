package com.example.demo.repository;

import com.example.demo.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    List<Event> findByStatus(String status);
    List<Event> findByOrganizerUsername(String username);
    List<Event> findByFacultyUsername(String username);
    List<Event> findByStartDateTimeAfter(LocalDateTime dateTime);
} 