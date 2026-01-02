package boundary.officerview;

import controller.*;
import entity.*;
import enums.*;
import interfaces.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Boundary class for handling and displaying BTO application functionality for officers.
 * This view allows officers to view their applications, apply for new BTO projects,
 * request application withdrawals, and view their bookings.
 * 
 * The class implements the IDisplayable interface to provide a consistent UI pattern.
 * It interacts with the OfficerController to perform business logic operations.
 * 
 * @implements IDisplayable Interface for standardized display functionality
 */
public class OfficerApplicationsView implements IDisplayable {

	private Scanner scanner;
	private Officer currentOfficer;
	/**
	 * Instance variables
	 */
	private OfficerController officerController;
	// OfficerController removed

	/**
	 * Constructor with parameters to initialize the view with a specific officer.
	 * 
	 * @param officer The Officer entity whose account is being managed
	 */
	public OfficerApplicationsView(Officer officer) {
		this.scanner = new Scanner(System.in);
		this.officerController = new OfficerController();
		this.currentOfficer = officer;
	}

	/**
	 * Displays the main menu for BTO application management and handles user interaction.
	 * Provides options to view applications, apply for new projects, request withdrawals,
	 * view bookings, or return to the main officer menu.
	 * 
	 * This is the primary entry point for this view as required by the IDisplayable interface.
	 */
	@Override
	public void display() {
		boolean exit = false;
		
		while (!exit) {
			System.out.println("\n===== MANAGE BTO APPLICATIONS =====");
			System.out.println("1. View My Applications");
			System.out.println("2. Apply for New BTO Project");
			System.out.println("3. Request Application Withdrawal");
			System.out.println("4. Book Flat");
			System.out.println("5. View My Bookings");
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
				viewMyApplications();
				return false;
			case "2":
				applyForBTOProject();
				return false;
			case "3":
				requestWithdrawal();
				return false;
			case "4":
				bookFlat();
				return false;
			case "5":
				viewBookings();
				return false;
			case "0":
				return true;
			default:
				System.out.println("Invalid choice. Please try again.");
				return false;
		}
	}

	/**
	 * Displays all BTO applications submitted by the current officer.
	 * Retrieves applications from the controller and formats them for display.
	 * If no applications are found, an appropriate message is displayed.
	 */
	private void viewMyApplications() {
		System.out.println("\n===== MY BTO APPLICATIONS =====");
		
		// Use OfficerController instead of OfficerController
		ArrayList<BTOApplication> applications = officerController.getApplications(currentOfficer);
		
		if (applications.isEmpty()) {
			System.out.println("You have no BTO applications.");
			return;
		}
		
		displayApplications(applications);
	}

	/**
	 * Allows the officer to apply for a new BTO project.
	 * Guides the officer through selecting an eligible project and flat type,
	 * checks eligibility, and confirms the application submission.
	 * 
	 * The process includes:
	 * 1. Checking if officer has active applications
	 * 2. Retrieving and displaying eligible projects
	 * 3. Getting and validating project selection
	 * 4. Selecting and validating flat type
	 * 5. Confirming and submitting application
	 */
	private void applyForBTOProject() {
		System.out.println("\n===== APPLY FOR A PROJECT =====");
		
		// Check if user has active applications
		if (officerController.hasActiveApplications(currentOfficer)) {
			System.out.println("You already have active application(s). You cannot apply for more projects until your current applications are processed.");
			return;
		}
		
		// Get eligible projects
		ArrayList<Project> eligibleProjects = officerController.getEligibleProjects(currentOfficer);
		
		if (eligibleProjects.isEmpty()) {
			System.out.println("No eligible projects available for application at this time.");
			return;
		}
		
		// Display eligible projects
		displayProjects(eligibleProjects);
		
		// Get user selection
		System.out.print("\nEnter the name of the project to apply for (or 0 to cancel): ");
		String projectName = scanner.nextLine().trim();
		
		if (projectName.equals("0")) {
			return;
		}
		
		// Find the selected project
		Project selectedProject = null;
		for (Project project : eligibleProjects) {
			if (project.getProjectName().equalsIgnoreCase(projectName)) {
				selectedProject = project;
				break;
			}
		}
		
		if (selectedProject == null) {
			System.out.println("Invalid project name. Please try again.");
			return;
		}
		
		// Select flat type
		System.out.println("\nAvailable Flat Types:");
		for (FlatType flatType : selectedProject.getFlatTypes()) {
			System.out.println(flatType.getType() + ": " + flatType.getAvailableUnits() + " units available, Price: $" + flatType.getSellingPrice());
		}
		
		System.out.print("\nEnter the flat type (TWO_ROOM or THREE_ROOM): ");
		String flatTypeStr = scanner.nextLine().trim().toUpperCase();
		
		FlatTypeEnum flatType = null;
		try {
			flatType = FlatTypeEnum.valueOf(flatTypeStr);
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid flat type. Application cancelled.");
			return;
		}
		
		// Check eligibility for flat type
		if (!OfficerController.isEligibleForFlatType(currentOfficer, flatType)) {
			System.out.println("You are not eligible for this flat type. Application cancelled.");
			return;
		}
		
		// Confirm application
		System.out.print("\nConfirm application for " + selectedProject.getProjectName() + 
				" with flat type " + flatType + "? (Y/N): ");
		String confirm = scanner.nextLine().trim().toUpperCase();
		
		if (!confirm.equals("Y")) {
			System.out.println("Application cancelled.");
			return;
		}
		
		// Submit application
		BTOApplication application = officerController.createApplication(currentOfficer, selectedProject, flatType);
		
		if (application != null) {
			System.out.println("Application submitted successfully!");
			System.out.println("Application details: " + application);
		} else {
			System.out.println("Failed to submit application. Please try again later.");
		}
	}

