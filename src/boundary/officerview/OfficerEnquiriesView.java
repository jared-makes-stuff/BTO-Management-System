package boundary.officerview;

import controller.*;
import entity.*;
import enums.*;
import interfaces.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Boundary class for managing officer interactions with the enquiry system.
 * This view allows officers to submit, view, edit, and delete their enquiries about BTO projects.
 * 
 * The class implements the IDisplayable interface to provide a consistent UI pattern
 * throughout the application and uses the OfficerController to handle business logic operations.
 * 
 * @implements IDisplayable Interface for standardized display functionality
 */
public class OfficerEnquiriesView implements IDisplayable {

	private Scanner scanner;
	private Officer currentOfficer;
	private OfficerController officerController;
	
	/**
	 * Constructor with parameters to initialize the view with a specific officer.
	 * 
	 * @param officer The Officer entity whose account is being managed
	 */
	public OfficerEnquiriesView(Officer officer) {
		this.scanner = new Scanner(System.in);
		this.officerController = new OfficerController();
		this.currentOfficer = officer;
	}
	
	/**
	 * Displays the main menu for enquiry management and handles user interaction.
	 * Provides options to submit new enquiries, view existing enquiries, 
	 * edit pending enquiries, delete pending enquiries, or return to the main officer menu.
	 * 
	 * This is the primary entry point for this view as required by the IDisplayable interface.
	 * The method runs in a loop until the user chooses to exit by selecting option 0.
	 */
	@Override
	public void display() {
		boolean exit = false;
		
		while (!exit) {
			System.out.println("\n===== MANAGE ENQUIRIES =====");
			System.out.println("1. Submit New Enquiry");
			System.out.println("2. View My Enquiries");
			System.out.println("3. Edit My Enquiry");
			System.out.println("4. Delete My Enquiry");
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
				submitEnquiry();
				return false;
			case "2":
				viewMyEnquiries();
				return false;
			case "3":
				editEnquiry();
				return false;
			case "4":
				deleteEnquiry();
				return false;
			case "0":
				return true;
			default:
				System.out.println("Invalid choice. Please try again.");
				return false;
		}
	}

	/**
	 * Allows the officer to submit a new enquiry about a BTO project.
	 * This method guides the officer through:
	 * 1. Selecting a project from available projects
	 * 2. Entering the content of the enquiry
	 * 3. Submitting the enquiry through the controller
	 * 
	 * Validation is performed to ensure projects exist and the enquiry content is not empty.
	 */
	private void submitEnquiry() {
		System.out.println("\n===== SUBMIT NEW ENQUIRY =====");
		
		// Get a list of projects to choose from
		ArrayList<Project> projects = officerController.getEligibleProjects(currentOfficer);
		
		if (projects.isEmpty()) {
			System.out.println("No projects available to submit enquiries for.");
			return;
		}
		
		// Display projects
		System.out.println("\nAvailable Projects:");
		System.out.println("------------------------------------------------------");
		System.out.printf("%-5s | %-20s | %-20s\n", "No.", "Project Name", "Neighborhood");
		System.out.println("------------------------------------------------------");
		
		for (int i = 0; i < projects.size(); i++) {
			Project project = projects.get(i);
			System.out.printf("%-5d | %-20s | %-20s\n", 
					(i+1), project.getProjectName(), project.getNeighborhood());
		}
		
		System.out.println("------------------------------------------------------");
		
		// Get project selection
		System.out.print("\nEnter the number of the project to enquire about (or 0 to cancel): ");
		String input = scanner.nextLine().trim();
		
		if (input.equals("0")) {
			return;
		}
		
		int selection;
		try {
			selection = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			System.out.println("Invalid number. Enquiry cancelled.");
			return;
		}
		
		if (selection < 1 || selection > projects.size()) {
			System.out.println("Invalid selection. Enquiry cancelled.");
			return;
		}
		
		Project selectedProject = projects.get(selection - 1);
		
		// Get enquiry content
		System.out.println("\nSelected Project: " + selectedProject.getProjectName());
		System.out.println("Please enter your enquiry below (or type 'cancel' to cancel):");
		String content = scanner.nextLine().trim();
		
		if (content.equalsIgnoreCase("cancel")) {
			System.out.println("Enquiry cancelled.");
			return;
		}
		
		if (content.isEmpty()) {
			System.out.println("Enquiry cannot be empty. Enquiry cancelled.");
			return;
		}
		
		// Submit enquiry
		Enquiry enquiry = officerController.submitEnquiry(currentOfficer, selectedProject, content);
		
		if (enquiry != null) {
			System.out.println("Enquiry submitted successfully!");
			System.out.println("Enquiry ID: " + enquiry.getEnquiryID());
		} else {
			System.out.println("Failed to submit enquiry. Please try again later.");
		}
	}

