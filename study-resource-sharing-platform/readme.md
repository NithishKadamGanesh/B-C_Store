# Study Resource Sharing Platform

The **Study Resource Sharing Platform** is a web application that enables students to upload, share, and manage study resources efficiently. With AWS S3 integration for file storage and MySQL for metadata management, students can securely upload study materials enriched with metadata and easily browse or preview resources within the platform.

## Features
- **Upload Study Resources**: Students can upload files with metadata, including title, description, and tags.
- **List and View Resources**: Display uploaded resources in a list format, with an option for previewing each resource.
- **AWS S3 Integration**: Secure file storage in an AWS S3 bucket.
- **MySQL Database**: Store metadata about each resource, such as title, description, tags, and upload date.

---

## Prerequisites

To set up the project locally, ensure the following dependencies are installed:
- **Java** 8 or higher
- **Maven** (for building the project)
- **MySQL** (version 8.0.40 or higher)
- **AWS S3 Bucket** for file storage

---

## MySQL Setup

1. Install **MySQL** version 8.0.40 (or higher).
2. Execute the following operations in order:

```bash
CREATE DATABASE study_resources;

USE study_resources;


CREATE USER 'db_new_user'@'localhost' IDENTIFIED BY 'password';

GRANT ALL PRIVILEGES ON file_upload_db.* TO 'db_new_user'@'localhost';

FLUSH PRIVILEGES;


CREATE TABLE file_metadata (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    file_url VARCHAR(255) NOT NULL,
    upload_time DATETIME NOT NULL
);
```

# AWS S3 Setup

1. **Create an S3 bucket** in your AWS account.
2. **Obtain your AWS Access Key, Secret Key,** and **S3 bucket name.**
3. Update your AWS credentials in the `application.properties` file:

```
   aws.s3.bucketName=your-bucket-name
   
   aws.accessKeyId=your-access-key
   
   aws.secretKey=your-secret-key
   
   aws.region=your-region
 ```

### Step 4: Access the Application

Once the application is running, open your browser and go to:

http://localhost:8080

This will load the home page of the Study Resource Sharing Platform.

---


## Folder Structure
```bash
study-resource-sharing-platform/
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.studyshare.platform
│   │   │       ├── controller       # Contains the application controllers
│   │   │       ├── model            # Contains the entity models
│   │   │       ├── repository       # Contains the JPA repository interfaces
│   │   │       └── service          # Contains the business logic services
│   ├── resources
│   │   └── application.properties   # Contains the application configurations
│   │   ├── static       # Contains the static content files(css etc.)
│   │   ├── template     # Contains the Views(html file)
│   ├── test
│   │   └── java
│   │       └── com.studyshare.platform # Contains the unit tests for services and controllers
├── pom.xml
├── README.md
└── target
```

## Running Unit Tests

The project includes unit tests for the services and controllers. You can run the tests using Maven:

```bash
mvn test;
```


## Future Enhancements

## 1. User Authentication

- Implements **login and registration functionality** for users.
- Allows secure access to the platform and personal content.

## 2. User Management

- Introduces **user roles** to enable content moderation by admins.
- Ensures that user permissions are aligned with their roles (e.g., admin, regular user).


## 3. Improved Search

- Adds **filters** to search uploaded resources by:
  - **Tags**
  - **Title**
  - **Description**
- Enhances content discoverability and user experience.


## 4. Enhanced File Preview

- Expands file preview capabilities to support **additional file types**.


## 5. Comments Feature

- Allows **user interaction and feedback** on uploaded content by enabling comments on notes.
- Includes:
  - A **comments section** on the note preview page.
  - A **form** to submit comments.


## 6. Rating System

- Enables users to **rate notes on a scale** (e.g., 1 to 5).
- Displays the **average rating** on the note preview page, providing feedback on note quality.


## 7. Bookmarking Feature

- Allows users to **bookmark notes** for quick access later.
- Includes a **bookmark management page** where users can view and remove their bookmarked notes.

## 8. Notifications

- Users receive **notifications** when an admin approves or rejects their notes.


## 9. Activity Feed

- A **personalized activity feed** displays recent user actions, such as adding comments, rating notes, and uploading notes.
- Allows users to **track their own actions** within the platform.



## 10. Enhanced File Analytics (Views and Downloads)

- Tracks and displays **view and download counts** for each note.
- Users can see how many times a note has been viewed or downloaded, adding value to the content.


## 11. Home Page with Dashboard

- A **personalized home page** with links to frequently used sections (My Notes, Bookmarks, Notifications).
- Provides a **dashboard view** of recent uploads and access to user-specific data, enhancing the user experience.



## 12. Session Management and Security Enhancements

- Configured **Spring Security** for:
  - **Session invalidation** on logout.
  - **Session fixation protection**.
  - **Single active session per user** for added security.
- These security configurations ensure secure session handling and protection against common session-based vulnerabilities.


## 13. Profile Management

- Users can **view and update** their profile information.
- Allows users to manage their personal details, improving user control over their account.

   