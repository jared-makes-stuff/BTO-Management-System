package boundary.officerview;

import controller.*;
import entity.*;
import interfaces.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * View class for officers to browse and filter projects in the system.
 * Provides functionality for viewing all projects, applying filters,
 * and specifically viewing projects assigned to the current officer.
 * Implements the IDisplayable interface to provide a user interface.
 */
public class OfficerProjectsView implements IDisplayable {

	private Scanner scanner;
	private Officer currentOfficer;
	private OfficerController officerController;

	/**
	 * Constructor with parameters to initialize the view with a specific officer.
	 * 
	 * @param officer The Officer entity whose account is being managed
	 */
	public OfficerProjectsView(Officer officer) {
		this.scanner = new Scanner(System.in);
		this.officerController = new OfficerController();
		this.currentOfficer = officer;
	}

	/**
	 * Displays the projects menu and handles user interaction.
	 * Provides options for viewing all projects, filtered projects, and officer's assigned projects.
	 */
	@Override
	public void display() {
		boolean exit = false;
		
		while (!exit) {
			System.out.println("\n===== VIEW PROJECTS =====");
			System.out.println("1. View All Projects");
			System.out.println("2. View Projects with Filter");
			System.out.println("3. View My Assigned Projects");  // Updated text to reflect multiple projects
			System.out.println("0. Return to Officer Menu");
			System.out.print("Enter your choice: ");
			
			String input = scanner.nextLine();
			exit = getUserInput(input);
		}
	}

	/**
	 * Processes the user's menu selection.
	 * 
	 * @param input The user's menu choice as a string
	 * @return true if the user wants to exit, false to stay in the menu
	 */
	private boolean getUserInput(String input) {
		switch (input) {
			case "1":
				viewAllProjects();
				return false;
			case "2":
				viewProjectsWithFilter();
				return false;
			case "3":
				viewAssignedProjects();  // Changed method name to match plural
				return false;
			case "0":
				return true;
			default:
				System.out.println("Invalid choice. Please try again.");
				return false;
		}
	}

	/**
	 * Displays a list of all projects available to the officer.
	 * Includes visible projects, assigned projects, and projects the officer has applied for.
	 */
	private void viewAllProjects() {
		System.out.println("\n===== ALL AVAILABLE PROJECTS =====");
		// Store all visible projects in a list
		ArrayList<Project> visibleProjects = officerController.getAllVisibleProjects();

		// Include all Assigned and Applied projects for the current officer
		for (Project project : officerController.getAssignedProjects(currentOfficer)){
			if (!visibleProjects.contains(project)) {
				visibleProjects.add(project);
			}
		}
		for (Project project : officerController.getAppliedProjects(currentOfficer)){
			if (!visibleProjects.contains(project)) {
				visibleProjects.add(project);
			}
		}
		
		if (visibleProjects.isEmpty()) {
			System.out.println("No projects available at this time.");
		} else {
			displayProjects(visibleProjects);
		}
	}

	/**
	 * Allows the officer to filter projects based on various criteria.
	 * Creates a filter based on user input and applies it to the project list.
	 */
	private void viewProjectsWithFilter() {
		System.out.println("\n===== FILTER PROJECTS =====");
		
		// Create a new filter
		Filter filter = new Filter();
		
		// Get filter criteria
		System.out.println("Enter filter criteria (leave blank to skip):");
		
		System.out.print("Project Name: ");
		String projectName = scanner.nextLine().trim();
		if (!projectName.isEmpty()) {
			filter.setProjectName(projectName);
		}
		
		System.out.print("Neighborhood (comma-separated for multiple): ");
		String neighborhoodInput = scanner.nextLine().trim();
		if (!neighborhoodInput.isEmpty()) {
			String[] neighborhoods = neighborhoodInput.split(",");
			ArrayList<String> neighborhoodList = new ArrayList<>();
			for (String neighborhood : neighborhoods) {
				neighborhoodList.add(neighborhood.trim());
			}
			filter.setNeighborhoodList(neighborhoodList);
		}
		
		System.out.print("Minimum Price (SGD): ");
		String minPriceStr = scanner.nextLine().trim();
		if (!minPriceStr.isEmpty()) {
			try {
				double minPrice = Double.parseDouble(minPriceStr);
				filter.setMinPrice(minPrice);
			} catch (NumberFormatException e) {
				System.out.println("Invalid number format. Using default minimum price.");
			}
		}
		
		System.out.print("Maximum Price (SGD): ");
		String maxPriceStr = scanner.nextLine().trim();
		if (!maxPriceStr.isEmpty()) {
			try {
				double maxPrice = Double.parseDouble(maxPriceStr);
				filter.setMaxPrice(maxPrice);
			} catch (NumberFormatException e) {
				System.out.println("Invalid number format. Using default maximum price.");
			}
		}
		
		// Get filtered projects
		ArrayList<Project> filteredProjects = officerController.getFilteredProjects(currentOfficer, filter);
		
		System.out.println("\n===== FILTERED PROJECTS =====");
		if (filteredProjects.isEmpty()) {
			System.out.println("No projects match your filter criteria.");
		} else {
			displayProjects(filteredProjects);
		}
	}

