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
        try {
            System.out.println("\n\n==== INITIALIZING DATABASE ====");
            System.out.println("Current user count: " + userRepository.count());
            
            // Only initialize if the repository is empty
            if (userRepository.count() == 0) {
                System.out.println("No users found. Creating test users...");
                
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
                System.out.println("Created user: " + clubHead.getUsername() + ", password: " + clubHead.getPassword() + ", role: " + clubHead.getRole());
                
                // Add a custom user for testing login
                ClubHead customClubHead = new ClubHead();
                customClubHead.setId("custom1");
                customClubHead.setUsername("user");
                customClubHead.setPassword("123");
                customClubHead.setName("Custom Club Head");
                customClubHead.setEmail("custom@example.com");
                customClubHead.setRole("CLUB_HEAD");
                customClubHead.setClubName("Custom Club");
                userRepository.save(customClubHead);
                System.out.println("Created user: " + customClubHead.getUsername() + ", password: " + customClubHead.getPassword() + ", role: " + customClubHead.getRole());
                
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
                System.out.println("Created user: " + faculty.getUsername() + ", password: " + faculty.getPassword() + ", role: " + faculty.getRole());
                
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
                System.out.println("Created user: " + hod.getUsername() + ", password: " + hod.getPassword() + ", role: " + hod.getRole());
                
                // Create a RoomManager
                RoomManager roomManager = new RoomManager();
                roomManager.setId("4");
                roomManager.setUsername("roommanager");
                roomManager.setPassword("password");
                roomManager.setName("Room Manager User");
                roomManager.setEmail("roommanager@example.com");
                roomManager.setRole("ROOM_MANAGER");
                userRepository.save(roomManager);
                System.out.println("Created user: " + roomManager.getUsername() + ", password: " + roomManager.getPassword() + ", role: " + roomManager.getRole());
                
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
                System.out.println("Created user: " + student.getUsername() + ", password: " + student.getPassword() + ", role: " + student.getRole());
                
                System.out.println("Test users created successfully!");
                System.out.println("Total users in database: " + userRepository.count());
                
                // Verify users were created
                userRepository.findAll().forEach(user -> {
                    System.out.println("Verified user in DB - ID: " + user.getId() + 
                                      ", Username: " + user.getUsername() + 
                                      ", Password: " + user.getPassword() + 
                                      ", Role: " + user.getRole());
                });
            } else {
                System.out.println("Database already has users. Skipping initialization.");
                System.out.println("Existing users in database:");
                userRepository.findAll().forEach(user -> {
                    System.out.println("User - ID: " + user.getId() + 
                                      ", Username: " + user.getUsername() + 
                                      ", Password: " + user.getPassword() + 
                                      ", Role: " + user.getRole());
                });
            }
            System.out.println("==== DATABASE INITIALIZATION COMPLETE ====\n\n");
        } catch (Exception e) {
            System.err.println("ERROR DURING DATABASE INITIALIZATION:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
} 