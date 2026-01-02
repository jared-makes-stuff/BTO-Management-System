package boundary.applicantview;

import controller.*;
import entity.*;
import interfaces.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Boundary class for managing applicant interactions with housing projects.
 * 
 * This class provides functionality for applicants to view available housing projects,
 * filter projects based on their preferences, and view projects they have already applied for.
 * It serves as a specialized view that handles all project browsing operations for applicants
 * in the BTO Management System.
 */
public class ApplicantProjectsView implements IDisplayable {

	/**
	 * Core components needed for project interaction
	 */
	private Scanner scanner;
	private Applicant currentApplicant;
	private ApplicantController applicantController;

	/**
	 * Constructs an ApplicantProjectsView with a specific applicant
	 * 
	 * @param applicant The applicant who will be browsing projects
	 */
	public ApplicantProjectsView(Applicant applicant) {
		this.scanner = new Scanner(System.in);
		this.applicantController = new ApplicantController();
		this.currentApplicant = applicant;
	}

	/**
	 * Displays the project browsing menu and processes user selections.
	 * Provides options for viewing all projects, applying filters, and viewing applied projects.
	 */
	@Override
	public void display() {
		boolean exit = false;
		
		while (!exit) {
			System.out.println("\n===== VIEW PROJECTS =====");
			System.out.println("1. View All Projects");
			System.out.println("2. View Projects with Filter");
			System.out.println("3. View My Applied Projects");
			System.out.println("0. Return to Applicant Menu");
			System.out.print("Enter your choice: ");
			
			String input = scanner.nextLine();
			exit = getUserInput(input);
		}
	}

	/**
	 * Processes the user's menu selection and executes the appropriate project browsing function.
	 * 
	 * @param input The user's menu choice
	 * @return true if the user wants to exit to the main menu, false otherwise
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
				viewMyAppliedProjects();
				return false;
			case "0":
				return true;
			default:
				System.out.println("Invalid choice. Please try again.");
				return false;
		}
	}

	/**
	 * Displays all projects that the current applicant is eligible to apply for.
	 * Eligibility is determined based on various factors such as marital status,
	 * age, and application period status.
	 */
	private void viewAllProjects() {
		System.out.println("\n===== ALL AVAILABLE PROJECTS =====");
		ArrayList<Project> eligibleProjects = applicantController.getEligibleProjects(currentApplicant);
		
		if (eligibleProjects.isEmpty()) {
			System.out.println("No eligible projects available at this time.");
		} else {
			displayProjects(eligibleProjects);
		}
	}

	/**
	 * Allows the applicant to filter projects based on various criteria such as
	 * project name, neighborhood, and price range. The applicant creates a filter
	 * which is then used to find matching projects.
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
		ArrayList<Project> filteredProjects = applicantController.getFilteredProjects(currentApplicant, filter);
		
		System.out.println("\n===== FILTERED PROJECTS =====");
		if (filteredProjects.isEmpty()) {
			System.out.println("No projects match your filter criteria.");
		} else {
			displayProjects(filteredProjects);
		}
	}

	/**
	 * Displays all projects that the current applicant has already applied for.
	 * This allows applicants to easily track and review their applications.
	 */
	private void viewMyAppliedProjects() {
		System.out.println("\n===== MY APPLIED PROJECTS =====");
		ArrayList<Project> appliedProjects = applicantController.getAppliedProjects(currentApplicant);
		
		if (appliedProjects.isEmpty()) {
			System.out.println("You haven't applied for any projects yet.");
		} else {
			displayProjects(appliedProjects);
		}
	}

	/**
	 * Displays a formatted list of projects with their key details.
	 * For each project, shows the name, neighborhood, application status, and period.
	 * Also displays detailed information about the flat types available in each project.
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
	 * Sets the current applicant who will be browsing projects.
	 * 
	 * @param currentApplicant The applicant who will use the project view
	 */
	public void setCurrentApplicant(Applicant currentApplicant) {
		this.currentApplicant = currentApplicant;
	}

	/**
	 * Sets the controller for handling business logic related to project operations.
	 * 
	 * @param applicantController The controller instance to use for this view
	 */
	public void setApplicantController(ApplicantController applicantController) {
		this.applicantController = applicantController;
	}

	/**
	 * Gets the currently active applicant.
	 * 
	 * @return The current applicant using the project browsing features
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
}