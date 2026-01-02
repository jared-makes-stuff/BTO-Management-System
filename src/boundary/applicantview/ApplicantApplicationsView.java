package boundary.applicantview;

import controller.*;
import entity.*;
import enums.*;
import interfaces.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Boundary class for managing an applicant's BTO housing applications.
 * 
 * This class provides a comprehensive interface for applicants to manage their
 * housing applications throughout the entire lifecycle. Applicants can apply for
 * new projects, view their existing applications, request withdrawals, book flats
 * when selected, and view their booking details.
 * 
 * The view handles all user interactions related to the application process,
 * from initial submission through to flat selection and booking.
 */
public class ApplicantApplicationsView implements IDisplayable {

	/**
	 * Core components needed for application management
	 */
	private Scanner scanner;
	private Applicant currentApplicant;
	private ApplicantController applicantController;

	/**
	 * Constructs an ApplicantApplicationsView with a specific applican
	 * 
	 * @param applicant The applicant whose applications will be managed
	 */
	public ApplicantApplicationsView(Applicant applicant) {
		this.scanner = new Scanner(System.in);
		this.applicantController = new ApplicantController();
		this.currentApplicant = applicant;
	}
	/**
	 * Displays the application management menu and processes user selections.
	 * Provides options for applying to projects, viewing applications, requesting
	 * withdrawals, booking flats, and viewing bookings.
	 */
	@Override
	public void display() {
		boolean exit = false;
		
		while (!exit) {
			System.out.println("\n===== MANAGE BTO APPLICATIONS =====");
			System.out.println("1. Apply for a Project");
			System.out.println("2. View My Applications");
			System.out.println("3. Request Withdrawal");
			System.out.println("4. Book Flat");
			System.out.println("5. View My Bookings");
			System.out.println("0. Return to Applicant Menu");
			System.out.print("Enter your choice: ");
			
			String input = scanner.nextLine();
			exit = getUserInput(input);
		}
	}

