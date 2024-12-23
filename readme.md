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


Ensure the credentials in application.properties match your local MySQL setup:
spring.datasource.username=root
spring.datasource.password=password
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

The project includes unit tests for the services and controllers.



```bash
Run Tests Using Your IDE:
IntelliJ IDEA:
Right-click the test class or method and select Run 'TestClassName'.
Eclipse:
Right-click the test class or method, navigate to Run As, and select JUnit Test.


Use the following Maven command to execute all tests:
mvn test;
```