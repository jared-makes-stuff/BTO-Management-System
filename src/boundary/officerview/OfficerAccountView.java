package boundary.officerview;

import controller.*;
import entity.*;
import enums.*;
import interfaces.*;
import java.util.Scanner;

/**
 * View class for managing an Officer's account information and settings.
 * Provides functionality for viewing and updating profile details and changing passwords.
 * Implements the IDisplayable interface to provide a user interface for account management.
 */
public class OfficerAccountView implements IDisplayable {

	private Scanner scanner;
	private Officer currentOfficer;
	/**
	 * Instance variables for controller access
	 */
	private OfficerController officerController;
	
	/**
	 * Constructor with parameters to initialize the view with a specific officer.
	 * 
	 * @param officer The Officer entity whose account is being managed
	 */
	public OfficerAccountView(Officer officer) {
		this.scanner = new Scanner(System.in);
		this.officerController = new OfficerController();
		this.currentOfficer = officer;
	}
	
	/**
	 * Displays the account management menu and handles user interaction.
	 * Provides options for viewing profile, updating information, and changing password.
	 */
	@Override
	public void display() {
		boolean exit = false;
		
		while (!exit) {
			System.out.println("\n===== MANAGE ACCOUNT =====");
			System.out.println("1. View Profile");
			System.out.println("2. Edit Profile");
			System.out.println("3. Change Password");
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
				viewProfile();
				return false;
			case "2":
				editProfile();
				return false;
			case "3":
				changePassword();
				return false;
			case "0":
				return true;
			default:
				System.out.println("Invalid choice. Please try again.");
				return false;
		}
	}
	
	/**
	 * Displays the current officer's profile information including
	 * personal details and list of projects being assigned to.
	 */
	private void viewProfile() {
		System.out.println("\n===== YOUR PROFILE =====");
		System.out.println("Name: " + currentOfficer.getName());
		System.out.println("NRIC: " + currentOfficer.getNric());
		System.out.println("Age: " + currentOfficer.getAge());
		System.out.println("Marital Status: " + currentOfficer.getMaritalStatus());
		
		if (currentOfficer.getAssignedProjects() != null) {
			for (Project project : currentOfficer.getAssignedProjects()) {
				System.out.println("Assigned Projects: " + project.getProjectName());
			}
		} else {
			System.out.println("Assigned Project: None");
		}
	}
	
	/**
	 * Allows the officer to update their profile information including
	 * name, age, and marital status. Changes are saved through the controller.
	 */
	private void editProfile() {
		System.out.println("\n===== EDIT PROFILE =====");
		System.out.println("Current Profile:");
		System.out.println("1. Name: " + currentOfficer.getName());
		System.out.println("2. Age: " + currentOfficer.getAge());
		System.out.println("3. Marital Status: " + currentOfficer.getMaritalStatus());
		System.out.println("0. Cancel");
		System.out.print("Select field to edit: ");
		
		String selection = scanner.nextLine();
		
		switch (selection) {
			case "1":
				System.out.print("Enter new name: ");
				String newName = scanner.nextLine();
				if (!newName.isEmpty()) {
					currentOfficer.setName(newName);
					System.out.println("Name updated successfully.");
				}
				break;
			case "2":
				System.out.print("Enter new age: ");
				try {
					int newAge = Integer.parseInt(scanner.nextLine());
					if (newAge > 0) {
						currentOfficer.setAge(newAge);
						System.out.println("Age updated successfully.");
					} else {
						System.out.println("Invalid age. Must be greater than 0.");
					}
				} catch (NumberFormatException e) {
					System.out.println("Invalid input. Please enter a number.");
				}
				break;
			case "3":
				System.out.println("Current marital status: " + currentOfficer.getMaritalStatus());
				System.out.println("1. SINGLE");
				System.out.println("2. MARRIED");
				System.out.print("Select new marital status: ");
				String maritalSelection = scanner.nextLine();
				
				switch (maritalSelection) {
					case "1":
						currentOfficer.setMaritalStatus(MarriageStatusEnum.SINGLE);
						System.out.println("Marital status updated to SINGLE.");
						break;
					case "2":
						currentOfficer.setMaritalStatus(MarriageStatusEnum.MARRIED);
						System.out.println("Marital status updated to MARRIED.");
						break;
					default:
						System.out.println("Invalid selection. Marital status not updated.");
				}
				break;
			case "0":
				System.out.println("Edit cancelled.");
				break;
			default:
				System.out.println("Invalid selection.");
		}
		
		// Update the officer in the database
		if (!selection.equals("0") && !selection.equals("")) {
			officerController.editProfile(currentOfficer, 
					currentOfficer.getName(),
					currentOfficer.getNric(),
					currentOfficer.getAge(), 
					currentOfficer.getMaritalStatus());
		}
	}
	
	/**
	 * Provides functionality for the officer to change their password.
	 * Verifies current password and validates the new password before updating.
	 */
	private void changePassword() {
		System.out.println("\n===== CHANGE PASSWORD =====");
		System.out.print("Enter current password: ");
		String currentPassword = scanner.nextLine();
		
		if (!officerController.verifyPassword(currentOfficer, currentPassword)) {
			System.out.println("Incorrect password.");
			return;
		}
		
		System.out.print("Enter new password: ");
		String newPassword = scanner.nextLine();
		
		if (!OfficerController.isValidPassword(newPassword)) {
			System.out.println("Invalid password. Password must be at least 8 characters long.");
			return;
		}
		
		System.out.print("Confirm new password: ");
		String confirmPassword = scanner.nextLine();
		
		if (!newPassword.equals(confirmPassword)) {
			System.out.println("Passwords do not match.");
			return;
		}
		
		boolean success = officerController.changePassword(currentOfficer, currentPassword, newPassword);
		
		if (success) {
			System.out.println("Password changed successfully.");
		} else {
			System.out.println("Failed to change password.");
		}
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
	 * @return The Officer entity whose account is being managed
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