	/**
	 * Allows the officer to request a withdrawal for an existing BTO application.
	 * Displays eligible applications for withdrawal and processes the officer's selection.
	 * 
	 * The method filters applications to show only those that are eligible for withdrawal
	 * (those with WithdrawalStatusEnum.NA), allows selection, and confirms the request
	 * before submitting it to the controller.
	 */
	private void requestWithdrawal() {
		System.out.println("\n===== REQUEST APPLICATION WITHDRAWAL =====");
		
		// Get all applications using OfficerController
		ArrayList<BTOApplication> applications = officerController.getApplications(currentOfficer);
		ArrayList<BTOApplication> withdrawableApplications = new ArrayList<>();
		
		// Filter applications that can be withdrawn
		for (BTOApplication app : applications) {
			if (app.getWithdrawalStatus() == WithdrawalStatusEnum.NA) {
				withdrawableApplications.add(app);
			}
		}
		
		if (withdrawableApplications.isEmpty()) {
			System.out.println("You have no applications eligible for withdrawal.");
			return;
		}
		
		// Display withdrawable applications
		System.out.println("Your Applications:");
		for (int i = 0; i < withdrawableApplications.size(); i++) {
			BTOApplication app = withdrawableApplications.get(i);
			System.out.println((i + 1) + ". Project: " + app.getProject().getProjectName() + 
					", Flat Type: " + app.getFlatType() + 
					", Status: " + app.getStatus() + 
					", Application Date: " + app.getApplicationDate());
		}
		
		// Get user selection
		System.out.print("Select an application to withdraw (1-" + withdrawableApplications.size() + ") or 0 to cancel: ");
		String selection = scanner.nextLine();
		
		try {
			int choice = Integer.parseInt(selection);
			
			if (choice == 0) {
				return;
			}
			
			if (choice < 1 || choice > withdrawableApplications.size()) {
				System.out.println("Invalid selection.");
				return;
			}
			
			BTOApplication selectedApplication = withdrawableApplications.get(choice - 1);
			
			// Confirm withdrawal
			System.out.print("Are you sure you want to request withdrawal? (y/n): ");
			String confirm = scanner.nextLine().toLowerCase();
			
			if (!confirm.equals("y") && !confirm.equals("yes")) {
				System.out.println("Withdrawal request cancelled.");
				return;
			}
			
			// Request withdrawal using OfficerController
			boolean success = officerController.requestWithdrawal(selectedApplication);
			
			if (success) {
				System.out.println("Withdrawal request submitted successfully!");
				System.out.println("Your withdrawal request will be reviewed by a manager.");
			} else {
				System.out.println("Failed to submit withdrawal request.");
			}
			
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a number.");
		}
	}

