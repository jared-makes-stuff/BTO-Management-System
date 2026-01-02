package boundary.officerview;

import controller.*;
import entity.*;
import enums.*;
import interfaces.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Boundary class for officer registration functionality.
 * This view provides a user interface for new officers to register in the system.
 * It handles user input validation and coordinates with the controller to create
 * new officer accounts.
 * 
 * @implements IDisplayable
 */
public class OfficerRegistrationView implements IDisplayable {

	private Scanner scanner;
	private Officer currentOfficer;
	private OfficerController officerController;

	/**
	 * Constructor with parameters to initialize the view with a specific officer.
	 * 
	 * @param officer The Officer entity whose account is being managed
	 */
	public OfficerRegistrationView(Officer officer) {
	    this.scanner = new Scanner(System.in);
	    this.officerController = new OfficerController();
	    this.currentOfficer = officer;
	}

	/**
	 * Displays the officer registration form and processes user input.
	 * Guides the user through entering their personal information and creating
	 * a new officer account in the system.
	 */
	@Override
	public void display() {
		boolean exit = false;
		
		while (!exit) {
			System.out.println("\n===== PROJECT REGISTRATION =====");
			System.out.println("1. View Available Projects");
			System.out.println("2. Register for Project");
			System.out.println("3. View My Applications");
			System.out.println("0. Return to Officer Menu");
			System.out.print("Enter your choice: ");
			
			String input = scanner.nextLine();
			exit = getUserInput(input);
		}
	}

	/**
	 * 
	 * @param input
	 */
	private boolean getUserInput(String input) {
		switch (input) {
			case "1":
				viewAvailableProjects();
				return false;
			case "2":
				registerForProject();
				return false;
			case "3":
				viewMyApplications();
				return false;
			case "0":
				return true;
			default:
				System.out.println("Invalid choice. Please try again.");
				return false;
		}
	}

	private void viewAvailableProjects() {
		System.out.println("\n===== AVAILABLE PROJECTS =====");
		ArrayList<Project> availableProjects = officerController.getAllVisibleProjects();
		
		if (availableProjects.isEmpty()) {
			System.out.println("No available projects at this time.");
			return;
		}
		
		System.out.println("\n------------------------------------------------------");
		System.out.printf("%-20s | %-15s | %-10s | %-10s\n", 
				"Project Name", "Neighborhood", "Officer Slots", "Available Slots");
		System.out.println("------------------------------------------------------");
		
		for (Project project : availableProjects) {
			int usedSlots = project.getAssignedOfficers().size();
			int availableSlots = project.getOfficerSlots() - usedSlots;
			
			// Only show projects with available slots
			if (availableSlots > 0) {
				System.out.printf("%-20s | %-15s | %-10d | %-10d\n", 
						project.getProjectName(), 
						project.getNeighborhood(), 
						project.getOfficerSlots(),
						availableSlots);
			}
		}
		
		System.out.println("------------------------------------------------------");
	}

	private void registerForProject() {
		System.out.println("\n===== REGISTER FOR PROJECT =====");
		
		// Check if officer already has projects
		ArrayList<Project> assignedProjects = currentOfficer.getAssignedProjects();
		
		// Check if officer has pending applications
		ArrayList<OfficerApplication> pendingApplications = currentOfficer.getOfficerApplications();
		if (!pendingApplications.isEmpty()) {
			System.out.println("You already have pending application(s):");
			for (OfficerApplication app : pendingApplications) {
				if (app.getStatus() == OfficerApplicationStatusEnum.PENDING) {
					System.out.println("- " + app.getProject().getProjectName() + 
							" (Applied on: " + app.getApplicationDate() + ")");
				}
			}
			System.out.println("Please wait for these to be processed before applying for more projects.");
			return;
		}
		
		// Get available projects
		ArrayList<Project> eligibleProjects = officerController.getEligibleProjectForRegistration(currentOfficer);

		if (eligibleProjects.isEmpty()) {
			System.out.println("No eligible projects available for registration at this time.");
			return;
		}
		
		// Display eligible projects
		System.out.println("\nEligible Projects:");
		for (int i = 0; i < eligibleProjects.size(); i++) {
			Project project = eligibleProjects.get(i);
			int usedSlots = project.getAssignedOfficers().size();
			int availableSlots = project.getOfficerSlots() - usedSlots;
			
			System.out.println((i+1) + ". " + project.getProjectName() + 
					" (" + project.getNeighborhood() + ") - " + 
					availableSlots + " slots available");
		}
		
		// Get project selection
		System.out.print("\nEnter the number of the project to register for (or 0 to cancel): ");
		String input = scanner.nextLine().trim();
		
		if (input.equals("0")) {
			return;
		}
		
		int selection;
		try {
			selection = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			System.out.println("Invalid number. Registration cancelled.");
			return;
		}
		
		if (selection < 1 || selection > eligibleProjects.size()) {
			System.out.println("Invalid selection. Registration cancelled.");
			return;
		}
		
		Project selectedProject = eligibleProjects.get(selection - 1);
		
		// Confirm registration
		System.out.print("\nConfirm registration for " + selectedProject.getProjectName() + "? (Y/N): ");
		String confirm = scanner.nextLine().trim().toUpperCase();
		
		if (!confirm.equals("Y")) {
			System.out.println("Registration cancelled.");
			return;
		}
		
		// Create officer application
		OfficerApplication application = officerController.createOfficerApplication(currentOfficer, selectedProject);
		
		if (application != null) {
			System.out.println("Registration application submitted successfully!");
			System.out.println("Your application will be reviewed by the project manager.");
		} else {
			System.out.println("Failed to submit registration. Please try again later.");
		}
	}

	private void viewMyApplications() {
		System.out.println("\n===== MY PROJECT APPLICATIONS =====");
		
		ArrayList<OfficerApplication> applications = currentOfficer.getOfficerApplications();
		
		if (applications.isEmpty()) {
			System.out.println("You don't have any project applications yet.");
			return;
		}
		
		System.out.println("\n------------------------------------------------------");
		System.out.printf("%-20s | %-15s | %-10s | %-15s\n", 
				"Project Name", "Neighborhood", "Status", "Application Date");
		System.out.println("------------------------------------------------------");
		
		for (OfficerApplication app : applications) {
			System.out.printf("%-20s | %-15s | %-10s | %-15s\n", 
					app.getProject().getProjectName(), 
					app.getProject().getNeighborhood(), 
					app.getStatus(), 
					app.getApplicationDate());
		}
		
		System.out.println("------------------------------------------------------");
	}

	/**
	 * 
	 * @param currentOfficer
	 */
	public void setCurrentOfficer(Officer currentOfficer) {
		this.currentOfficer = currentOfficer;
	}

	/**
	 * 
	 * @param officerController
	 */
	public void setOfficerController(OfficerController officerController) {
		this.officerController = officerController;
	}

	public Officer getCurrentOfficer() {
		return this.currentOfficer;
	}

	public OfficerController getOfficerController() {
		return this.officerController;
	}
}