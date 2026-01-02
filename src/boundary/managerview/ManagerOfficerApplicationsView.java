package boundary.managerview;

import controller.*;
import entity.*;
import enums.OfficerApplicationStatusEnum;
import interfaces.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Boundary class for handling and displaying officer application management functionality for managers.
 * This view allows managers to view and process applications from officers
 * who wish to be assigned to BTO projects managed by the current manager.
 * 
 * @implements IDisplayable
 */
public class ManagerOfficerApplicationsView implements IDisplayable {

	private Scanner scanner;
	private Manager currentManager;
	/**
	 * Instance variables
	 */
	private ManagerController managerController;

	/**
	 * Constructor that initializes the view with a specified manager
	 * 
	 * @param manager The Manager entity who will be using this view
	 */
	public ManagerOfficerApplicationsView(Manager manager) {
        this.scanner = new Scanner(System.in);
        this.managerController = new ManagerController();
        this.currentManager = manager;
	}

	/**
	 * Displays the main menu for officer application management and handles user interaction.
	 * Provides options to view all applications, process applications,
	 * or return to the main manager menu.
	 */
	@Override
	public void display() {
		boolean exit = false;
		
		while (!exit) {
			System.out.println("\n===== MANAGE OFFICER APPLICATIONS =====");
			System.out.println("1. View All Applications");
			System.out.println("2. Process Officer Applications");
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
				processOfficerApplications();
				return false;
			case "0":
				return true;
			default:
				System.out.println("Invalid choice. Please try again.");
				return false;
		}
	}

	/**
	 * Displays all officer applications for projects managed by the current manager.
	 * If no applications are found, an appropriate message is displayed.
	 */
	private void viewAllApplications() {
		System.out.println("\n===== ALL OFFICER APPLICATIONS =====");
		
		// Get all projects managed by this manager
		ArrayList<Project> managedProjects = managerController.getManagedProjects(currentManager);
		
		if (managedProjects.isEmpty()) {
			System.out.println("You are not managing any projects.");
			return;
		}
		
		// Get officer applications for each project
		ArrayList<OfficerApplication> allOfficerApplications = new ArrayList<>();
		for (Project project : managedProjects) {
			ArrayList<OfficerApplication> projectApplications = managerController.getOfficerApplicationsByProject(project);
			allOfficerApplications.addAll(projectApplications);
		}
		
		if (allOfficerApplications.isEmpty()) {
			System.out.println("No officer applications found for your managed projects.");
		} else {
			displayOfficerApplications(allOfficerApplications);
		}
	}