	/**
	 * Displays a formatted list of BTO applications with their details.
	 * Shows project, flat type, status, dates, and other relevant information
	 * for each application in the provided list.
	 * 
	 * @param applications ArrayList of BTOApplication objects to display
	 */
	private void displayApplications(ArrayList<BTOApplication> applications) {
		System.out.println("\n------------------------------------------------------------------");
		System.out.printf("%-5s | %-15s | %-15s | %-10s | %-15s\n", 
				"No.", "Project", "Flat Type", "Status", "Withdrawal Status");
		System.out.println("------------------------------------------------------------------");
		
		for (int i = 0; i < applications.size(); i++) {
			BTOApplication app = applications.get(i);
			System.out.printf("%-5d | %-15s | %-15s | %-10s | %-15s\n", 
					(i+1), 
					app.getProject().getProjectName(), 
					app.getFlatType(), 
					app.getStatus(), 
					app.getWithdrawalStatus());
		}
		
		System.out.println("------------------------------------------------------------------");
	}

	/**
	 * Sets the current officer for this view.
	 * Used to update the officer context when needed.
	 * 
	 * @param currentOfficer The Officer entity to set as the current user
	 */
	public void setCurrentOfficer(Officer currentOfficer) {
		this.currentOfficer = currentOfficer;
	}

	/**
	 * Sets the officer controller for this view.
	 * Used to update the controller reference when needed.
	 * 
	 * @param officerController The OfficerController to handle business logic
	 */
	public void setOfficerController(OfficerController officerController) {
		this.officerController = officerController;
	}
	
	// Removed setOfficerController method

	/**
	 * Gets the current officer entity.
	 * 
	 * @return The Officer entity who is currently using this view
	 */
	public Officer getCurrentOfficer() {
		return this.currentOfficer;
	}

	/**
	 * Gets the current officer controller.
	 * 
	 * @return The OfficerController used by this view
	 */
	public OfficerController getOfficerController() {
		return this.officerController;
	}
	
	/**
	 * Allows applicants with successful applications to book a flat unit.
	 * Displays only eligible applications (those with SUCCESSFUL status)
	 * and guides the user through the flat booking process.
	 */
	private void bookFlat() {
		System.out.println("\n===== BOOK FLAT =====");
		
		ArrayList<BTOApplication> applications = officerController.getApplications(currentOfficer);
		
		// Filter applications that are successful
		ArrayList<BTOApplication> eligibleApplications = new ArrayList<>();
		for (BTOApplication app : applications) {
			// Check application status and withdrawal status
			if (app.getStatus() == BTOApplicationStatusEnum.SUCCESSFUL && 
					app.getWithdrawalStatus() == WithdrawalStatusEnum.NA) {
				
				// Use the controller method to check eligibility
				if (OfficerController.isEligibleForFlatType(currentOfficer, app.getFlatType())) {
					eligibleApplications.add(app);
				}
			}
		}
		
		if (eligibleApplications.isEmpty()) {
			System.out.println("You don't have any successful applications eligible for booking.");
			System.out.println("Note: Eligibility depends on your age and marital status:");
			System.out.println("- Singles must be 35+ years old and can only book 2-Room flats");
			System.out.println("- Married applicants must be 21+ years old");
			return;
		}
		
		// Display applications that can be booked (status = SUCCESSFUL)
		System.out.println("\n------------------------------------------------------------------");
		System.out.printf("%-5s | %-15s | %-15s | %-10s\n", 
				"No.", "Project", "Flat Type", "Status");
		System.out.println("------------------------------------------------------------------");
		
		for (int i = 0; i < eligibleApplications.size(); i++) {
			BTOApplication app = eligibleApplications.get(i);
			System.out.printf("%-5d | %-15s | %-15s | %-10s\n", 
					(i+1), 
					app.getProject().getProjectName(), 
					app.getFlatType(), 
					app.getStatus());
		}
		
		System.out.println("------------------------------------------------------------------");
		
		// Get user selection
		System.out.print("\nEnter the number of the application to book (or 0 to cancel): ");
		String input = scanner.nextLine().trim();
		
		if (input.equals("0")) {
			return;
		}
		
		int selection;
		try {
			selection = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			System.out.println("Invalid number. Booking cancelled.");
			return;
		}
		
		if (selection < 1 || selection > eligibleApplications.size()) {
			System.out.println("Invalid selection. Booking cancelled.");
			return;
		}
		
		BTOApplication selectedApp = eligibleApplications.get(selection - 1);
		
		// Get flat type for the project
		ArrayList<FlatType> flatTypes = selectedApp.getProject().getFlatTypes();
		FlatType selectedFlatType = null;
		
		for (FlatType flatType : flatTypes) {
			if (flatType.getType() == selectedApp.getFlatType()) {
				selectedFlatType = flatType;
				break;
			}
		}
		
		if (selectedFlatType == null) {
			System.out.println("Error: Flat type not found in project. Booking cancelled.");
			return;
		}
		
		// Confirm booking
		System.out.print("\nConfirm booking of " + selectedApp.getFlatType() + 
				" in " + selectedApp.getProject().getProjectName() + 
				" for $" + selectedFlatType.getSellingPrice() + "? (Y/N): ");
		String confirm = scanner.nextLine().trim().toUpperCase();
		
		if (!confirm.equals("Y")) {
			System.out.println("Booking cancelled.");
			return;
		}
		
		// Book flat
		boolean success = officerController.bookFlat(selectedApp, selectedFlatType);
		
		if (success) {
			System.out.println("Flat booking request submitted successfully!");
			System.out.println("Your booking is now pending. An officer will contact you soon to process your booking.");
		} else {
			System.out.println("Failed to submit booking request. Please try again later or contact support.");
		}
	}

