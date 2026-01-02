package boundary.applicantview;

import entity.*;
import interfaces.*;
import utils.DisplayMenu;
import java.util.Scanner;

/**
 * Boundary class for managing applicant interactions with the BTO Management System.
 * 
 * This class serves as the main entry point for applicants to interact with the system.
 * It provides a menu-driven interface for accessing different applicant functionalities
 * such as viewing projects, managing applications, handling enquiries, and updating profile details.
 * 
 * The class delegates specific functionalities to specialized sub-views to maintain
 * separation of concerns and modularity.
 */
public class ApplicantView implements IDisplayable {
	
	/**
	 * Reference to the currently logged-in applicant
	 */
	private Applicant currentApplicant;
	
	/**
	 * Constructor that initializes a new ApplicantView with the logged in user
	 * and controller but no applicant yet.
	 */
	public ApplicantView(Applicant currentApplicant) {
		this.currentApplicant = currentApplicant;
	}

	/**
	 * Sets the currently logged-in applicant for this view.
	 * This should be called immediately after successful login.
	 * 
	 * @param currentApplicant The logged-in applicant whose session is being managed
	 */
	public void setCurrentApplicant(Applicant currentApplicant) {
		this.currentApplicant = currentApplicant;
	}

	/**
	 * Gets the currently logged-in applicant.
	 * 
	 * @return The current applicant using the system
	 */
	public Applicant getCurrentApplicant() {
		return this.currentApplicant;
	}

	/**
	 * Displays the main menu for applicants and processes user selections.
	 * Provides a loop that continues until the user chooses to exit.
	 */
	@Override
	public void display() {
		Scanner scanner = new Scanner(System.in);
		boolean exit = false;
		
		while (!exit) {
			System.out.println("\n===== APPLICANT MENU =====");
			System.out.println("Welcome, " + currentApplicant.getName() + "!");
			System.out.println("\n1. View Projects");
			System.out.println("2. Manage BTO Applications");
			System.out.println("3. Manage Enquiries");
			System.out.println("4. Manage Account");
			System.out.println("0. Logout");
			System.out.print("Enter your choice: ");
			
			String input = scanner.nextLine();
			exit = getUserInput(input);
		}
	}

	/**
	 * Processes the user's menu selection and navigates to the appropriate sub-view.
	 * 
	 * @param input The user's menu choice
	 * @return true if the user wants to exit, false otherwise
	 */
	private boolean getUserInput(String input) {
		switch (input) {
			case "1":
				DisplayMenu.displayMenu(new ApplicantProjectsView(currentApplicant));
				return false;
			case "2":
				DisplayMenu.displayMenu(new ApplicantApplicationsView(currentApplicant));
				return false;
			case "3":
				DisplayMenu.displayMenu(new ApplicantEnquiriesView(currentApplicant));
				return false;
			case "4":
				DisplayMenu.displayMenu(new ApplicantAccountView(currentApplicant));
				return false;
			case "0":
				System.out.println("Logging out...");
				return true;
			default:
				System.out.println("Invalid choice. Please try again.");
				return false;
		}
	}
}