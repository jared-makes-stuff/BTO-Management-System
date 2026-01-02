package boundary.officerview;

import entity.*;
import interfaces.*;
import utils.DisplayMenu;
import java.util.Scanner;

/**
 * Boundary class for managing officer interactions with the BTO Management System.
 * Provides the main interface for officers to navigate between different functionalities
 * including project viewing, application management, enquiry handling, and account settings.
 */
public class OfficerView implements IDisplayable {

	private Officer currentOfficer;

	/**
	 * Constructor that initializes a new OfficerView with the logged in user.
	 * 
	 * @param currentOfficer The Officer entity who is currently logged in
	 */
	public OfficerView(Officer currentOfficer) {
		this.currentOfficer = currentOfficer;
	}


	/**
	 * Sets the current officer for this view.
	 * 
	 * @param currentOfficer The Officer entity who is currently logged in
	 */
	public void setCurrentOfficer(Officer currentOfficer) {
		this.currentOfficer = currentOfficer;
	}

	/**
	 * Gets the current officer entity.
	 * 
	 * @return The Officer entity who is currently logged in
	 */
	public Officer getCurrentOfficer() {
		return this.currentOfficer;
	}

	/**
	 * Displays the officer main menu and handles user interaction.
	 * Provides navigation to various officer functionalities through sub-views.
	 */
	@Override
	public void display() {
		Scanner scanner = new Scanner(System.in);
		boolean exit = false;
		
		while (!exit) {
			System.out.println("\n===== OFFICER MENU =====");
			System.out.println("Welcome, " + currentOfficer.getName() + "!");
			System.out.println("\n1. View Projects");
			System.out.println("2. Manage BTO Applications (as Applicant)");
			System.out.println("3. Manage Enquiries (as Applicant)");
			System.out.println("4. Manage Project Officer Registration");
			System.out.println("5. Handle Project");
			System.out.println("6. Handle Enquiries");
			System.out.println("7. Manage Account");
			System.out.println("0. Logout");
			System.out.print("Enter your choice: ");
			
			String input = scanner.nextLine();
			exit = getUserInput(input);
		}
	}

	/**
	 * Processes the user's menu selection and navigates to the appropriate sub-view.
	 * 
	 * @param input The user's menu choice as a string
	 * @return true if the user wants to logout, false to stay in the menu
	 */
	private boolean getUserInput(String input) {
		switch (input) {
			case "1":
				DisplayMenu.displayMenu(new OfficerProjectsView(currentOfficer));
				return false;
			case "2":
				DisplayMenu.displayMenu(new OfficerApplicationsView(currentOfficer));
				return false;
			case "3":
				DisplayMenu.displayMenu(new OfficerEnquiriesView(currentOfficer));
				return false;
			case "4":
				DisplayMenu.displayMenu(new OfficerRegistrationView(currentOfficer));
				return false;
			case "5":
				DisplayMenu.displayMenu(new OfficerHandleProjectView(currentOfficer));
				return false;
			case "6":
				DisplayMenu.displayMenu(new OfficerHandleEnquiriesView(currentOfficer));
				return false;
			case "7":
				DisplayMenu.displayMenu(new OfficerAccountView(currentOfficer));
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