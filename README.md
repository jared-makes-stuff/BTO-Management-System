# BTO Management System

## Description

This system implements the BTO (Build-To-Order) application workflow for Singapore's public housing system. It manages the complete lifecycle from project creation by managers to application submission by applicants and processing by officers. The system implements proper role restrictions, authentication, and business logic according to HDB requirements.

## Table of Contents

- [Getting Started](#getting-started)
- [Usage](#usage)
- [System Architecture](#system-architecture)
- [Class Diagrams](#class-diagrams)
- [Sequence Diagrams](#sequence-diagrams)
- [Project Structure](#project-structure)
- [Developer Guide](#developer-guide)
- [Design Patterns](#design-patterns)
- [Contributors](#contributors)

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 23 or above

## Usage

1. **Login**: Use the provided NRIC and password credentials
2. **Navigate**: Follow the menu prompts to access different functionalities
3. **Data Management**: Use the data files menu to load/save data

### Sample User Credentials

- **Applicant**: NRIC: S1234567A, Password: password (John)
- **Officer**: NRIC: T2109876H, Password: password (Daniel)
- **Manager**: NRIC: S5678901G, Password: password (Jessica)

## System Architecture

The system follows a layered **Entity-Boundary-Controller (EBC)** architecture pattern:

```
┌─────────────────────────────────────────────────────────────────┐
│                        Boundary Layer                           │
│  (LoginView, ApplicantView, OfficerView, ManagerView, etc.)    │
├─────────────────────────────────────────────────────────────────┤
│                       Controller Layer                          │
│  (ApplicantController, OfficerController, ManagerController)   │
├─────────────────────────────────────────────────────────────────┤
│                         Entity Layer                            │
│  (User, Applicant, Officer, Manager, Project, BTOApplication)  │
├─────────────────────────────────────────────────────────────────┤
│                        Database Layer                           │
│  (ApplicantDatabase, ProjectDatabase, BookingDatabase, etc.)   │
└─────────────────────────────────────────────────────────────────┘
```

## Class Diagrams

### Core Entity Inheritance Hierarchy

```mermaid
classDiagram
    class User {
        <<abstract>>
        -String name
        -String nric
        -int age
        -MarriageStatusEnum maritalStatus
        -String password
        -Filter filter
        +getName() String
        +getNric() String
        +getAge() int
        +getPassword() String
        +setPassword(String)
        +getMaritalStatus() MarriageStatusEnum
    }
    
    class Applicant {
        -ArrayList~BTOApplication~ applications
        -ArrayList~Enquiry~ enquiries
        -Booking booking
        +addApplication(BTOApplication) boolean
        +addEnquiry(Enquiry) boolean
        +setBooking(Booking)
        +getApplications() ArrayList
    }
    
    class Officer {
        -ArrayList~OfficerApplication~ officerApplications
        -Project assignedProject
        +addOfficerApplication(OfficerApplication) boolean
        +setAssignedProject(Project)
        +getAssignedProject() Project
    }
    
    class Manager {
        -ArrayList~Project~ managedProjects
        +addProject(Project) boolean
        +removeProject(Project) boolean
        +getManagedProjects() ArrayList
    }
    
    User <|-- Applicant
    Applicant <|-- Officer
    User <|-- Manager
```

### Project and Application Domain Model

```mermaid
classDiagram
    class Project {
        -String projectName
        -String neighborhood
        -LocalDate applicationStartDate
        -LocalDate applicationEndDate
        -Manager manager
        -int officerSlots
        -VisibilityEnum visibility
        -ArrayList~FlatType~ flatTypes
        -ArrayList~Officer~ assignedOfficers
        +addFlatType(FlatType) boolean
        +assignOfficer(Officer) boolean
        +isWithinApplicationPeriod() boolean
    }
    
    class FlatType {
        -String flatTypeName
        -int unitCount
        -double price
        +getFlatTypeName() String
        +getUnitCount() int
        +decrementUnitCount() boolean
    }
    
    class BTOApplication {
        -String applicationID
        -Applicant applicant
        -Project project
        -FlatType flatType
        -BTOApplicationStatusEnum status
        -LocalDate submissionDate
        +approve()
        +reject()
        +getStatus() BTOApplicationStatusEnum
    }
    
    class OfficerApplication {
        -String applicationID
        -Officer officer
        -Project project
        -OfficerApplicationStatusEnum status
        +approve()
        +reject()
    }
    
    class Booking {
        -String bookingID
        -BTOApplication application
        -LocalDate bookingDate
        -BookingStatusEnum status
        +complete()
        +cancel()
    }
    
    class Enquiry {
        -String enquiryID
        -String content
        -String reply
        -Applicant submittedBy
        -Project project
        -EnquiryStatusEnum status
        +respond(String reply, User respondent)
    }
    
    class Receipt {
        -String receiptID
        -Booking booking
        -double amount
        -LocalDate issueDate
    }
    
    Project "1" --> "*" FlatType : contains
    Project "1" --> "*" Officer : assignedTo
    Project "1" --> "1" Manager : managedBy
    BTOApplication "*" --> "1" Project : appliesTo
    BTOApplication "*" --> "1" Applicant : submittedBy
    BTOApplication "*" --> "1" FlatType : selectedType
    OfficerApplication "*" --> "1" Officer : submittedBy
    OfficerApplication "*" --> "1" Project : appliesTo
    Booking "1" --> "1" BTOApplication : for
    Receipt "1" --> "1" Booking : for
    Enquiry "*" --> "1" Project : regarding
    Enquiry "*" --> "1" Applicant : submittedBy
```

### Controller Layer Architecture

```mermaid
classDiagram
    class AuthController {
        <<abstract>>
        +login(String nric, String password)* User
        +logout()*
        +isValidPassword(String password) boolean
    }
    
    class ApplicantAuth {
        +login(String nric, String password) Applicant
        +logout()
    }
    
    class OfficerAuth {
        +login(String nric, String password) Officer
        +logout()
    }
    
    class ManagerAuth {
        +login(String nric, String password) Manager
        +logout()
    }
    
    class ApplicantController {
        +viewAvailableProjects(Applicant) ArrayList
        +applyForProject(Applicant, Project, FlatType) boolean
        +submitEnquiry(Applicant, Project, String) boolean
        +viewApplicationStatus(Applicant) void
        +isEligibleForFlatType(Applicant, FlatType) boolean
    }
    
    class OfficerController {
        +viewAssignedProject(Officer) Project
        +processBooking(Officer, BTOApplication) boolean
        +respondToEnquiry(Officer, Enquiry, String) boolean
        +generateReceipt(Booking) Receipt
    }
    
    class ManagerController {
        +createProject(Manager, Project) boolean
        +approveApplication(Manager, BTOApplication) boolean
        +rejectApplication(Manager, BTOApplication) boolean
        +approveOfficerApplication(Manager, OfficerApplication) boolean
        +toggleProjectVisibility(Manager, Project) boolean
    }
    
    AuthController <|-- ApplicantAuth
    AuthController <|-- OfficerAuth
    AuthController <|-- ManagerAuth
```

### Database Layer

```mermaid
classDiagram
    class IDatabasePrintable {
        <<interface>>
        +printDatabase() void
    }
    
    class ApplicantDatabase {
        -HashMap~String, Applicant~ applicants
        +addApplicant(Applicant) boolean
        +findApplicantByNric(String) Applicant
        +getAllApplicants() ArrayList
    }
    
    class OfficerDatabase {
        -HashMap~String, Officer~ officers
        +addOfficer(Officer) boolean
        +findOfficerByNric(String) Officer
        +getAllOfficers() ArrayList
    }
    
    class ManagerDatabase {
        -HashMap~String, Manager~ managers
        +addManager(Manager) boolean
        +findManagerByNric(String) Manager
        +getAllManagers() ArrayList
    }
    
    class ProjectDatabase {
        -HashMap~String, Project~ projects
        +addProject(Project) boolean
        +findProjectByName(String) Project
        +getVisibleProjects() ArrayList
    }
    
    class BTOApplicationDatabase {
        -HashMap~String, BTOApplication~ applications
        +addApplication(BTOApplication) boolean
        +findByApplicant(Applicant) ArrayList
    }
    
    class BookingDatabase {
        -HashMap~String, Booking~ bookings
        +addBooking(Booking) boolean
        +findByApplication(BTOApplication) Booking
    }
    
    IDatabasePrintable <|.. ApplicantDatabase
    IDatabasePrintable <|.. OfficerDatabase
    IDatabasePrintable <|.. ManagerDatabase
    IDatabasePrintable <|.. ProjectDatabase
    IDatabasePrintable <|.. BTOApplicationDatabase
    IDatabasePrintable <|.. BookingDatabase
```

## Sequence Diagrams

### User Authentication Flow

```mermaid
sequenceDiagram
    participant U as User
    participant LV as LoginView
    participant AC as AuthController
    participant DB as UserDatabase
    participant MM as MainMenuView
    
    U->>LV: Enter NRIC and Password
    LV->>AC: login(nric, password)
    AC->>AC: isValidPassword(password)
    alt Invalid Password Format
        AC-->>LV: null (invalid format)
        LV-->>U: Display error message
    else Valid Password Format
        AC->>DB: findUserByNric(nric)
        alt User Not Found
            DB-->>AC: null
            AC-->>LV: null (user not found)
            LV-->>U: Display "Invalid credentials"
        else User Found
            DB-->>AC: User object
            AC->>AC: verifyPassword(password)
            alt Password Mismatch
                AC-->>LV: null (wrong password)
                LV-->>U: Display "Invalid credentials"
            else Password Match
                AC-->>LV: User object
                LV->>MM: displayUserMenu(User)
                MM-->>U: Show role-specific menu
            end
        end
    end
```

### BTO Application Submission Flow

```mermaid
sequenceDiagram
    participant A as Applicant
    participant AV as ApplicantView
    participant AC as ApplicantController
    participant PDB as ProjectDatabase
    participant ADB as BTOApplicationDatabase
    
    A->>AV: Select "Apply for BTO"
    AV->>AC: viewAvailableProjects(Applicant)
    AC->>PDB: getVisibleProjects()
    PDB-->>AC: List of visible projects
    AC->>AC: filterByEligibility(projects, Applicant)
    AC-->>AV: Filtered project list
    AV-->>A: Display available projects
    
    A->>AV: Select Project and FlatType
    AV->>AC: applyForProject(Applicant, Project, FlatType)
    AC->>AC: hasExistingApplication(Applicant)
    alt Already Has Application
        AC-->>AV: false (already applied)
        AV-->>A: Display "Already have pending application"
    else No Existing Application
        AC->>AC: isEligibleForFlatType(Applicant, FlatType)
        alt Not Eligible
            AC-->>AV: false (not eligible)
            AV-->>A: Display eligibility requirements
        else Eligible
            AC->>AC: createApplication(Applicant, Project, FlatType)
            AC->>ADB: addApplication(BTOApplication)
            ADB-->>AC: success
            AC-->>AV: true (application submitted)
            AV-->>A: Display "Application submitted successfully"
        end
    end
```

### Booking Processing Flow (Officer)

```mermaid
sequenceDiagram
    participant O as Officer
    participant OV as OfficerView
    participant OC as OfficerController
    participant ADB as BTOApplicationDatabase
    participant BDB as BookingDatabase
    participant RDB as ReceiptDatabase
    
    O->>OV: Select "Process Booking"
    OV->>OC: getPendingBookings(Officer)
    OC->>ADB: getApprovedApplications(Project)
    ADB-->>OC: List of approved applications
    OC-->>OV: Applications ready for booking
    OV-->>O: Display pending bookings
    
    O->>OV: Select Application to Process
    OV->>OC: processBooking(Officer, BTOApplication)
    OC->>OC: createBooking(BTOApplication)
    OC->>BDB: addBooking(Booking)
    BDB-->>OC: success
    OC->>OC: updateApplicationStatus(BOOKED)
    OC->>OC: decrementFlatTypeUnits(FlatType)
    
    OC->>OC: generateReceipt(Booking)
    OC->>RDB: addReceipt(Receipt)
    RDB-->>OC: success
    OC-->>OV: Booking completed with receipt
    OV-->>O: Display receipt and confirmation
```

### Enquiry Response Flow

```mermaid
sequenceDiagram
    participant S as Staff (Officer/Manager)
    participant SV as StaffView
    participant SC as Controller
    participant EDB as EnquiryDatabase
    
    S->>SV: Select "View Enquiries"
    SV->>SC: getPendingEnquiries(Project)
    SC->>EDB: findByProject(Project)
    EDB-->>SC: List of enquiries
    SC->>SC: filterByStatus(PENDING)
    SC-->>SV: Pending enquiries
    SV-->>S: Display pending enquiries
    
    S->>SV: Select Enquiry to Respond
    S->>SV: Enter Response
    SV->>SC: respondToEnquiry(Staff, Enquiry, response)
    SC->>SC: setEnquiryReply(response)
    SC->>SC: setRespondent(Staff)
    SC->>SC: setStatus(REPLIED)
    SC->>EDB: updateEnquiry(Enquiry)
    EDB-->>SC: success
    SC-->>SV: Enquiry responded
    SV-->>S: Display confirmation
```

### Manager Project Creation Flow

```mermaid
sequenceDiagram
    participant M as Manager
    participant MV as ManagerView
    participant MC as ManagerController
    participant PDB as ProjectDatabase
    
    M->>MV: Select "Create Project"
    MV-->>M: Display project creation form
    M->>MV: Enter project details
    
    MV->>MC: createProject(Manager, projectDetails)
    MC->>MC: validateProjectDates()
    alt Invalid Dates
        MC-->>MV: false (invalid date range)
        MV-->>M: Display "Invalid application period"
    else Valid Dates
        MC->>MC: checkManagerAvailability(Manager, dates)
        alt Manager Busy
            MC-->>MV: false (overlapping project)
            MV-->>M: Display "Already managing project in this period"
        else Manager Available
            MC->>MC: createProject(projectDetails)
            MC->>PDB: addProject(Project)
            PDB-->>MC: success
            MC->>MC: addToManagerProjects(Manager, Project)
            MC-->>MV: true (project created)
            MV-->>M: Display "Project created successfully"
        end
    end
```

## Project Structure

The system follows a layered architecture with:

- `boundary`: User interface components
- `controller`: Business logic components
- `entity`: Domain objects and data models
- `database`: Data access components
- `utils`: Helper classes and utilities
- `enums`: Enumeration classes for fixed sets of values
- `interfaces`: Interfaces used throughout the system

## Developer Guide

This guide provides instructions for developers who want to work on the BTO Management System project.

### Development Environment Setup

#### Prerequisites

1. **Java Development Kit (JDK)**

   - Install JDK 23 or higher
   - Set the JAVA_HOME environment variable
   - Add Java's bin directory to your system PATH
2. **IDE Setup**

   - Eclipse (recommended)

     - Download and install Eclipse IDE for Java Developers
     - Launch Eclipse and select a workspace directory
   - Alternative IDEs

     - IntelliJ IDEA
     - VS Code with Java extensions
3. **Git (Optional)**

   - Install Git for version control
   - Configure Git with your username and email

#### Project Setup

1. **Clone the Repository (if using Git)**

   ```
   git clone https://github.com/Jared0024/BTO-Management-System.git
   cd BTO-Management-System
   ```
2. **Configure Build Path**

   - Right-click on the project in the Package Explorer
   - Select Build Path > Configure Build Path
   - Ensure the JRE System Library is set to JDK 23 or higher
   - Click Apply and Close

### Building and Running the Project

#### Method 1: Using Eclipse IDE

1. **Build the Project**

   - Project > Build Project (or let Eclipse auto-build)
2. **Run the Application**

   - Right-click src/main/Main.java
   - Select Run As > Java Application

#### Method 2: Command Line Compilation

1. **Navigate to the Project Root**

   ```
   cd path/to/BTO-Management-System
   ```
2. **Compile the Java Files**

   ```
   mkdir -p bin

   cd /src
   javac -d ..\bin main\*.java boundary\*.java boundary\filehandlerview\*.java controller\*.java database\*.java entity\*.java utils\*.java enums\*.java interfaces\*.java
   ```
3. **Run the Application**

   ```
   cd /bin
   java main.Main
   ```

### Working with Data Files

The application uses CSV files stored in the `datafiles` directory to persist data. During development:

1. **Data File Location**

   - The application expects data files in the `datafiles` directory at the project root
   - When running from the `src` directory, it looks for files at `../datafiles/`
2. **Loading Data Files**

   - Use the "Handle Data Files" option in the main menu to load data
   - When starting, select "Read All Files" to load all sample data at once

### File Layout

```
BTO-Management-System/
├── Class Diagrams/              # UML class diagrams for system architecture
├── datafiles/                   # CSV data storage files
│   ├── applicants.csv          # Applicant user data
│   ├── officers.csv            # Officer user data
│   ├── managers.csv            # Manager user data
│   ├── projects.csv            # BTO project data
│   ├── applications.csv        # BTO applications data
│   ├── bookings.csv            # Appointment booking data
│   ├── enquiries.csv           # User enquiries data
│   └── receipts.csv            # Payment receipt data
├── html/                       # Generated JavaDoc documentation
├── SequenceDiagrams/          # UML sequence diagrams for system workflows
└── src/                       # Source code directory
    ├── boundary/              # User interface components
    │   ├── MainMenuView.java
    │   ├── ApplicantView.java
    │   ├── OfficerView.java
    │   ├── ManagerView.java
    │   ├── ProjectView.java
    │   ├── ApplicationView.java
    │   ├── BookingView.java
    │   ├── EnquiryView.java
    │   └── FileHandlerView.java
    ├── controller/            # Business logic and data processing
    │   ├── ApplicantController.java
    │   ├── OfficerController.java
    │   ├── ManagerController.java
    │   ├── ProjectController.java
    │   ├── ApplicationController.java
    │   ├── BookingController.java
    │   └── EnquiryController.java
    ├── database/              # Data access and storage management
    │   ├── ApplicantDatabase.java
    │   ├── OfficerDatabase.java
    │   ├── ManagerDatabase.java
    │   ├── ProjectDatabase.java
    │   ├── ApplicationDatabase.java
    │   ├── BookingDatabase.java
    │   ├── EnquiryDatabase.java
    │   └── ReceiptDatabase.java
    ├── entity/                # Domain objects and data models
    │   ├── Applicant.java
    │   ├── Officer.java
    │   ├── Manager.java
    │   ├── Project.java
    │   ├── BTOApplication.java
    │   ├── OfficerApplication.java
    │   ├── Booking.java
    │   ├── Enquiry.java
    │   └── Receipt.java
    ├── enums/                 # Enumeration types for constants
    │   ├── UserType.java
    │   ├── ApplicationStatus.java
    │   ├── BookingStatus.java
    │   ├── EnquiryStatus.java
    │   └── ProjectStatus.java
    ├── interfaces/            # Interface definitions
    │   ├── User.java
    │   ├── Application.java
    │   └── Database.java
    ├── main/                  # Application entry point
    │   └── Main.java
    └── utils/                 # Helper classes and utilities
        ├── DisplayMenu.java   # Menu display and navigation
        ├── FileHandler.java   # File I/O operations
        ├── InputHandler.java  # User input processing
        └── Validator.java     # Data validation
```

### Loading Data Files

1. Run Main.java
2. Select "1. Handle Data Files" from the main menu
3. Select "1. Read Data Files"
4. Choose "10. Read All Files"
5. Verify all data is loaded successfully

## Common Issues and Solutions

### File Path Issues

If you encounter file path issues when running from different directories:

1. Ensure the `datafiles` directory is present at the project root
2. When running from the src directory, the application looks for files at `../datafiles/`
3. When running from the project root, modify the path in `FileHandlerReadView.java` to use `datafiles/` instead

## Design Patterns

- Model-View-Controller (MVC)
- Entity-Boundary-Controller (EBC)

## SOLID Principles

The application adheres to SOLID principles:

- **Single Responsibility**: Each class has only one responsibility
- **Open/Closed**: Entities are open for extension but closed for modification
- **Liskov Substitution**: User subtypes can substitute their their base type
- **Interface Segregation**: Focused interfaces for specific functionalities
- **Dependency Inversion**: High-level modules depend on abstractions

## Contributors

- JARED TAN SHU YI (CHEN SHUYI)
- AARON LOH WEI HAN
- TOH DE XUE
- TYLER TAN
- CHAI ZHI KANG