	/**
	 * Displays all enquiries submitted by the current officer.
	 * Retrieves all enquiries associated with the current officer from the controller
	 * and formats them for display. If no enquiries are found, an appropriate 
	 * message is displayed.
	 */
	private void viewMyEnquiries() {
		System.out.println("\n===== MY ENQUIRIES =====");
		
		ArrayList<Enquiry> enquiries = officerController.getEnquiries(currentOfficer);
		
		if (enquiries.isEmpty()) {
			System.out.println("You don't have any enquiries yet.");
			return;
		}
		
		displayEnquiries(enquiries);
	}

	/**
	 * Allows the officer to edit the content of a pending enquiry.
	 * This method:
	 * 1. Retrieves all enquiries for the current officer
	 * 2. Filters for only those with PENDING status
	 * 3. Displays the pending enquiries
	 * 4. Allows selection of an enquiry to edit
	 * 5. Gets the new content and updates the enquiry through the controller
	 * 
	 * Only pending enquiries (those that have not been replied to) can be edited.
	 */
	private void editEnquiry() {
		System.out.println("\n===== EDIT MY ENQUIRY =====");
		
		// Get user's enquiries
		ArrayList<Enquiry> enquiries = officerController.getEnquiries(currentOfficer);
		
		if (enquiries.isEmpty()) {
			System.out.println("You don't have any enquiries to edit.");
			return;
		}
		
		// Filter pending enquiries (can only edit pending enquiries)
		ArrayList<Enquiry> pendingEnquiries = new ArrayList<>();
		for (Enquiry enquiry : enquiries) {
			if (enquiry.getStatus() == EnquiryStatusEnum.PENDING) {
				pendingEnquiries.add(enquiry);
			}
		}
		
		if (pendingEnquiries.isEmpty()) {
			System.out.println("You don't have any pending enquiries that can be edited.");
			return;
		}
		
		// Display pending enquiries
		System.out.println("\nPending Enquiries:");
		displayEnquiries(pendingEnquiries);
		
		// Get enquiry selection
		System.out.print("\nEnter the number of the enquiry to edit (or 0 to cancel): ");
		String input = scanner.nextLine().trim();
		
		if (input.equals("0")) {
			return;
		}
		
		int selection;
		try {
			selection = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			System.out.println("Invalid number. Edit cancelled.");
			return;
		}
		
		if (selection < 1 || selection > pendingEnquiries.size()) {
			System.out.println("Invalid selection. Edit cancelled.");
			return;
		}
		
		Enquiry selectedEnquiry = pendingEnquiries.get(selection - 1);
		
		// Display current content
		System.out.println("\nCurrent Enquiry Content:");
		System.out.println(selectedEnquiry.getContent());
		
		// Get new content
		System.out.println("\nEnter new content (or type 'cancel' to cancel):");
		String newContent = scanner.nextLine().trim();
		
		if (newContent.equalsIgnoreCase("cancel")) {
			System.out.println("Edit cancelled.");
			return;
		}
		
		if (newContent.isEmpty()) {
			System.out.println("New content cannot be empty. Edit cancelled.");
			return;
		}
		
		// Update enquiry
		boolean success = officerController.editEnquiry(selectedEnquiry, newContent);
		
		if (success) {
			System.out.println("Enquiry updated successfully!");
		} else {
			System.out.println("Failed to update enquiry. Please try again later.");
		}
	}