	/**
	 * Displays only the projects that the officer is assigned to.
	 * Shows a list of assigned projects and allows detailed view of selected projects.
	 */
	private void viewAssignedProjects() {
		System.out.println("\n===== MY ASSIGNED PROJECTS =====");
		
		ArrayList<Project> assignedProjects = currentOfficer.getAssignedProjects();
		
		if (assignedProjects.isEmpty()) {
			System.out.println("You are not assigned to any project yet.");
			return;
		}
		
		System.out.println("\nYou are assigned to the following projects:");
		for (int i = 0; i < assignedProjects.size(); i++) {
			Project project = assignedProjects.get(i);
			System.out.println((i+1) + ". " + project.getProjectName() + " (" + project.getNeighborhood() + ")");
		}
		
		// Ask user to select a project for detailed view if there are multiple
		Project selectedProject;
		if (assignedProjects.size() > 1) {
			System.out.print("\nSelect a project for detailed view (or 0 to return): ");
			String input = scanner.nextLine().trim();
			
			if (input.equals("0")) {
				return;
			}
			
			int selection;
			try {
				selection = Integer.parseInt(input);
				if (selection < 1 || selection > assignedProjects.size()) {
					System.out.println("Invalid selection. Showing all projects instead.");
					for (Project project : assignedProjects) {
						displayProjectDetails(project);
					}
					return;
				}
				selectedProject = assignedProjects.get(selection - 1);
				displayProjectDetails(selectedProject);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Showing all projects instead.");
				for (Project project : assignedProjects) {
					displayProjectDetails(project);
				}
			}
		} else {
			// Only one project, display it directly
			displayProjectDetails(assignedProjects.get(0));
		}
	}

	/**
	 * Displays detailed information about a specific project.
	 * Shows project details, flat types, assigned officers, and application statistics.
	 * 
	 * @param project The project to display details for
	 */
	private void displayProjectDetails(Project project) {
		
		System.out.println("\n===== PROJECT: " + project.getProjectName() + " =====");
		System.out.println("Neighborhood: " + project.getNeighborhood());
		System.out.println("Application Period: " + project.getApplicationStartDate() + 
				" to " + project.getApplicationEndDate());
		System.out.println("Status: " + (project.isApplicationOpen() ? "OPEN" : "CLOSED"));
		System.out.println("Manager: " + (project.getManager() != null ? 
				project.getManager().getName() : "Unassigned"));
		
		// Display flat types
		System.out.println("\nFlat Types:");
		for (FlatType flatType : project.getFlatTypes()) {
			System.out.println(flatType.getType() + ": " + 
					flatType.getAvailableUnits() + "/" + flatType.getNumUnits() + 
					" units available, Price: $" + flatType.getSellingPrice());
		}
		
		// Display other assigned officers
		System.out.println("\nAssigned Officers:");
		if (project.getAssignedOfficers().size() <= 1) {
			System.out.println("No other officers assigned to this project.");
		} else {
			for (Officer officer : project.getAssignedOfficers()) {
				if (!officer.getNric().equals(currentOfficer.getNric())) {
					System.out.println("- " + officer.getName());
				}
			}
		}
		
		// Show application counts
		System.out.println("\nApplication Statistics:");
		ArrayList<BTOApplication> applications = officerController.getApplicationsByProject(project);
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
		
		System.out.println("Total Applications: " + applications.size());
		System.out.println("Pending: " + pendingCount);
		System.out.println("Successful: " + successfulCount);
		System.out.println("Booked: " + bookedCount);
		System.out.println("Unsuccessful: " + unsuccessfulCount);
		System.out.println("\n---------------------------------------------------");
	}

	/**
	 * Formats and displays a list of projects with their basic details.
	 * 
	 * @param projects The list of projects to display
	 */
	private void displayProjects(ArrayList<Project> projects) {
		System.out.println("\n------------------------------------------------------");
		System.out.printf("%-15s | %-15s | %-10s | %-15s\n", 
				"Project Name", "Neighborhood", "Status", "Application Period");
		System.out.println("------------------------------------------------------");
		
		for (Project project : projects) {
			String status = project.isApplicationOpen() ? "OPEN" : "CLOSED";
			String period = project.getApplicationStartDate() + " to " + project.getApplicationEndDate();
			
			System.out.printf("%-15s | %-15s | %-10s | %-15s\n", 
					project.getProjectName(), project.getNeighborhood(), status, period);
			
			// Display flat types available
			System.out.println("  Available Flat Types:");
			for (FlatType flatType : project.getFlatTypes()) {
				System.out.printf("    %-10s: %d units available, Price: $%.2f\n", 
						flatType.getType(), flatType.getAvailableUnits(), flatType.getSellingPrice());
			}
			
			System.out.println("------------------------------------------------------");
		}
	}

	/**
	 * Sets the current officer for this view.
	 * 
	 * @param currentOfficer The Officer entity whose projects are being viewed
	 */
	public void setCurrentOfficer(Officer currentOfficer) {
		this.currentOfficer = currentOfficer;
	}

	/**
	 * Sets the controller instance for this view.
	 * 
	 * @param officerController The OfficerController to handle business logic
	 */
	public void setOfficerController(OfficerController officerController) {
		this.officerController = officerController;
	}

	/**
	 * Gets the current officer entity.
	 * 
	 * @return The Officer entity whose projects are being viewed
	 */
	public Officer getCurrentOfficer() {
		return this.currentOfficer;
	}

	/**
	 * Gets the current officer controller.
	 * 
	 * @return The OfficerController instance used by this view
	 */
	public OfficerController getOfficerController() {
		return this.officerController;
	}
}