	/**
	 * Displays all bookings made by the current officer.
	 * Shows booking details including project, flat, payment status, and receipt information.
	 * 
	 * Formats the bookings in a tabular display and allows the user to select
	 * a specific booking to view more detailed information.
	 */
	private void viewBookings() {
		System.out.println("\n===== MY FLAT BOOKINGS =====");
		
		// Get all flat bookings for the officer
		ArrayList<Booking> bookings = currentOfficer.getBooking();
		
		if (bookings.isEmpty()) {
			System.out.println("You have no flat bookings.");
			return;
		}
		
		// Display the bookings
		System.out.println("\n-----------------------------------------------------------------");
		System.out.printf("%-5s | %-15s | %-15s | %-10s | %-15s\n", 
				"No.", "Project", "Flat Type", "Status", "Booking Date");
		System.out.println("-----------------------------------------------------------------");
		
		for (int i = 0; i < bookings.size(); i++) {
			Booking booking = bookings.get(i);
			System.out.printf("%-5d | %-15s | %-15s | %-10s | %-15s\n", 
					(i+1), 
					booking.getApplication().getProject().getProjectName(), 
					booking.getApplication().getFlatType(), 
					booking.getStatus(), 
					booking.getBookingDateTime());
		}
		
		System.out.println("-----------------------------------------------------------------");
		
		// Provide more details for a specific booking if requested
		System.out.print("\nEnter booking number for more details (or 0 to return): ");
		String input = scanner.nextLine().trim();
		
		if (input.equals("0")) {
			return;
		}
		
		try {
			int selection = Integer.parseInt(input);
			
			if (selection < 1 || selection > bookings.size()) {
				System.out.println("Invalid selection.");
				return;
			}
			
			Booking selectedBooking = bookings.get(selection - 1);
			displayBookingDetails(selectedBooking);
			
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a number.");
		}
	}
	
	/**
	 * Displays detailed information about a specific booking.
	 * Shows comprehensive booking information in a formatted view.
	 * 
	 * @param booking The Booking object to display details for
	 */
	private void displayBookingDetails(Booking booking) {
		System.out.println("\n===== BOOKING DETAILS =====");
		System.out.println("Project: " + booking.getApplication().getProject().getProjectName());
		System.out.println("Flat Type: " + booking.getApplication().getFlatType());
		System.out.println("Status: " + booking.getStatus());
		System.out.println("Booking Date: " + booking.getBookingDateTime());
		
		// Wait for user to press enter to continue
		System.out.print("\nPress Enter to continue...");
		scanner.nextLine();
	}

	/**
	 * Displays a formatted list of projects with their details.
	 * Shows project name, neighborhood, application period, and flat types
	 * in a tabular format for easier readability.
	 * 
	 * @param projects ArrayList of Project objects to display
	 */
	private void displayProjects(ArrayList<Project> projects) {
		System.out.println("\n------------------------------------------------------");
		System.out.printf("%-15s | %-15s | %-10s | %-15s\n", 
				"Project Name", "Neighborhood", "Status", "Application Period");
		System.out.println("------------------------------------------------------");
		
		for (int i = 0; i < projects.size(); i++) {
			Project project = projects.get(i);
			String status = project.isApplicationOpen() ? "OPEN" : "CLOSED";
			String period = project.getApplicationStartDate() + " to " + project.getApplicationEndDate();
			
			System.out.printf("%-15s | %-15s | %-10s | %-15s\n", 
					project.getProjectName(), project.getNeighborhood(), status, period);
		}
		
		System.out.println("------------------------------------------------------");
	}
}