	/**
	 * Allows the officer to delete a pending enquiry.
	 * This method:
	 * 1. Retrieves all enquiries for the current officer
	 * 2. Filters for only those with PENDING status
	 * 3. Displays the pending enquiries
	 * 4. Allows selection of an enquiry to delete
	 * 5. Confirms the deletion and processes it through the controller
	 * 
	 * Only pending enquiries (those that have not been replied to) can be deleted.
	 */
	private void deleteEnquiry() {
		System.out.println("\n===== DELETE MY ENQUIRY =====");
		
		// Get user's enquiries
		ArrayList<Enquiry> enquiries = officerController.getEnquiries(currentOfficer);
		
		if (enquiries.isEmpty()) {
			System.out.println("You don't have any enquiries to delete.");
			return;
		}
		
		// Filter pending enquiries (can only delete pending enquiries)
		ArrayList<Enquiry> pendingEnquiries = new ArrayList<>();
		for (Enquiry enquiry : enquiries) {
			if (enquiry.getStatus() == EnquiryStatusEnum.PENDING) {
				pendingEnquiries.add(enquiry);
			}
		}
		
		if (pendingEnquiries.isEmpty()) {
			System.out.println("You don't have any pending enquiries that can be deleted.");
			return;
		}
		
		// Display pending enquiries
		System.out.println("\nPending Enquiries:");
		displayEnquiries(pendingEnquiries);
		
		// Get enquiry selection
		System.out.print("\nEnter the number of the enquiry to delete (or 0 to cancel): ");
		String input = scanner.nextLine().trim();
		
		if (input.equals("0")) {
			return;
		}
		
		int selection;
		try {
			selection = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			System.out.println("Invalid number. Delete cancelled.");
			return;
		}
		
		if (selection < 1 || selection > pendingEnquiries.size()) {
			System.out.println("Invalid selection. Delete cancelled.");
			return;
		}
		
		Enquiry selectedEnquiry = pendingEnquiries.get(selection - 1);
		
		// Confirm deletion
		System.out.print("\nAre you sure you want to delete this enquiry? (Y/N): ");
		String confirm = scanner.nextLine().trim().toUpperCase();
		
		if (!confirm.equals("Y")) {
			System.out.println("Delete cancelled.");
			return;
		}
		
		// Delete enquiry
		boolean success = officerController.deleteEnquiry(selectedEnquiry);
		
		if (success) {
			System.out.println("Enquiry deleted successfully!");
		} else {
			System.out.println("Failed to delete enquiry. Please try again later.");
		}
	}

	/**
	 * Displays a formatted list of enquiries with their details.
	 * Shows enquiry ID, project name, date, status, reply date, and a preview of 
	 * the content and reply for each enquiry in a tabular format.
	 * 
	 * @param enquiries ArrayList of Enquiry objects to display
	 */
	private void displayEnquiries(ArrayList<Enquiry> enquiries) {
		System.out.println("\n---------------------------------------------------------------------------");
		System.out.printf("%-5s | %-10s | %-15s | %-12s | %-10s | %-15s\n", 
				"No.", "Enquiry ID", "Project", "Date", "Status", "Reply Date");
		System.out.println("---------------------------------------------------------------------------");
		
		for (int i = 0; i < enquiries.size(); i++) {
			Enquiry enquiry = enquiries.get(i);
			String replyDate = (enquiry.getReplyDate() != null) ? enquiry.getReplyDate().toString() : "N/A";
			
			System.out.printf("%-5d | %-10s | %-15s | %-12s | %-10s | %-15s\n", 
					(i+1), 
					enquiry.getEnquiryID(), 
					enquiry.getProject().getProjectName(), 
					enquiry.getDateTime(), 
					enquiry.getStatus(), 
					replyDate);
			
			// Show brief content preview
			String contentPreview = enquiry.getContent();
			if (contentPreview.length() > 50) {
				contentPreview = contentPreview.substring(0, 47) + "...";
			}
			System.out.println("  Content: " + contentPreview);
			
			// Show reply if available
			if (enquiry.getStatus() == EnquiryStatusEnum.REPLIED) {
				String replyPreview = enquiry.getReply();
				if (replyPreview != null) {
					if (replyPreview.length() > 50) {
						replyPreview = replyPreview.substring(0, 47) + "...";
					}
					System.out.println("  Reply: " + replyPreview);
				}
			}
			
			System.out.println("---------------------------------------------------------------------------");
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

