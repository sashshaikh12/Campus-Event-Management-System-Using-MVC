# Campus Event Management System

## Overview
This is a Spring Boot application that manages campus events, room reservations, and student registrations.

## How to Run

### Option 1: Using the Batch File (Windows)
1. Double-click on the `run.bat` file in the root directory of the project
2. The application will start with debug logging enabled

### Option 2: Using Maven Command
Run the following command in the project root directory:

```bash
mvn spring-boot:run
```

## Accessing the Application
Once the application is running, open your browser and navigate to:
```
http://localhost:8080
```

## Direct Access to Dashboards
You can access any of the role-specific dashboards directly using these URLs:

- Club Head Dashboard: http://localhost:8080/clubhead/dashboard
- Faculty Dashboard: http://localhost:8080/faculty/dashboard
- HOD Dashboard: http://localhost:8080/hod/dashboard
- Room Manager Dashboard: http://localhost:8080/roommanager/dashboard
- Student Dashboard: http://localhost:8080/student/dashboard

## Default Users
The system comes pre-configured with the following default users:

- Club Head: username: `clubhead`, password: `password`
- Faculty: username: `faculty`, password: `password`
- HOD: username: `hod`, password: `password`
- Room Manager: username: `roommanager`, password: `password`
- Student: username: `student`, password: `password`

## Troubleshooting
If you encounter any errors:

1. Make sure you have Java 11 or later installed
2. Ensure that MySQL is running and accessible with the credentials in `application.properties`
3. Check the console output for detailed error messages

## Database Configuration
The application uses MySQL. You can modify the database settings in `src/main/resources/application.properties` 