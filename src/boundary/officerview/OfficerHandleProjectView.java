package boundary.officerview;

import controller.*;
import entity.*;
import enums.*;
import interfaces.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Boundary class for handling project management functionality for officers.
 * This view allows officers to manage BTO project applications, process booking requests,
 * and generate receipts for their assigned projects.
 * 
 * The class implements the IDisplayable interface to provide a consistent UI pattern
 * throughout the application and uses the OfficerController to handle business logic operations.
 * 
 * @implements IDisplayable Interface for standardized display functionality
 */
public class OfficerHandleProjectView implements IDisplayable {

	private Scanner scanner;
	private Officer currentOfficer;
	private OfficerController officerController;

	/**
	 * Constructor with parameters to initialize the view with a specific officer.
	 * 
	 * @param officer The Officer entity whose account is being managed
	 */
	public OfficerHandleProjectView(Officer officer) {
		this.scanner = new Scanner(System.in);
		this.officerController = new OfficerController();
		this.currentOfficer = officer;
	}

	/**
	 * Displays the main menu for project management and handles user interaction.
	 * Provides options to view project applications, process bookings, generate receipts,
	 * or return to the main officer menu.
	 * 
	 * This is the primary entry point for this view as required by the IDisplayable interface.
	 * The method runs in a loop until the user chooses to exit by selecting option 0.
	 */
	@Override
	public void display() {
		boolean exit = false;
		
		while (!exit) {
			System.out.println("\n===== HANDLE PROJECT =====");
			System.out.println("1. View Project Applications");
			System.out.println("2. Process Booking");
			System.out.println("3. Generate Receipts");
			System.out.println("0. Return to Officer Menu");
			System.out.print("Enter your choice: ");
			
			String input = scanner.nextLine();
			exit = getUserInput(input);
		}
	}

	/**
	 * Processes user input from the main menu and calls the appropriate method.
	 * Routes the user to the selected functionality based on their menu choice.
	 * 
	 * @param input The user's menu selection as a string
	 * @return boolean True if the user wants to exit the menu, false otherwise
	 */
	private boolean getUserInput(String input) {
		switch (input) {
			case "1":
				viewProjectApplications();
				return false;
			case "2":
				processBookingRequests();
				return false;
			case "3":
				generateReceipts();
				return false;
			case "0":
				return true;
			default:
				System.out.println("Invalid choice. Please try again.");
				return false;
		}
	}

