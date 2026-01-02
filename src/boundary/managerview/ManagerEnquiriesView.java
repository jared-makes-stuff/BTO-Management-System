package boundary.managerview;

import controller.*;
import entity.*;
import interfaces.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Boundary class for handling and displaying enquiry management functionality for managers.
 * This view allows managers to view, filter, and reply to enquiries submitted by applicants
 * regarding the BTO projects they manage.
 * 
 * @implements IDisplayable
 */
public class ManagerEnquiriesView implements IDisplayable {

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
	public ManagerEnquiriesView(Manager manager) {
        this.scanner = new Scanner(System.in);
        this.managerController = new ManagerController();
        this.currentManager = manager;
	}

	/**
	 * Displays the main menu for enquiry management and handles user interaction.
	 * Provides options to view all enquiries, view project-specific enquiries,
	 * reply to enquiries, or return to the main manager menu.
	 */
	@Override
	public void display() {
		boolean exit = false;
		
		while (!exit) {
			System.out.println("\n===== MANAGE ENQUIRIES =====");
			System.out.println("1. View All Enquiries");
			System.out.println("2. View Project Enquiries");
			System.out.println("3. Reply to Enquiries");
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
				viewAllEnquiries();
				return false;
			case "2":
				viewProjectEnquiries();
				return false;
			case "3":
				replyToEnquiries();
				return false;
			case "0":
				return true;
			default:
				System.out.println("Invalid choice. Please try again.");
				return false;
		}
	}

	/**
	 * Displays all enquiries for all projects managed by the current manager.
	 * If no enquiries are found, an appropriate message is displayed.
	 */
	private void viewAllEnquiries() {
		System.out.println("\n===== ALL ENQUIRIES =====");
		
		ArrayList<Enquiry> enquiries = managerController.getEnquiriesByManagedProjects(currentManager);
		
		if (enquiries.isEmpty()) {
			System.out.println("No enquiries found for your managed projects.");
		} else {
			displayEnquiries(enquiries);
		}
	}

	/**
	 * Allows the manager to view enquiries for a specific project they manage.
	 * The manager selects a project from a list of their managed projects.
	 */
	private void viewProjectEnquiries() {
		System.out.println("\n===== PROJECT ENQUIRIES =====");
		
		// Get all projects managed by this manager
		ArrayList<Project> managedProjects = managerController.getManagedProjects(currentManager);
		
		if (managedProjects.isEmpty()) {
			System.out.println("You are not managing any projects.");
			return;
		}
		
		// Display managed projects
		System.out.println("\nProjects you manage:");
		for (int i = 0; i < managedProjects.size(); i++) {
			Project project = managedProjects.get(i);
			System.out.println((i+1) + ". " + project.getProjectName() + " (" + project.getNeighborhood() + ")");
		}
		
		// Select project
		System.out.print("\nEnter number of project to view enquiries for (or 0 to cancel): ");
		int selection;
		try {
			selection = Integer.parseInt(scanner.nextLine().trim());
			if (selection == 0) {
				return;
			}
			if (selection < 1 || selection > managedProjects.size()) {
				System.out.println("Invalid selection.");
				return;
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input.");
			return;
		}
		
		Project selectedProject = managedProjects.get(selection - 1);
		
		// Get enquiries for selected project
		ArrayList<Enquiry> enquiries = Enquiry.findEnquiriesByProject(selectedProject);
		
		if (enquiries.isEmpty()) {
			System.out.println("No enquiries found for project " + selectedProject.getProjectName() + ".");
		} else {
			System.out.println("\n===== ENQUIRIES FOR " + selectedProject.getProjectName().toUpperCase() + " =====");
			displayEnquiries(enquiries);
		}
	}

	/**
	 * Allows the manager to reply to pending enquiries for their managed projects.
	 * The manager selects an enquiry to reply to and provides a response.
	 */
	private void replyToEnquiries() {
		System.out.println("\n===== REPLY TO ENQUIRIES =====");
		
		// Get all pending enquiries for projects managed by this manager
		ArrayList<Enquiry> pendingEnquiries = managerController.getPendingEnquiriesByManagedProjects(currentManager);
		
		if (pendingEnquiries.isEmpty()) {
			System.out.println("No pending enquiries to reply to.");
			return;
		}
		
		// Display pending enquiries
		System.out.println("\nPending Enquiries:");
		displayEnquiries(pendingEnquiries);
		
		// Select enquiry to reply to
		System.out.print("\nEnter enquiry ID to reply to (or 0 to cancel): ");
		String enquiryId = scanner.nextLine().trim();
		if (enquiryId.equals("0")) {
			return;
		}
		
		// Find the selected enquiry
		Enquiry selectedEnquiry = null;
		for (Enquiry enquiry : pendingEnquiries) {
			if (enquiry.getEnquiryID().equals(enquiryId)) {
				selectedEnquiry = enquiry;
				break;
			}
		}
		
		if (selectedEnquiry == null) {
			System.out.println("Enquiry with ID " + enquiryId + " not found or is not pending.");
			return;
		}
		
		// Get reply for the enquiry
		System.out.println("\nEnquiry: " + selectedEnquiry.getContent());
		System.out.print("Enter your reply: ");
		String reply = scanner.nextLine().trim();
		
		if (reply.isEmpty()) {
			System.out.println("Reply cannot be empty.");
			return;
		}
		
		// Process the reply
		boolean success = managerController.replyToEnquiry(currentManager, selectedEnquiry, reply);
		
		if (success) {
			System.out.println("Enquiry replied successfully!");
		} else {
			System.out.println("Failed to reply to enquiry. Please try again.");
		}
	}

	/**
	 * Displays a formatted list of enquiries with detailed information.
	 * Shows enquiry ID, project, status, applicant, date and reply date.
	 * 
	 * @param enquiries ArrayList of Enquiry objects to display
	 */
	private void displayEnquiries(ArrayList<Enquiry> enquiries) {
		if (enquiries.isEmpty()) {
			System.out.println("No enquiries to display.");
			return;
		}
		
		System.out.println("\n---------------------------------------------------------------------");
		System.out.printf("%-10s | %-20s | %-12s | %-10s | %-10s | %-15s\n", 
				"ID", "Project", "Status", "Applicant", "Date", "Reply Date");
		System.out.println("---------------------------------------------------------------------");
		
		for (Enquiry enquiry : enquiries) {
			Project project = enquiry.getProject();
			Applicant applicant = enquiry.getSubmittedBy();
			
			System.out.printf("%-10s | %-20s | %-12s | %-10s | %s | %s\n", 
					enquiry.getEnquiryID(),
					project.getProjectName(),
					enquiry.getStatus(),
					applicant.getName(),
					enquiry.getDateTime(),
					enquiry.getReplyDate() != null ? enquiry.getReplyDate().toString() : "N/A");
			
			System.out.println("Query: " + enquiry.getContent());
			if (enquiry.getReply() != null && !enquiry.getReply().isEmpty()) {
				System.out.println("Reply: " + enquiry.getReply());
			}
			System.out.println("---------------------------------------------------------------------");
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
	public ManagerEnquiriesView() {
		this.scanner = new Scanner(System.in);
		this.managerController = new ManagerController();
	}
}