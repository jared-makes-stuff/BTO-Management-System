package boundary.managerview;

import controller.*;
import entity.*;
import enums.*;
import interfaces.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Boundary class for handling and displaying report generation functionality for managers.
 * This view allows managers to generate various reports about applicants and projects,
 * including filtered reports based on specific criteria like age, marital status,
 * flat type, and project.
 * 
 * @implements IDisplayable
 */
public class ManagerReportsView implements IDisplayable {

    private Scanner scanner;
    private Manager currentManager;
    private ManagerController managerController;

    /**
     * Constructor that initializes the view with a specified manager
     * 
     * @param manager The Manager entity who will be using this view
     */
    public ManagerReportsView(Manager manager) {
        this.scanner = new Scanner(System.in);
        this.managerController = new ManagerController();
        this.currentManager = manager;
    }

    /**
     * Displays the main menu for report generation and handles user interaction.
     * Provides options to generate different types of reports or return to the main manager menu.
     */
    @Override
    public void display() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n===== GENERATE REPORTS =====");
            System.out.println("1. Generate Reports");
            System.out.println("2. Generate Report with Filter");
            System.out.println("0. Return to Manager Menu");
            System.out.print("Enter your choice: ");
            
            String input = scanner.nextLine();
            exit = getUserInput(input);
        }
    }

    /**
     * Processes user input from the main menu and calls the appropriate method.
     * 
     * @param input The user's menu selection as a string
     * @return boolean True if the user wants to exit the menu, false otherwise
     */
    private boolean getUserInput(String input) {
        switch (input) {
            case "1":
                generateReports();
                return false;
            case "2":
                generateReportsWithFilter();
                return false;
            case "0":
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
    }

    /**
     * Displays a submenu for selecting the type of report to generate.
     * Options include applicant reports and project reports.
     */
    private void generateReports() {
        System.out.println("\n===== GENERATE REPORTS =====");
        System.out.println("1. Generate Applicant Report");
        System.out.println("2. Generate Project Report");
        System.out.println("0. Cancel");
        System.out.print("Enter your choice: ");
        
        String input = scanner.nextLine();
        
        switch (input) {
            case "1":
                generateApplicantReport();
                break;
            case "2":
                generateProjectReport();
                break;
            case "0":
                System.out.println("Operation cancelled.");
                break;
            default:
                System.out.println("Invalid choice. Operation cancelled.");
                break;
        }
    }

    /**
     * Generates and displays a comprehensive report on all applicants in the system.
     * Shows applicant details including personal information and application status.
     */
    private void generateApplicantReport() {
        System.out.println("\n===== APPLICANT REPORT =====");
        
        ArrayList<Applicant> applicants = managerController.generateApplicantReport();
        
        if (applicants.isEmpty()) {
            System.out.println("No applicants to report.");
            return;
        }
        
        displayApplicantReport(applicants);
    }

    /**
     * Generates and displays a detailed report on all projects in the system.
     * Includes project details, flat types, application statistics, and assigned officers.
     */
    private void generateProjectReport() {
        System.out.println("\n===== PROJECT REPORT =====");
        
        ArrayList<Project> projects = managerController.getAllProjects();
        
        if (projects.isEmpty()) {
            System.out.println("No projects to report.");
            return;
        }
        
        System.out.println("\nProjects and their applications:");
        for (Project project : projects) {
            System.out.println("\n====== " + project.getProjectName() + " ======");
            System.out.println("Location: " + project.getNeighborhood());
            System.out.println("Application Period: " + project.getApplicationStartDate() + " to " + project.getApplicationEndDate());
            System.out.println("Manager: " + (project.getManager() != null ? project.getManager().getName() : "None"));
            System.out.println("Visibility: " + project.getVisibility());
            
            // Show flat types
            System.out.println("\nFlat Types:");
            for (FlatType flatType : project.getFlatTypes()) {
                System.out.println("  " + flatType.getType() + ": " + 
                    flatType.getAvailableUnits() + "/" + flatType.getNumUnits() + 
                    " units available, Price: $" + flatType.getSellingPrice());
            }
            
            // Show application statistics
            // Use the correct method to get applications by project
            ArrayList<BTOApplication> applications = BTOApplication.findApplicationsByProject(project);
            int pendingCount = 0, successfulCount = 0, unsuccessfulCount = 0, bookedCount = 0;
            
            for (BTOApplication app : applications) {
                switch (app.getStatus()) {
                    case PENDING:
                        pendingCount++;
                        break;
                    case SUCCESSFUL:
                        successfulCount++;
                        break;
                    case UNSUCCESSFUL:
                        unsuccessfulCount++;
                        break;
                    case BOOKED:
                        bookedCount++;
                        break;
                }
            }
            
            System.out.println("\nApplication Statistics:");
            System.out.println("  Total Applications: " + applications.size());
            System.out.println("  Pending: " + pendingCount);
            System.out.println("  Successful: " + successfulCount);
            System.out.println("  Unsuccessful: " + unsuccessfulCount);
            System.out.println("  Booked: " + bookedCount);
            
            // Show assigned officers
            System.out.println("\nAssigned Officers:");
            if (project.getAssignedOfficers().isEmpty()) {
                System.out.println("  No officers assigned.");
            } else {
                for (Officer officer : project.getAssignedOfficers()) {
                    // Show all projects the officer is assigned to
                    StringBuilder otherProjects = new StringBuilder();
                    for (Project p : officer.getAssignedProjects()) {
                        if (!p.equals(project)) {
                            if (otherProjects.length() > 0) {
                                otherProjects.append(", ");
                            }
                            otherProjects.append(p.getProjectName());
                        }
                    }
                    
                    if (otherProjects.length() > 0) {
                        System.out.println("  - " + officer.getName() + " (NRIC: " + officer.getNric() + 
                                ") - Also assigned to: " + otherProjects);
                    } else {
                        System.out.println("  - " + officer.getName() + " (NRIC: " + officer.getNric() + ")");
                    }
                }
            }
        }
    }

    /**
     * Allows the manager to generate filtered reports based on specific criteria.
     * The manager can filter by flat type, project name, applicant age, and marital status.
     */
    private void generateReportsWithFilter() {
        System.out.println("\n===== GENERATE REPORTS WITH FILTER =====");
        
        // Create booking filter
        BookingFilter filter = new BookingFilter();
        boolean hasFilter = false;
        
        // Get flat type filter
        System.out.println("Enter flat types to include (comma-separated, leave blank for all):");
        System.out.println("Available types: TWO_ROOM, THREE_ROOM");
        String flatTypeInput = scanner.nextLine().trim();
        
        if (!flatTypeInput.isEmpty()) {
            String[] flatTypeStrings = flatTypeInput.split(",");
            ArrayList<FlatTypeEnum> flatTypes = new ArrayList<>();
            
            for (String typeStr : flatTypeStrings) {
                try {
                    FlatTypeEnum type = FlatTypeEnum.valueOf(typeStr.trim().toUpperCase());
                    flatTypes.add(type);
                } catch (IllegalArgumentException e) {
                    System.out.println("Warning: Invalid flat type '" + typeStr.trim() + "' ignored.");
                }
            }
            
            if (!flatTypes.isEmpty()) {
                filter.setFlatType(flatTypes);
                hasFilter = true;
            }
        }
        
        // Get project name filter
        System.out.print("Enter project name (leave blank for all): ");
        String projectName = scanner.nextLine().trim();
        
        if (!projectName.isEmpty()) {
            filter.setProjectName(projectName);
            hasFilter = true;
        }
        
        // Get age filter
        System.out.print("Enter age range (comma-separated, leave blank for all): ");
        String ageInput = scanner.nextLine().trim();
        
        if (!ageInput.isEmpty()) {
            String[] ageStrings = ageInput.split(",");
            ArrayList<Integer> ages = new ArrayList<>();
            
            for (String ageStr : ageStrings) {
                try {
                    int age = Integer.parseInt(ageStr.trim());
                    ages.add(age);
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid age '" + ageStr.trim() + "' ignored.");
                }
            }
            
            if (!ages.isEmpty()) {
                filter.setAge(ages);
                hasFilter = true;
            }
        }
        
        // Get marital status filter
        System.out.println("Enter marital status (SINGLE, MARRIED, both comma-separated, or leave blank for all): ");
        String maritalInput = scanner.nextLine().trim().toUpperCase();
        
        if (!maritalInput.isEmpty()) {
            String[] statusStrings = maritalInput.split(",");
            ArrayList<MarriageStatusEnum> statuses = new ArrayList<>();
            
            for (String statusStr : statusStrings) {
                try {
                    MarriageStatusEnum status = MarriageStatusEnum.valueOf(statusStr.trim());
                    statuses.add(status);
                } catch (IllegalArgumentException e) {
                    System.out.println("Warning: Invalid marital status '" + statusStr.trim() + "' ignored.");
                }
            }
            
            if (!statuses.isEmpty()) {
                filter.setMaritalStatus(statuses);
                hasFilter = true;
            }
        }
        
        // Generate report with filter
        if (!hasFilter) {
            System.out.println("No filters provided. Generating complete report.");
            generateApplicantReport();
        } else {
            // Use the controller's method to get filtered applicants
            ArrayList<Applicant> filteredApplicants = managerController.generateApplicantReportWithFilter(filter);
            
            if (filteredApplicants.isEmpty()) {
                System.out.println("No applicants match the filter criteria.");
            } else {
                System.out.println("\n===== FILTERED APPLICANT REPORT =====");
                displayApplicantReport(filteredApplicants);
            }
        }
    }

    /**
     * Displays a formatted list of applicants with their details.
     * Shows applicant's personal information, marital status, and applications.
     * 
     * @param applicants ArrayList of Applicant objects to display in the report
     */
    private void displayApplicantReport(ArrayList<Applicant> applicants) {
        System.out.println("\nTotal Applicants: " + applicants.size());
        System.out.println("\n---------------------------------------------------------------------------");
        System.out.printf("%-12s | %-20s | %-5s | %-12s | %-20s | %-10s\n", 
                "NRIC", "Name", "Age", "Status", "Project", "Flat Type");
        System.out.println("---------------------------------------------------------------------------");
        
        for (Applicant applicant : applicants) {
            // Get applications for this applicant
            ArrayList<BTOApplication> applications = applicant.getApplications();
            
            if (applications == null || applications.isEmpty()) {
                // Display applicant with no applications
                System.out.printf("%-12s | %-20s | %-5d | %-12s | %-20s | %-10s\n", 
                        applicant.getNric(), 
                        applicant.getName(), 
                        applicant.getAge(), 
                        applicant.getMaritalStatus(),
                        "No application",
                        "N/A");
            } else {
                // Display applicant with each application
                for (BTOApplication app : applications) {
                    String projectName = app.getProject() != null ? app.getProject().getProjectName() : "Unknown";
                    
                    System.out.printf("%-12s | %-20s | %-5d | %-12s | %-20s | %-10s\n", 
                            applicant.getNric(), 
                            applicant.getName(), 
                            applicant.getAge(), 
                            applicant.getMaritalStatus(),
                            projectName,
                            app.getFlatType());
                    
                    System.out.println("  Application Status: " + app.getStatus() + 
                            ", Withdrawal Status: " + app.getWithdrawalStatus() + 
                            ", Date: " + app.getApplicationDate());
                }
            }
            
            System.out.println("---------------------------------------------------------------------------");
        }
    }

    /**
     * Sets the current manager for this view.
     * 
     * @param currentManager The Manager entity to set as the current user
     */
    public void setCurrentManager(Manager currentManager) {
        this.currentManager = currentManager;
    }

    /**
     * Sets the manager controller for this view.
     * 
     * @param managerController The ManagerController to handle business logic
     */
    public void setManagerController(ManagerController managerController) {
        this.managerController = managerController;
    }

    /**
     * Gets the current manager entity.
     * 
     * @return The Manager entity who is currently using this view
     */
    public Manager getCurrentManager() {
        return this.currentManager;
    }

    /**
     * Gets the current manager controller.
     * 
     * @return The ManagerController used by this view
     */
    public ManagerController getManagerController() {
        return this.managerController;
    }

    /**
     * Default constructor that initializes the view with a new scanner and controller.
     * Does not set a current manager - this must be set separately before use.
     */
    public ManagerReportsView() {
        this.scanner = new Scanner(System.in);
        this.managerController = new ManagerController();
    }
}