	/**
	 * Displays applications for projects assigned to the current officer.
	 * If the officer is assigned to multiple projects, allows selection of a specific project.
	 * Displays application statistics and allows filtering by application status.
	 * 
	 * The method first checks if the officer has any assigned projects, then retrieves
	 * and displays applications for the selected project with filtering options.
	 */
	private void viewProjectApplications() {
		ArrayList<Project> assignedProjects = currentOfficer.getAssignedProjects();
		
		if (assignedProjects.isEmpty()) {
			System.out.println("You are not assigned to any project yet.");
			return;
		}
		
		// If officer has multiple projects, let them select which one to view
		Project selectedProject;
		if (assignedProjects.size() > 1) {
			System.out.println("\n===== SELECT PROJECT =====");
			for (int i = 0; i < assignedProjects.size(); i++) {
				System.out.println((i+1) + ". " + assignedProjects.get(i).getProjectName());
			}
			
			System.out.print("\nSelect a project (or 0 to cancel): ");
			String input = scanner.nextLine().trim();
			
			if (input.equals("0")) {
				return;
			}
			
			int selection;
			try {
				selection = Integer.parseInt(input);
				if (selection < 1 || selection > assignedProjects.size()) {
					System.out.println("Invalid selection.");
					return;
				}
				selectedProject = assignedProjects.get(selection - 1);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.");
				return;
			}
		} else {
			selectedProject = assignedProjects.get(0);
		}
		
		System.out.println("\n===== APPLICATIONS FOR " + selectedProject.getProjectName().toUpperCase() + " =====");
		
		ArrayList<BTOApplication> applications = officerController.getApplicationsByProject(selectedProject);
		
		if (applications.isEmpty()) {
			System.out.println("No applications found for this project.");
			return;
		}
		
		// Display application counts by status
		int pendingCount = 0, successfulCount = 0, bookedCount = 0, unsuccessfulCount = 0;
		
		for (BTOApplication app : applications) {
			switch (app.getStatus()) {
				case PENDING:
					pendingCount++;
					break;
				case SUCCESSFUL:
					successfulCount++;
					break;
				case BOOKED:
					bookedCount++;
					break;
				case UNSUCCESSFUL:
					unsuccessfulCount++;
					break;
			}
		}
		
		System.out.println("\nApplication Statistics:");
		System.out.println("Total Applications: " + applications.size());
		System.out.println("Pending: " + pendingCount);
		System.out.println("Successful: " + successfulCount);
		System.out.println("Booked: " + bookedCount);
		System.out.println("Unsuccessful: " + unsuccessfulCount);
		
		// Get filter selection
		System.out.println("\nFilter applications by status:");
		System.out.println("1. All Applications");
		System.out.println("2. Pending Applications");
		System.out.println("3. Successful Applications");
		System.out.println("4. Booked Applications");
		System.out.println("5. Unsuccessful Applications");
		System.out.print("Enter your choice: ");
		
		String filterChoice = scanner.nextLine().trim();
		ArrayList<BTOApplication> filteredApplications;
		
		switch (filterChoice) {
			case "1":
				filteredApplications = applications;
				break;
			case "2":
				filteredApplications = officerController.getApplicationsByProjectAndStatus(
						selectedProject, BTOApplicationStatusEnum.PENDING);
				break;
			case "3":
				filteredApplications = officerController.getApplicationsByProjectAndStatus(
						selectedProject, BTOApplicationStatusEnum.SUCCESSFUL);
				break;
			case "4":
				filteredApplications = officerController.getApplicationsByProjectAndStatus(
						selectedProject, BTOApplicationStatusEnum.BOOKED);
				break;
			case "5":
				filteredApplications = officerController.getApplicationsByProjectAndStatus(
						selectedProject, BTOApplicationStatusEnum.UNSUCCESSFUL);
				break;
			default:
				System.out.println("Invalid choice. Showing all applications.");
				filteredApplications = applications;
				break;
		}
		
		// Display filtered applications
		displayApplications(filteredApplications);
	}

	/**
	 * Allows the officer to generate and view receipts for their assigned projects.
	 * If the officer is assigned to multiple projects, allows selection of a specific project.
	 * Displays all receipts for the selected project.
	 * 
	 * The method first checks if the officer has any assigned projects, then retrieves
	 * and displays receipts for the selected project.
	 */
	private void generateReceipts() {
		ArrayList<Project> assignedProjects = currentOfficer.getAssignedProjects();
		
		if (assignedProjects.isEmpty()) {
			System.out.println("You are not assigned to any project yet.");
			return;
		}
		
		// If officer has multiple projects, let them select which one to view
		Project selectedProject;
		if (assignedProjects.size() > 1) {
			System.out.println("\n===== SELECT PROJECT =====");
			for (int i = 0; i < assignedProjects.size(); i++) {
				System.out.println((i+1) + ". " + assignedProjects.get(i).getProjectName());
			}
			
			System.out.print("\nSelect a project (or 0 to cancel): ");
			String input = scanner.nextLine().trim();
			
			if (input.equals("0")) {
				return;
			}
			
			int selection;
			try {
				selection = Integer.parseInt(input);
				if (selection < 1 || selection > assignedProjects.size()) {
					System.out.println("Invalid selection.");
					return;
				}
				selectedProject = assignedProjects.get(selection - 1);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.");
				return;
			}
		} else {
			selectedProject = assignedProjects.get(0);
		}
		
		System.out.println("\n===== GENERATE RECEIPTS FOR " + selectedProject.getProjectName().toUpperCase() + " =====");
		
		// Get receipts for this project
		ArrayList<Receipt> receipts = officerController.getReceiptsByProject(selectedProject);
		
		if (receipts.isEmpty()) {
			System.out.println("No receipts available for this project.");
		} else {
			System.out.println("\nReceipts for " + selectedProject.getProjectName() + ":");
			displayReceipts(receipts);
		}
	}

