package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class Event {
    @Id
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, length = 1000)
    private String description;
    
    @Column(nullable = false)
    private LocalDateTime startDateTime;
    
    @Column(nullable = false)
    private LocalDateTime endDateTime;
    
    @Column(nullable = false)
    private String venue;
    
    @Column(nullable = false)
    private Integer maxParticipants;
    
    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private ClubHead organizer;
    
    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;
    
    @Column(nullable = false)
    private String status; // PENDING, APPROVED, REJECTED, COMPLETED, CANCELLED
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = true)
    private String department;
    
    @ManyToMany(mappedBy = "registeredEvents", fetch = FetchType.LAZY)
    private List<Student> registeredStudents;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "event_attendance",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> attendedStudents;
    
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public Event() {
        this.id = UUID.randomUUID().toString();
        this.registeredStudents = new ArrayList<>();
        this.attendedStudents = new ArrayList<>();
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public Event(String name, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, 
                String venue, int maxParticipants, ClubHead organizer, Faculty faculty) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.venue = venue;
        this.maxParticipants = maxParticipants;
        this.organizer = organizer;
        this.faculty = faculty;
        this.status = "PENDING";
        this.registeredStudents = new ArrayList<>();
        this.attendedStudents = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public ClubHead getOrganizer() {
        return organizer;
    }

    public void setOrganizer(ClubHead organizer) {
        this.organizer = organizer;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Student> getRegisteredStudents() {
        return registeredStudents;
    }

    public void setRegisteredStudents(List<Student> registeredStudents) {
        this.registeredStudents = registeredStudents;
    }

    public List<Student> getAttendedStudents() {
        return attendedStudents;
    }

    public void setAttendedStudents(List<Student> attendedStudents) {
        this.attendedStudents = attendedStudents;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void addRegisteredStudent(Student student) {
        if (this.registeredStudents.size() < maxParticipants && !student.isBlacklisted()) {
            this.registeredStudents.add(student);
            student.registerForEvent(this);
        }
    }

    public void markAttendance(Student student) {
        if (this.registeredStudents.contains(student) && !this.attendedStudents.contains(student)) {
            this.attendedStudents.add(student);
        }
    }

    public void markAbsent(Student student) {
        if (this.registeredStudents.contains(student) && !this.attendedStudents.contains(student)) {
            student.incrementAbsenceCount();
        }
    }
} 