	/**
	 * Allows the manager to process (approve/reject) pending officer applications.
	 * The manager selects an application to process, reviews the officer's details
	 * including other project assignments, and makes a decision.
	 */
	private void processOfficerApplications() {
		System.out.println("\n===== PROCESS OFFICER APPLICATIONS =====");
		
		// Get all projects managed by this manager
		ArrayList<Project> managedProjects = managerController.getManagedProjects(currentManager);
		
		if (managedProjects.isEmpty()) {
			System.out.println("You are not managing any projects.");
			return;
		}
		
		// Get pending officer applications for each project
		ArrayList<OfficerApplication> pendingApplications = new ArrayList<>();
		for (Project project : managedProjects) {
			ArrayList<OfficerApplication> projectApplications = managerController.getOfficerApplicationsByProject(project);
			for (OfficerApplication app : projectApplications) {
				if (app.getStatus() == OfficerApplicationStatusEnum.PENDING) {
					pendingApplications.add(app);
				}
			}
		}
		
		if (pendingApplications.isEmpty()) {
			System.out.println("No pending officer applications to process.");
			return;
		}
		
		// Display pending applications with index numbers
		System.out.println("\nPending Officer Applications:");
		displayOfficerApplicationsWithIndex(pendingApplications);
		
		// Select application to process by index
		System.out.print("\nEnter application number to process (or 0 to cancel): ");
		int appIndex;
		try {
			appIndex = Integer.parseInt(scanner.nextLine().trim());
			if (appIndex == 0) {
				return;
			}
			if (appIndex < 1 || appIndex > pendingApplications.size()) {
				System.out.println("Invalid selection.");
				return;
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input.");
			return;
		}
		
		// Get the selected application
		OfficerApplication selectedApplication = pendingApplications.get(appIndex - 1);
		
		 // Additional check to display if officer already has projects
		Officer applicantOfficer = selectedApplication.getOfficer();
		ArrayList<Project> officerProjects = applicantOfficer.getAssignedProjects();
		
		if (!officerProjects.isEmpty()) {
			System.out.println("\nNote: This officer is already assigned to the following projects:");
			for (Project p : officerProjects) {
				System.out.println("- " + p.getProjectName());
			}
		}
		
		// Process the application
		System.out.println("\nProcessing Officer Application");
		System.out.println("Officer: " + selectedApplication.getOfficer().getName());
		System.out.println("Project: " + selectedApplication.getProject().getProjectName());
		System.out.println("Date: " + selectedApplication.getApplicationDate());
		System.out.println("\n1. Approve Application");
		System.out.println("2. Reject Application");
		System.out.print("Enter your choice: ");
		
		String choice = scanner.nextLine().trim();
		boolean success = false;
		
		switch (choice) {
			case "1":
				// Check if project has available slots
				Project project = selectedApplication.getProject();
				if (project.getAssignedOfficers().size() >= project.getOfficerSlots()) {
					System.out.println("Project has no available officer slots. Cannot approve application.");
					return;
				}
				
				// Approve application
				success = managerController.processOfficerApplication(selectedApplication, true);
				
				if (success) {
					System.out.println("Officer application approved successfully!");
				} else {
					System.out.println("Failed to approve officer application. Please try again.");
				}
				break;
				
			case "2":
				// Reject application
				success = managerController.processOfficerApplication(selectedApplication, false);
				
				if (success) {
					System.out.println("Officer application rejected successfully!");
				} else {
					System.out.println("Failed to reject officer application. Please try again.");
				}
				break;
				
			default:
				System.out.println("Invalid choice.");
				break;
		}
	}

	/**
	 * Display officer applications without index numbers.
	 * Shows project, status, officer name, and application date in a formatted table.
	 * 
	 * @param officerApplications ArrayList of OfficerApplication objects to display
	 */
	private void displayOfficerApplications(ArrayList<OfficerApplication> officerApplications) {
		if (officerApplications.isEmpty()) {
			System.out.println("No officer applications to display.");
			return;
		}
		
		System.out.println("\n---------------------------------------------------------------------");
		System.out.printf("%-20s | %-12s | %-20s | %-10s\n", 
				"Project", "Status", "Officer", "Date");
		System.out.println("---------------------------------------------------------------------");
		
		for (OfficerApplication app : officerApplications) {
			Project project = app.getProject();
			Officer officer = app.getOfficer();
			
			System.out.printf("%-20s | %-12s | %-20s | %s\n", 
					project.getProjectName(),
					app.getStatus(),
					officer.getName(),
					app.getApplicationDate());
		}
		
		System.out.println("---------------------------------------------------------------------");
	}
	
	/**
	 * Display officer applications with index numbers.
	 * Similar to displayOfficerApplications but includes an index number for selection.
	 * Used when processing applications to allow the manager to select by number.
	 * 
	 * @param officerApplications ArrayList of OfficerApplication objects to display
	 */
	private void displayOfficerApplicationsWithIndex(ArrayList<OfficerApplication> officerApplications) {
		if (officerApplications.isEmpty()) {
			System.out.println("No officer applications to display.");
			return;
		}
		
		System.out.println("\n--------------------------------------------------------------------------");
		System.out.printf("%-5s | %-20s | %-12s | %-20s | %-10s\n", 
				"No.", "Project", "Status", "Officer", "Date");
		System.out.println("--------------------------------------------------------------------------");
		
		for (int i = 0; i < officerApplications.size(); i++) {
			OfficerApplication app = officerApplications.get(i);
			Project project = app.getProject();
			Officer officer = app.getOfficer();
			
			System.out.printf("%-5d | %-20s | %-12s | %-20s | %s\n", 
					i + 1,
					project.getProjectName(),
					app.getStatus(),
					officer.getName(),
					app.getApplicationDate());
		}
		
		System.out.println("--------------------------------------------------------------------------");
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
	public ManagerOfficerApplicationsView() {
		this.scanner = new Scanner(System.in);
		this.managerController = new ManagerController();
	}
}