	/**
	 * Processes the user's menu selection and executes the appropriate application management function.
	 * 
	 * @param input The user's menu choice
	 * @return true if the user wants to exit to the main menu, false otherwise
	 */
	private boolean getUserInput(String input) {
		switch (input) {
			case "1":
				applyForProject();
				return false;
			case "2":
				viewMyApplications();
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
	 * Guides the applicant through the process of applying for a housing project.
	 * Validates eligibility, presents available projects, verifies flat type eligibility,
	 * and processes the application submission.
	 */
	private void applyForProject() {
		System.out.println("\n===== APPLY FOR A PROJECT =====");
		
		// Check if user has active applications
		if (applicantController.hasActiveApplications(currentApplicant)) {
			System.out.println("You already have active application(s). You cannot apply for more projects until your current applications are processed.");
			return;
		}
		
		// Get eligible projects
		ArrayList<Project> eligibleProjects = applicantController.getEligibleProjects(currentApplicant);
		
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
		if (!ApplicantController.isEligibleForFlatType(currentApplicant, flatType)) {
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
		BTOApplication application = applicantController.createApplication(currentApplicant, selectedProject, flatType);
		
		if (application != null) {
			System.out.println("Application submitted successfully!");
			System.out.println("Application details: " + application);
		} else {
			System.out.println("Failed to submit application. Please try again later.");
		}
	}

	/**
	 * Displays a list of all applications submitted by the current applicant.
	 * For each application, shows the project, flat type, application status,
	 * and withdrawal status.
	 */
	private void viewMyApplications() {
		System.out.println("\n===== MY APPLICATIONS =====");
		
		ArrayList<BTOApplication> applications = applicantController.getApplications(currentApplicant);
		
		if (applications.isEmpty()) {
			System.out.println("You don't have any applications yet.");
			return;
		}
		
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
	 * Allows the applicant to request withdrawal from an eligible application.
	 * Displays only applications that can be withdrawn (those with NA withdrawal status)
	 * and guides the user through the withdrawal request process.
	 */
	private void requestWithdrawal() {
		System.out.println("\n===== REQUEST WITHDRAWAL =====");
		
		ArrayList<BTOApplication> applications = applicantController.getApplications(currentApplicant);
		
		if (applications.isEmpty()) {
			System.out.println("You don't have any applications to withdraw.");
			return;
		}
		
		// Display applications
		System.out.println("\n------------------------------------------------------------------");
		System.out.printf("%-5s | %-15s | %-15s | %-10s | %-15s\n", 
				"No.", "Project", "Flat Type", "Status", "Withdrawal Status");
		System.out.println("------------------------------------------------------------------");
		
		int validCount = 0;
		for (int i = 0; i < applications.size(); i++) {
			BTOApplication app = applications.get(i);
			// Only show applications that can be withdrawn
			if (app.getWithdrawalStatus() == WithdrawalStatusEnum.NA) {
				validCount++;
				System.out.printf("%-5d | %-15s | %-15s | %-10s | %-15s\n", 
						validCount, 
						app.getProject().getProjectName(), 
						app.getFlatType(), 
						app.getStatus(), 
						app.getWithdrawalStatus());
			}
		}
		
		if (validCount == 0) {
			System.out.println("You don't have any applications eligible for withdrawal.");
			return;
		}
		
		System.out.println("------------------------------------------------------------------");
		
		// Get user selection
		System.out.print("\nEnter the number of the application to withdraw (or 0 to cancel): ");
		String input = scanner.nextLine().trim();
		
		if (input.equals("0")) {
			return;
		}
		
		int selection;
		try {
			selection = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			System.out.println("Invalid number. Withdrawal cancelled.");
			return;
		}
		
		if (selection < 1 || selection > validCount) {
			System.out.println("Invalid selection. Withdrawal cancelled.");
			return;
		}
		
		// Find the selected application
		BTOApplication selectedApp = null;
		int count = 0;
		for (BTOApplication app : applications) {
			if (app.getWithdrawalStatus() == WithdrawalStatusEnum.NA) {
				count++;
				if (count == selection) {
					selectedApp = app;
					break;
				}
			}
		}
		
		// Confirm withdrawal
		System.out.print("\nConfirm withdrawal of application for " + 
				selectedApp.getProject().getProjectName() + "? (Y/N): ");
		String confirm = scanner.nextLine().trim().toUpperCase();
		
		if (!confirm.equals("Y")) {
			System.out.println("Withdrawal cancelled.");
			return;
		}
		
		// Submit withdrawal request
		boolean success = applicantController.requestWithdrawal(selectedApp);
		
		if (success) {
			System.out.println("Withdrawal request submitted successfully!");
		} else {
			System.out.println("Failed to submit withdrawal request. Please try again later.");
		}
	}

	/**
	 * Allows applicants with successful applications to book a flat unit.
	 * Displays only eligible applications (those with SUCCESSFUL status)
	 * and guides the user through the flat booking process.
	 */
	private void bookFlat() {
		System.out.println("\n===== BOOK FLAT =====");
		
		ArrayList<BTOApplication> applications = applicantController.getApplications(currentApplicant);
		
		// Filter applications that are successful
		ArrayList<BTOApplication> eligibleApplications = new ArrayList<>();
		for (BTOApplication app : applications) {
			// Check application status and withdrawal status
			if (app.getStatus() == BTOApplicationStatusEnum.SUCCESSFUL && 
					app.getWithdrawalStatus() == WithdrawalStatusEnum.NA) {
				
				// Use the controller method to check eligibility
				if (ApplicantController.isEligibleForFlatType(currentApplicant, app.getFlatType())) {
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
		boolean success = applicantController.bookFlat(selectedApp, selectedFlatType);
		
		if (success) {
			System.out.println("Flat booking request submitted successfully!");
			System.out.println("Your booking is now pending. An officer will contact you soon to process your booking.");
		} else {
			System.out.println("Failed to submit booking request. Please try again later or contact support.");
		}
	}

	/**
	 * Displays all flat bookings made by the current applicant.
	 * Shows detailed information about each booking including project,
	 * flat type, unit details, and booking status.
	 */
	private void viewBookings() {
		System.out.println("\n===== MY BOOKINGS =====");
		
		// Get all flat bookings for the applicant
		ArrayList<Booking> bookings = currentApplicant.getBooking();
		
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
	 * Displays a formatted list of projects with their key details.
	 * For each project, shows the name, neighborhood, application status, and period.
	 * 
	 * @param projects The list of projects to display
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

	/**
	 * Sets the current applicant whose applications will be managed.
	 * 
	 * @param currentApplicant The applicant who will use the applications view
	 */
	public void setCurrentApplicant(Applicant currentApplicant) {
		this.currentApplicant = currentApplicant;
	}

	/**
	 * Sets the controller for handling business logic related to application operations.
	 * 
	 * @param applicantController The controller instance to use for this view
	 */
	public void setApplicantController(ApplicantController applicantController) {
		this.applicantController = applicantController;
	}

	/**
	 * Gets the currently active applicant.
	 * 
	 * @return The current applicant using the application management features
	 */
	public Applicant getCurrentApplicant() {
		return this.currentApplicant;
	}

	/**
	 * Gets the controller used by this view.
	 * 
	 * @return The applicant controller instance
	 */
	public ApplicantController getApplicantController() {
		return this.applicantController;
	}

	/**
	 * Default constructor that initializes a new ApplicantApplicationsView with a Scanner
	 * and controller but no applicant yet.
	 */
	public ApplicantApplicationsView() {
		this.scanner = new Scanner(System.in);
		this.applicantController = new ApplicantController();
	}
}