	/**
	 * Displays a formatted list of BTO applications with their details.
	 * Shows application ID, applicant information, flat type, status, and date
	 * in a tabular format for easier readability.
	 * 
	 * @param applications ArrayList of BTOApplication objects to display
	 */
	private void displayApplications(ArrayList<BTOApplication> applications) {
		if (applications.isEmpty()) {
			System.out.println("No applications to display.");
			return;
		}
		
		System.out.println("\n----------------------------------------------------------------------");
		System.out.printf("%-5s | %-20s | %-12s | %-15s | %-10s | %-10s\n", 
				"ID", "Applicant", "NRIC", "Flat Type", "Status", "Date");
		System.out.println("----------------------------------------------------------------------");
		
		for (BTOApplication app : applications) {
			System.out.printf("%-5d | %-20s | %-12s | %-15s | %-10s | %s\n", 
					app.getApplicationID(),
					app.getApplicant().getName(),
					app.getApplicant().getNric(),
					app.getFlatType(),
					app.getStatus(),
					app.getApplicationDate());
		}
		
		System.out.println("----------------------------------------------------------------------");
	}

	/**
	 * Displays a formatted list of receipts with their details.
	 * Shows receipt number, applicant information, flat type, and date
	 * in a tabular format for easier readability.
	 * 
	 * @param receipts ArrayList of Receipt objects to display
	 */
	private void displayReceipts(ArrayList<Receipt> receipts) {
		if (receipts.isEmpty()) {
			System.out.println("No receipts to display.");
			return;
		}
		
		System.out.println("\n----------------------------------------------------------------------");
		System.out.printf("%-15s | %-20s | %-12s | %-15s | %-10s\n", 
				"Receipt Number", "Applicant", "NRIC", "Flat Type", "Date");
		System.out.println("----------------------------------------------------------------------");
		
		for (Receipt receipt : receipts) {
			Booking booking = receipt.getBooking();
			BTOApplication app = booking.getApplication();
			
			System.out.printf("%-15s | %-20s | %-12s | %-15s | %s\n", 
					receipt.getReceiptNumber(),
					app.getApplicant().getName(),
					app.getApplicant().getNric(),
					app.getFlatType(),
					receipt.getDate());
		}
		
		System.out.println("----------------------------------------------------------------------");
	}

