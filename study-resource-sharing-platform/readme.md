# StudyShare - Study Resource Sharing Platform

A modern **Spring Boot 3.2** web application that enables students to upload, share, discover, and preview study resources. Built with AWS S3 for cloud storage, MySQL for data persistence, and Spring Security for authentication.

---

## Features

- **User Authentication** - Register, login, and secure sessions with BCrypt password hashing
- **Resource Upload** - Upload PDFs, images, Word docs, PowerPoint, and text files (up to 100MB)
- **Search & Filter** - Full-text search by title, description, or tags with pagination
- **Tag-Based Browsing** - Click any tag to filter resources instantly
- **In-Browser Preview** - PDF viewer with page navigation (PDF.js) and image preview
- **Resource Management** - View, manage, and delete your own uploads
- **Resource Ownership** - Each upload is linked to the authenticated user
- **Responsive Design** - Modern UI that works on desktop, tablet, and mobile
- **Cloud Storage** - Secure file storage in AWS S3 with unique keys

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| **Framework** | Spring Boot 3.2.5 (Java 17) |
| **Security** | Spring Security 6 with BCrypt |
| **ORM** | Spring Data JPA / Hibernate |
| **Database** | MySQL 8+ |
| **Templates** | Thymeleaf with layout fragments |
| **Storage** | AWS S3 (SDK v2) |
| **Build** | Maven |

---

## Prerequisites

- **Java 17** or higher
- **Maven 3.8+**
- **MySQL 8.0+**
- **AWS Account** with an S3 bucket

---

## Quick Start

### 1. Database Setup

```sql
CREATE DATABASE study_resources;
```

### 2. Environment Variables

Set these before running (never hardcode credentials!):

```bash
export DB_URL=jdbc:mysql://localhost:3306/study_resources
export DB_USERNAME=root
export DB_PASSWORD=your-db-password
export AWS_S3_BUCKET_NAME=your-bucket-name
export AWS_S3_REGION=us-east-2
export AWS_ACCESS_KEY_ID=your-access-key
export AWS_SECRET_KEY=your-secret-key
```

Or on Windows:

```powershell
$env:DB_PASSWORD = "your-db-password"
$env:AWS_S3_BUCKET_NAME = "your-bucket-name"
$env:AWS_S3_REGION = "us-east-2"
$env:AWS_ACCESS_KEY_ID = "your-access-key"
$env:AWS_SECRET_KEY = "your-secret-key"
```

### 3. Build & Run

```bash
cd study-resource-sharing-platform
mvn clean install
mvn spring-boot:run
```

### 4. Access the Application

Open **http://localhost:8080** - you'll be redirected to the login page. Register a new account to get started.

---

## Project Structure

```
study-resource-sharing-platform/
├── src/main/java/com/studyshare/platform/
│   ├── StudyResourceSharingPlatformApplication.java
│   ├── config/
│   │   ├── S3Config.java              # AWS S3 client configuration
│   │   └── SecurityConfig.java        # Spring Security 6 filter chain
│   ├── controller/
│   │   ├── AuthController.java        # Login & registration
│   │   ├── HomeController.java        # Dashboard with stats
│   │   ├── ResourceController.java    # Upload, browse, search, delete
│   │   ├── PreviewController.java     # File preview (PDF/image)
│   │   └── CustomErrorController.java # Error pages
│   ├── dto/
│   │   └── RegistrationRequest.java   # Validated registration form
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   └── ResourceNotFoundException.java
│   ├── model/
│   │   ├── User.java                  # UserDetails implementation
│   │   ├── Role.java                  # ManyToMany roles
│   │   └── Resource.java             # File metadata + S3 reference
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── RoleRepository.java
│   │   └── ResourceRepository.java    # Search & pagination queries
│   └── service/
│       ├── UserService.java           # Registration + UserDetailsService
│       ├── ResourceService.java       # Upload, search, CRUD
│       └── PreviewService.java        # S3 URL generation
├── src/main/resources/
│   ├── application.properties         # Config with env var defaults
│   ├── static/css/style.css           # Unified modern stylesheet
│   └── templates/
│       ├── fragments/layout.html      # Shared navbar, footer, alerts
│       ├── home.html                  # Dashboard with stats & features
│       ├── login.html                 # Login form
│       ├── register.html              # Registration with validation
│       ├── upload.html                # File upload form
│       ├── resources.html             # Browse with search & pagination
│       ├── my-resources.html          # Manage your uploads
│       ├── preview.html               # PDF.js / image viewer
│       └── error.html                 # Custom error page
├── .gitignore
├── pom.xml
└── README.md
```

---

## Key Improvements Over Original

| Area | Before | After |
|------|--------|-------|
| **Spring Boot** | 2.5.5 (javax) | 3.2.5 (Jakarta EE) |
| **Security** | Deprecated `WebSecurityConfigurerAdapter`, CSRF disabled | Modern `SecurityFilterChain`, proper form login |
| **Data Model** | Broken Role (`@OneToMany` + `unique` = only 1 user per role) | Proper `@ManyToMany` User-Role relationship |
| **Resource Ownership** | Resources unlinked to users | Each resource belongs to its uploader |
| **Credentials** | AWS keys hardcoded in properties | Environment variables with safe defaults |
| **Search** | None | Full-text search + tag filtering + pagination |
| **File Types** | PDF + images only | PDF, images, Word, PowerPoint, text |
| **Frontend** | 7 separate CSS files, duplicated styles | Single unified CSS with design system |
| **Templates** | No shared layout, inconsistent design | Thymeleaf fragments with consistent modern UI |
| **Error Handling** | Generic catch-all | `@ControllerAdvice` with typed exceptions |
| **Validation** | Manual string checks | Jakarta Bean Validation with `@Valid` |
| **Code Quality** | Excessive Javadoc, bugs (broken `toString()`) | Clean, concise, Lombok-powered |

---

## Running Tests

```bash
mvn test
```

---

## Future Enhancements

- Comments / discussion on resources
- Rating system (1-5 stars)
- Bookmarking / favorites
- Admin dashboard with content moderation
- Download tracking and analytics
- User profile pages
- Email notifications
