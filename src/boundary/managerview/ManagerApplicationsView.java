package boundary.managerview;

import controller.*;
import entity.*;
import enums.*;
import interfaces.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Boundary class for handling and displaying BTO application management operations for managers.
 * This view allows managers to view all applications for their managed projects,
 * process BTO applications (approve/reject), and handle withdrawal requests.
 * 
 * @implements IDisplayable
 */
public class ManagerApplicationsView implements IDisplayable {

    private Scanner scanner;
    private Manager currentManager;
    private ManagerController managerController;

    /**
     * Constructor that initializes the view with a specified manager
     * 
     * @param manager The Manager entity who will be using this view
     */
    public ManagerApplicationsView(Manager manager) {
        this.scanner = new Scanner(System.in);
        this.managerController = new ManagerController();
        this.currentManager = manager;
    }

    /**
     * Displays the main menu for application management and handles user interaction.
     * Provides options to view all applications, process BTO applications,
     * handle withdrawal requests, or return to the main manager menu.
     */
    @Override
    public void display() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n===== PROCESS APPLICATIONS =====");
            System.out.println("1. View All Applications");
            System.out.println("2. Approve/Reject BTO Applications");
            System.out.println("3. Approve/Reject Withdrawal Requests");
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
                viewAllApplications();
                return false;
            case "2":
                processApplications();
                return false;
            case "3":
                processWithdrawalRequests();
                return false;
            case "0":
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
    }

    /**
     * Displays all applications for projects managed by the current manager.
     * Groups applications by project and shows application details.
     * If no applications are found, appropriate messages are displayed.
     */
    private void viewAllApplications() {
        System.out.println("\n===== ALL APPLICATIONS =====");
        
        // Get all managed projects
        ArrayList<Project> managedProjects = managerController.getManagedProjects(currentManager);
        
        if (managedProjects.isEmpty()) {
            System.out.println("You are not managing any projects.");
            return;
        }
        
        boolean foundApplications = false;
        
        // Display applications for each project
        for (Project project : managedProjects) {
            ArrayList<BTOApplication> applications = managerController.getApplicationsByProject(project);
            
            if (!applications.isEmpty()) {
                foundApplications = true;
                
                System.out.println("\n===== APPLICATIONS FOR " + project.getProjectName().toUpperCase() + " =====");
                displayApplications(applications);
            }
        }
        
        if (!foundApplications) {
            System.out.println("No applications found for your managed projects.");
        }
    }

    /**
     * Allows the manager to process (approve/reject) pending BTO applications.
     * The manager selects a project, then an application to process.
     * Displays relevant information like eligibility and unit availability
     * to help the manager make informed decisions.
     */
    private void processApplications() {
        System.out.println("\n===== PROCESS BTO APPLICATIONS =====");
        
        // Get all managed projects
        ArrayList<Project> managedProjects = managerController.getManagedProjects(currentManager);
        
        if (managedProjects.isEmpty()) {
            System.out.println("You are not managing any projects.");
            return;
        }
        
        // Display projects
        System.out.println("\nSelect Project:");
        for (int i = 0; i < managedProjects.size(); i++) {
            Project project = managedProjects.get(i);
            System.out.println((i + 1) + ". " + project.getProjectName() + " (" + project.getNeighborhood() + ")");
        }
        
        System.out.print("Enter project number (or 0 to cancel): ");
        int projectNumber;
        try {
            projectNumber = Integer.parseInt(scanner.nextLine().trim());
            if (projectNumber == 0) {
                return;
            }
            if (projectNumber < 1 || projectNumber > managedProjects.size()) {
                System.out.println("Invalid project number. Operation cancelled.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Operation cancelled.");
            return;
        }
        
        Project selectedProject = managedProjects.get(projectNumber - 1);
        
        // Get pending applications for the selected project
        ArrayList<BTOApplication> pendingApplications = new ArrayList<>();
        ArrayList<BTOApplication> allApplications = managerController.getApplicationsByProject(selectedProject);
        
        for (BTOApplication app : allApplications) {
            if (app.getStatus() == BTOApplicationStatusEnum.PENDING && 
                    app.getWithdrawalStatus() == WithdrawalStatusEnum.NA) {
                pendingApplications.add(app);
            }
        }
        
        if (pendingApplications.isEmpty()) {
            System.out.println("No pending applications found for this project.");
            return;
        }
        
        // Display pending applications
        System.out.println("\n===== PENDING APPLICATIONS =====");
        for (int i = 0; i < pendingApplications.size(); i++) {
            BTOApplication app = pendingApplications.get(i);
            System.out.println((i + 1) + ". Applicant: " + app.getApplicant().getName() + 
                    " (NRIC: " + app.getApplicant().getNric() + ")");
            System.out.println("   Age: " + app.getApplicant().getAge() + 
                    ", Marital Status: " + app.getApplicant().getMaritalStatus());
            System.out.println("   Flat Type: " + app.getFlatType() + 
                    ", Application Date: " + app.getApplicationDate());
            System.out.println("   Status: " + app.getStatus());
        }
        
        // Select application to process
        System.out.print("\nEnter application number to process (or 0 to cancel): ");
        int appNumber;
        try {
            appNumber = Integer.parseInt(scanner.nextLine().trim());
            if (appNumber == 0) {
                return;
            }
            if (appNumber < 1 || appNumber > pendingApplications.size()) {
                System.out.println("Invalid application number. Operation cancelled.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Operation cancelled.");
            return;
        }
        
        BTOApplication selectedApplication = pendingApplications.get(appNumber - 1);
        
        // Check flat type availability
        FlatTypeEnum requestedFlatType = selectedApplication.getFlatType();
        FlatType flatType = null;
        
        for (FlatType ft : selectedProject.getFlatTypes()) {
            if (ft.getType() == requestedFlatType) {
                flatType = ft;
                break;
            }
        }
        
        if (flatType == null) {
            System.out.println("Error: The requested flat type does not exist in this project.");
            return;
        }
        
        boolean isAvailable = flatType.getAvailableUnits() > 0;
        
        // Check eligibility
        Applicant applicant = selectedApplication.getApplicant();
        boolean isEligible = ApplicantController.isEligibleForProject(applicant, selectedProject) && 
                ApplicantController.isEligibleForFlatType(applicant, requestedFlatType);
        
        System.out.println("\nApplication Details:");
        System.out.println("Applicant: " + applicant.getName() + " (NRIC: " + applicant.getNric() + ")");
        System.out.println("Age: " + applicant.getAge() + ", Marital Status: " + applicant.getMaritalStatus());
        System.out.println("Requested Flat Type: " + requestedFlatType);
        System.out.println("Eligibility Check: " + (isEligible ? "ELIGIBLE" : "NOT ELIGIBLE"));
        System.out.println("Flat Type Availability: " + (isAvailable ? 
                flatType.getAvailableUnits() + " units available" : "NO UNITS AVAILABLE"));
        
        // Get manager decision
        System.out.println("\nProcess Application:");
        System.out.println("1. Approve Application");
        System.out.println("2. Reject Application");
        System.out.println("0. Cancel");
        System.out.print("Enter your choice: ");
        
        String decision = scanner.nextLine().trim();
        
        if (decision.equals("0")) {
            return;
        }
        
        boolean approve = decision.equals("1");
        
        if (approve && !isEligible) {
            System.out.println("Warning: Applicant is not eligible for this flat type.");
            System.out.print("Are you sure you want to approve? (Y/N): ");
            String confirm = scanner.nextLine().trim().toUpperCase();
            if (!confirm.equals("Y")) {
                System.out.println("Operation cancelled.");
                return;
            }
        }
        
        if (approve && !isAvailable) {
            System.out.println("Warning: No units available for this flat type.");
            System.out.print("Are you sure you want to approve? (Y/N): ");
            String confirm = scanner.nextLine().trim().toUpperCase();
            if (!confirm.equals("Y")) {
                System.out.println("Operation cancelled.");
                return;
            }
        }
        
        // Process application
        boolean processed = managerController.processApplication(selectedApplication, approve);
        
        if (processed) {
            System.out.println("Application " + (approve ? "approved" : "unsuccessful") + " successfully.");
        } else {
            System.out.println("Failed to process application. Please try again.");
        }
    }

    /**
     * Allows the manager to process (approve/reject) pending withdrawal requests.
     * Displays all withdrawal requests for the manager's projects,
     * and lets the manager select one to process.
     */
    private void processWithdrawalRequests() {
        System.out.println("\n===== PROCESS WITHDRAWAL REQUESTS =====");
        
        // Get all managed projects
        ArrayList<Project> managedProjects = managerController.getManagedProjects(currentManager);
        
        if (managedProjects.isEmpty()) {
            System.out.println("You are not managing any projects.");
            return;
        }
        
        // Collect all pending withdrawal requests
        ArrayList<BTOApplication> pendingWithdrawals = new ArrayList<>();
        
        for (Project project : managedProjects) {
            ArrayList<BTOApplication> applications = managerController.getApplicationsByProject(project);
            
            for (BTOApplication app : applications) {
                if (app.getWithdrawalStatus() == WithdrawalStatusEnum.PENDING) {
                    pendingWithdrawals.add(app);
                }
            }
        }
        
        if (pendingWithdrawals.isEmpty()) {
            System.out.println("No pending withdrawal requests found.");
            return;
        }
        
        // Display pending withdrawal requests
        System.out.println("\n===== PENDING WITHDRAWAL REQUESTS =====");
        for (int i = 0; i < pendingWithdrawals.size(); i++) {
            BTOApplication app = pendingWithdrawals.get(i);
            System.out.println((i + 1) + ". Applicant: " + app.getApplicant().getName() + 
                    " (NRIC: " + app.getApplicant().getNric() + ")");
            System.out.println("   Project: " + app.getProject().getProjectName() + 
                    ", Flat Type: " + app.getFlatType());
            System.out.println("   Application Status: " + app.getStatus() + 
                    ", Withdrawal Status: " + app.getWithdrawalStatus());
        }
        
        // Select request to process
        System.out.print("\nEnter request number to process (or 0 to cancel): ");
        int requestNumber;
        try {
            requestNumber = Integer.parseInt(scanner.nextLine().trim());
            if (requestNumber == 0) {
                return;
            }
            if (requestNumber < 1 || requestNumber > pendingWithdrawals.size()) {
                System.out.println("Invalid request number. Operation cancelled.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Operation cancelled.");
            return;
        }
        
        BTOApplication selectedApplication = pendingWithdrawals.get(requestNumber - 1);
        
        // Display details
        System.out.println("\nWithdrawal Request Details:");
        System.out.println("Applicant: " + selectedApplication.getApplicant().getName() + 
                " (NRIC: " + selectedApplication.getApplicant().getNric() + ")");
        System.out.println("Project: " + selectedApplication.getProject().getProjectName());
        System.out.println("Flat Type: " + selectedApplication.getFlatType());
        System.out.println("Current Status: " + selectedApplication.getStatus());
        
        // Get manager decision
        System.out.println("\nProcess Withdrawal Request:");
        System.out.println("1. Approve Withdrawal");
        System.out.println("2. Reject Withdrawal");
        System.out.println("0. Cancel");
        System.out.print("Enter your choice: ");
        
        String decision = scanner.nextLine().trim();
        
        if (decision.equals("0")) {
            return;
        }
        
        boolean approve = decision.equals("1");
        
        // Process withdrawal
        boolean processed = managerController.processWithdrawal(selectedApplication, approve);
        
        if (processed) {
            System.out.println("Withdrawal request " + (approve ? "approved" : "unsuccessful") + " successfully.");
        } else {
            System.out.println("Failed to process withdrawal request. Please try again.");
        }
    }

    /**
     * Displays a formatted list of applications with summary statistics.
     * Shows counts of applications by status and withdrawal status,
     * followed by a table with details of each application.
     * 
     * @param applications ArrayList of BTOApplication objects to display
     */
    private void displayApplications(ArrayList<BTOApplication> applications) {
        System.out.println("Total Applications: " + applications.size());
        
        // Summarize by status
        int pendingCount = 0, successfulCount = 0, unsuccessfulCount = 0, bookedCount = 0;
        int pendingWithdrawalCount = 0, approvedWithdrawalCount = 0, rejectedWithdrawalCount = 0;
        
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
            
            switch (app.getWithdrawalStatus()) {
                case PENDING:
                    pendingWithdrawalCount++;
                    break;
                case APPROVED:
                    approvedWithdrawalCount++;
                    break;
                case REJECTED:
                    rejectedWithdrawalCount++;
                    break;
                case NA:
                    // No action needed for NA status
                    break;
            }
        }
        
        System.out.println("\nApplication Status Summary:");
        System.out.println("Pending: " + pendingCount);
        System.out.println("Successful: " + successfulCount);
        System.out.println("Unsuccessful: " + unsuccessfulCount);
        System.out.println("Booked: " + bookedCount);
        
        if (pendingWithdrawalCount > 0 || approvedWithdrawalCount > 0 || rejectedWithdrawalCount > 0) {
            System.out.println("\nWithdrawal Status Summary:");
            System.out.println("Pending Withdrawals: " + pendingWithdrawalCount);
            System.out.println("Approved Withdrawals: " + approvedWithdrawalCount);
            System.out.println("Rejected Withdrawals: " + rejectedWithdrawalCount);
        }
        
        System.out.println("\n----------------------------------------------------------------");
        System.out.printf("%-5s | %-20s | %-12s | %-10s | %-10s | %-15s\n", 
                "ID", "Applicant", "NRIC", "Flat Type", "Status", "Withdrawal");
        System.out.println("----------------------------------------------------------------");
        
        int i = 1;
        for (BTOApplication app : applications) {
            System.out.printf("%-5d | %-20s | %-12s | %-10s | %-10s | %-15s\n", 
                    i++,
                    app.getApplicant().getName(),
                    app.getApplicant().getNric(),
                    app.getFlatType(),
                    app.getStatus(),
                    app.getWithdrawalStatus() == WithdrawalStatusEnum.NA ? "N/A" : app.getWithdrawalStatus());
        }
        
        System.out.println("----------------------------------------------------------------");
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
    public ManagerApplicationsView() {
        this.scanner = new Scanner(System.in);
        this.managerController = new ManagerController();
    }
}