	/**
	 * Allows the officer to process pending booking requests for their assigned projects.
	 * If the officer is assigned to multiple projects, allows selection of a specific project.
	 * Displays pending bookings and guides the officer through processing a selected booking.
	 * 
	 * The method checks applicant eligibility, confirms the booking, processes it through
	 * the controller, and optionally generates a receipt.
	 */
	private void processBookingRequests() {
		System.out.println("\n===== PROCESS BOOKING REQUESTS =====");
		
		ArrayList<Project> assignedProjects = officerController.getAssignedProjects(currentOfficer);
		if (assignedProjects.isEmpty()) {
			System.out.println("You are not assigned to any project yet.");
			return;
		}
		
		// If officer has multiple projects, let them select which one to process
		Project selectedProject;
		if (assignedProjects.size() > 1) {
			System.out.println("\n===== SELECT PROJECT =====");
			for (int i = 0; i < assignedProjects.size(); i++) {
				System.out.println((i+1) + ". " + assignedProjects.get(i).getProjectName());
			}
			
			System.out.print("\nSelect a project (or 0 to cancel): ");
			String input = scanner.nextLine().trim();
			
			if (input.equals("0")) {
				return;
			}
			
			int selection;
			try {
				selection = Integer.parseInt(input);
				if (selection < 1 || selection > assignedProjects.size()) {
					System.out.println("Invalid selection.");
					return;
				}
				selectedProject = assignedProjects.get(selection - 1);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.");
				return;
			}
		} else {
			selectedProject = assignedProjects.get(0);
		}
		
		// Get pending bookings for the selected project
		ArrayList<Booking> pendingBookings =  officerController.findPendingBookingsByProject(selectedProject);
		
		if (pendingBookings.isEmpty()) {
			System.out.println("No pending booking requests for your project.");
			return;
		}
		
		// Display pending bookings
		System.out.println("\nPending Booking Requests:");
		System.out.println("\n------------------------------------------------------------------");
		System.out.printf("%-5s | %-15s | %-20s | %-15s | %-10s\n", 
				"No.", "Project", "Applicant", "Flat Type", "Date");
		System.out.println("------------------------------------------------------------------");
		
		for (int i = 0; i < pendingBookings.size(); i++) {
			Booking booking = pendingBookings.get(i);
			BTOApplication application = booking.getApplication();
			System.out.printf("%-5d | %-15s | %-20s | %-15s | %-10s\n", 
					(i+1), 
					application.getProject().getProjectName(), 
					application.getApplicant().getName(), 
					booking.getFlatType(), 
					booking.getBookingDateTime());
		}
		
		System.out.println("------------------------------------------------------------------");
		
		// Select booking to process
		System.out.print("\nEnter the number of the booking to process (or 0 to cancel): ");
		String input = scanner.nextLine().trim();
		
		if (input.equals("0")) {
			return;
		}
		
		int selection;
		try {
			selection = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			System.out.println("Invalid number. Operation cancelled.");
			return;
		}
		
		if (selection < 1 || selection > pendingBookings.size()) {
			System.out.println("Invalid selection. Operation cancelled.");
			return;
		}
		
		Booking selectedBooking = pendingBookings.get(selection - 1);
		
		// Confirm processing
		BTOApplication application = selectedBooking.getApplication();
		Applicant applicant = application.getApplicant();
		
		System.out.println("\nBooking Details:");
		System.out.println("Project: " + application.getProject().getProjectName());
		System.out.println("Applicant: " + applicant.getName() + " (NRIC: " + applicant.getNric() + ")");
		System.out.println("Flat Type: " + selectedBooking.getFlatType());
		System.out.println("Age: " + applicant.getAge());
		System.out.println("Marital Status: " + applicant.getMaritalStatus());
		
		// Verify eligibility again
		boolean isEligible = ApplicantController.isEligibleForFlatType(applicant, selectedBooking.getFlatType());
		
		if (!isEligible) {
			System.out.println("\nWARNING: Applicant doesn't meet eligibility criteria for this flat type!");
			System.out.println("- Singles must be 35+ years old and can only book 2-Room flats");
			System.out.println("- Married applicants must be 21+ years old");
			
			System.out.print("\nContinue with processing despite eligibility issues? (Y/N): ");
			String override = scanner.nextLine().trim().toUpperCase();
			
			if (!override.equals("Y")) {
				System.out.println("Processing cancelled.");
				return;
			}
		}
		
		// Process booking
		System.out.print("\nConfirm processing of this booking request? (Y/N): ");
		String confirm = scanner.nextLine().trim().toUpperCase();
		
		if (!confirm.equals("Y")) {
			System.out.println("Processing cancelled.");
			return;
		}
		
		boolean success = officerController.processBooking(currentOfficer, selectedBooking);
		
		if (success) {
			System.out.println("Booking processed successfully!");
			System.out.println("Do you want to generate a receipt now? (Y/N): ");
			String generateReceipt = scanner.nextLine().trim().toUpperCase();
			
			if (generateReceipt.equals("Y")) {
				Receipt receipt = officerController.generateReceipt(currentOfficer, selectedBooking);
				
				if (receipt != null) {
					System.out.println("Receipt generated successfully!");
					System.out.println("Receipt Number: " + receipt.getReceiptNumber());
				} else {
					System.out.println("Failed to generate receipt. Please try again later.");
				}
			}
		} else {
			System.out.println("Failed to process booking. Please try again later.");
		}
	}

	/**
	 * Sets the current officer for this view.
	 * Used to update the officer context when needed, for example when switching users.
	 * 
	 * @param currentOfficer The Officer entity to set as the current user
	 */
	public void setCurrentOfficer(Officer currentOfficer) {
		this.currentOfficer = currentOfficer;
	}

	/**
	 * Sets the officer controller for this view.
	 * Used to update the controller reference when needed, typically during initialization
	 * or when the controller needs to be replaced.
	 * 
	 * @param officerController The OfficerController to handle business logic
	 */
	public void setOfficerController(OfficerController officerController) {
		this.officerController = officerController;
	}

	/**
	 * Gets the current officer entity.
	 * Allows access to the officer information for other components that may need it.
	 * 
	 * @return The Officer entity who is currently using this view
	 */
	public Officer getCurrentOfficer() {
		return this.currentOfficer;
	}

	/**
	 * Gets the current officer controller.
	 * Provides access to the controller for components that need to use its methods.
	 * 
	 * @return The OfficerController used by this view
	 */
	public OfficerController getOfficerController() {
		return this.officerController;
	}
}