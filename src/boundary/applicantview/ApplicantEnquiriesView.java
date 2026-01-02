package boundary.applicantview;

import controller.*;
import entity.*;
import enums.*;
import interfaces.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Boundary class for managing an applicant's enquiries about housing projects.
 * 
 * This class provides a comprehensive interface for applicants to manage their
 * enquiries throughout the entire lifecycle. Applicants can submit new enquiries,
 * view existing ones, edit pending enquiries, and delete enquiries they no longer need.
 * 
 * The view handles all user interactions related to the enquiry process,
 * enabling effective communication between applicants and housing officers.
 */
public class ApplicantEnquiriesView implements IDisplayable {

	/**
	 * Core components needed for enquiry management
	 */
	private Scanner scanner;
	private Applicant currentApplicant;
	private ApplicantController applicantController;

	/**
	 * Constructs an ApplicantEnquiriesView with a specific applicant
	 * 
	 * @param applicant The applicant whose enquiries will be managed
	 */
	public ApplicantEnquiriesView(Applicant applicant) {
		this.scanner = new Scanner(System.in);
		this.applicantController = new ApplicantController();
		this.currentApplicant = applicant;
	}

	/**
	 * Displays the enquiry management menu and processes user selections.
	 * Provides options for submitting, viewing, editing, and deleting enquiries
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
			System.out.println("0. Return to Applicant Menu");
			System.out.print("Enter your choice: ");
			
			String input = scanner.nextLine();
			exit = getUserInput(input);
		}
	}

	/**
	 * Processes the user's menu selection and executes the appropriate enquiry management function.
	 * 
	 * @param input The user's menu choice
	 * @return true if the user wants to exit to the main menu, false otherwise
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
	 * Guides the applicant through the process of submitting a new enquiry.
	 * The applicant selects a project to enquire about and provides the content
	 * of their question or concern, which is then submitted to the system.
	 */
	private void submitEnquiry() {
		System.out.println("\n===== SUBMIT NEW ENQUIRY =====");
		
		// Get a list of projects to choose from
		ArrayList<Project> projects = applicantController.getEligibleProjects(currentApplicant);
		
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
		Enquiry enquiry = applicantController.submitEnquiry(currentApplicant, selectedProject, content);
		
		if (enquiry != null) {
			System.out.println("Enquiry submitted successfully!");
			System.out.println("Enquiry ID: " + enquiry.getEnquiryID());
		} else {
			System.out.println("Failed to submit enquiry. Please try again later.");
		}
	}

	/**
	 * Displays a list of all enquiries submitted by the current applicant.
	 * For each enquiry, shows key details like the project, status, and 
	 * submission date, as well as the content and any responses.
	 */
	private void viewMyEnquiries() {
		System.out.println("\n===== MY ENQUIRIES =====");
		
		ArrayList<Enquiry> enquiries = applicantController.getEnquiries(currentApplicant);
		
		if (enquiries.isEmpty()) {
			System.out.println("You don't have any enquiries yet.");
			return;
		}
		
		displayEnquiries(enquiries);
	}

	/**
	 * Allows the applicant to edit a pending enquiry.
	 * Only enquiries that have not yet been replied to (with PENDING status)
	 * can be edited. The applicant selects an enquiry and provides new content.
	 */
	private void editEnquiry() {
		System.out.println("\n===== EDIT MY ENQUIRY =====");
		
		// Get user's enquiries
		ArrayList<Enquiry> enquiries = applicantController.getEnquiries(currentApplicant);
		
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
		boolean success = applicantController.editEnquiry(selectedEnquiry, newContent);
		
		if (success) {
			System.out.println("Enquiry updated successfully!");
		} else {
			System.out.println("Failed to update enquiry. Please try again later.");
		}
	}

	/**
	 * Allows the applicant to delete an enquiry.
	 * Presents a list of the applicant's enquiries, allows them to select one,
	 * and then removes it from the system after confirmation.
	 */
	private void deleteEnquiry() {
		System.out.println("\n===== DELETE MY ENQUIRY =====");
		
		// Get user's enquiries
		ArrayList<Enquiry> enquiries = applicantController.getEnquiries(currentApplicant);
		
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
		boolean success = applicantController.deleteEnquiry(selectedEnquiry);
		
		if (success) {
			System.out.println("Enquiry deleted successfully!");
		} else {
			System.out.println("Failed to delete enquiry. Please try again later.");
		}
	}

	/**
	 * Displays a formatted list of enquiries with their key details.
	 * For each enquiry, shows the ID, project, status, submission date/time,
	 * content, and any officer responses.
	 * 
	 * @param enquiries The list of enquiries to display
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
	 * Sets the current applicant whose enquiries will be managed.
	 * 
	 * @param currentApplicant The applicant who will use the enquiries view
	 */
	public void setCurrentApplicant(Applicant currentApplicant) {
		this.currentApplicant = currentApplicant;
	}

	/**
	 * Sets the controller for handling business logic related to enquiry operations.
	 * 
	 * @param applicantController The controller instance to use for this view
	 */
	public void setApplicantController(ApplicantController applicantController) {
		this.applicantController = applicantController;
	}

	/**
	 * Gets the currently active applicant.
	 * 
	 * @return The current applicant using the enquiry management features
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
	 * Default constructor that initializes a new ApplicantEnquiriesView with a Scanner
	 * and controller but no applicant yet.
	 */
	public ApplicantEnquiriesView() {
		this.scanner = new Scanner(System.in);
		this.applicantController = new ApplicantController();
	}
}