package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    @Transactional
    public void initData() {
        // Only initialize if the repository is empty
        if (userRepository.count() == 0) {
            System.out.println("Initializing test users...");
            
            // Create a ClubHead
            ClubHead clubHead = new ClubHead();
            clubHead.setId("1");
            clubHead.setUsername("clubhead");
            clubHead.setPassword("password");
            clubHead.setName("Club Head User");
            clubHead.setEmail("clubhead@example.com");
            clubHead.setRole("CLUB_HEAD");
            clubHead.setClubName("Tech Club");
            userRepository.save(clubHead);
            
            // Create a Faculty
            Faculty faculty = new Faculty();
            faculty.setId("2");
            faculty.setUsername("faculty");
            faculty.setPassword("password");
            faculty.setName("Faculty User");
            faculty.setEmail("faculty@example.com");
            faculty.setRole("FACULTY");
            faculty.setDepartment("Computer Science");
            userRepository.save(faculty);
            
            // Create a HOD
            HOD hod = new HOD();
            hod.setId("3");
            hod.setUsername("hod");
            hod.setPassword("password");
            hod.setName("HOD User");
            hod.setEmail("hod@example.com");
            hod.setRole("HOD");
            hod.setDepartment("Computer Science");
            userRepository.save(hod);
            
            // Create a RoomManager
            RoomManager roomManager = new RoomManager();
            roomManager.setId("4");
            roomManager.setUsername("roommanager");
            roomManager.setPassword("password");
            roomManager.setName("Room Manager User");
            roomManager.setEmail("roommanager@example.com");
            roomManager.setRole("ROOM_MANAGER");
            userRepository.save(roomManager);
            
            // Create a Student
            Student student = new Student();
            student.setId("5");
            student.setUsername("student");
            student.setPassword("password");
            student.setName("Student User");
            student.setEmail("student@example.com");
            student.setRole("STUDENT");
            student.setRollNumber("CS123");
            student.setDepartment("Computer Science");
            student.setSemester(5);
            userRepository.save(student);
            
            System.out.println("Test users created successfully!");
        }
    }
} 