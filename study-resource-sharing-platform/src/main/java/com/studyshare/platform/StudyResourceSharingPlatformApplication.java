package com.studyshare.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Study Resource Sharing Platform application.
 * <p>
 * This is a Spring Boot application that enables students to share, discover, and manage study resources.
 * The application is configured with MVC architecture, a Spring Boot backend, and MySQL for data storage.
 * Initial focus is on core functionalities such as user authentication, resource upload/download, and search.
 * </p>
 * 
 * <p>
 * This class contains the main method, which bootstraps the Spring Boot application.
 * </p>
 * 
 * @see SpringApplication
 * @since 1.0
 */
@SpringBootApplication
public class StudyResourceSharingPlatformApplication {

    /**
     * Main method to start the Study Resource Sharing Platform application.
     *
     * @param args Command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        SpringApplication.run(StudyResourceSharingPlatformApplication.class, args);
    }
}
