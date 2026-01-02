package boundary.managerview;

import controller.*;
import entity.*;
import enums.*;
import interfaces.*;
import java.util.Scanner;

/**
 * View class for managing a Manager's account information and settings.
 * Provides functionality for viewing and updating profile details and changing passwords.
 * Implements the IDisplayable interface to provide a user interface for account management.
 */
public class ManagerAccountView implements IDisplayable {

	private Scanner scanner;
	/**
	 * Instance variables
	 */
	private ManagerController managerController;
	private Manager currentManager;

	/**
	 * Constructor with parameters to initialize the view with a specific manager
	 * 
	 * @param manager The Manager entity whose account is being managed
	 */
	public ManagerAccountView(Manager manager) {
        this.scanner = new Scanner(System.in);
        this.managerController = new ManagerController();
        this.currentManager = manager;
	}

	/**
	 * Displays the account management menu and handles user interaction.
	 * Provides options for viewing profile, updating information, and changing password.
	 */
	@Override
	public void display() {
		boolean exit = false;
		
		while (!exit) {
			System.out.println("\n===== ACCOUNT MANAGEMENT =====");
			System.out.println("1. View My Profile");
			System.out.println("2. Update Profile");
			System.out.println("3. Change Password");
			System.out.println("0. Return to Manager Menu");
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
	 * Displays the current manager's profile information including
	 * personal details and list of projects being managed.
	 */
	private void viewProfile() {
		System.out.println("\n===== MY PROFILE =====");
		System.out.println("NRIC: " + currentManager.getNric());
		System.out.println("Name: " + currentManager.getName());
		System.out.println("Age: " + currentManager.getAge());
		System.out.println("Marital Status: " + currentManager.getMaritalStatus());
		
		// Display managed projects
		System.out.println("\nManaged Projects:");
		if (currentManager.getManagedProjects() != null && !currentManager.getManagedProjects().isEmpty()) {
			for (Project project : currentManager.getManagedProjects()) {
				System.out.println("- " + project.getProjectName() + " (" + project.getNeighborhood() + ")");
			}
		} else {
			System.out.println("You are not managing any projects currently.");
		}
	}

	/**
	 * Allows the manager to update their profile information including
	 * name, age, and marital status. Changes are saved through the controller.
	 */
	private void editProfile() {
		System.out.println("\n===== UPDATE PROFILE =====");
		System.out.println("Current Profile:");
		System.out.println("Name: " + currentManager.getName());
		System.out.println("Age: " + currentManager.getAge());
		System.out.println("Marital Status: " + currentManager.getMaritalStatus());
		
		// Get new values
		System.out.println("\nEnter new values (leave blank to keep current):");
		
		System.out.print("Name [" + currentManager.getName() + "]: ");
		String name = scanner.nextLine().trim();
		
		System.out.print("Age [" + currentManager.getAge() + "]: ");
		String ageStr = scanner.nextLine().trim();
		
		System.out.print("Marital Status [" + currentManager.getMaritalStatus() + "] (SINGLE/MARRIED): ");
		String maritalStatusStr = scanner.nextLine().trim().toUpperCase();
		
		// Update manager information
		if (!name.isEmpty()) {
			currentManager.setName(name);
		}
		
		if (!ageStr.isEmpty()) {
			try {
				int age = Integer.parseInt(ageStr);
				currentManager.setAge(age);
			} catch (NumberFormatException e) {
				System.out.println("Invalid age format. Age not updated.");
			}
		}
		
		if (!maritalStatusStr.isEmpty()) {
			try {
				MarriageStatusEnum maritalStatus = MarriageStatusEnum.valueOf(maritalStatusStr);
				currentManager.setMaritalStatus(maritalStatus);
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid marital status. Marital status not updated.");
			}
		}
		
		// Save changes
		managerController.editProfile(currentManager, currentManager.getName(), currentManager.getNric(), currentManager.getAge(), currentManager.getMaritalStatus());
		
		System.out.println("Profile updated successfully!");
	}

	/**
	 * Provides functionality for the manager to change their password.
	 * Verifies current password and validates the new password before updating.
	 */
	private void changePassword() {
		System.out.println("\n===== CHANGE PASSWORD =====");
		
		// Get current password for verification
		System.out.print("Enter current password: ");
		String currentPassword = scanner.nextLine();
		
		// Verify current password
		if (!managerController.verifyPassword(currentManager, currentPassword)) {
			System.out.println("Incorrect password. Password not changed.");
			return;
		}
		
		// Get new password
		System.out.print("Enter new password: ");
		String newPassword = scanner.nextLine();
		
		// Validate password strength
		if (!ManagerController.isValidPassword(newPassword)) {
			System.out.println("Password must be at least 8 characters long.");
			return;
		}
		
		// Confirm new password
		System.out.print("Confirm new password: ");
		String confirmPassword = scanner.nextLine();
		
		if (!newPassword.equals(confirmPassword)) {
			System.out.println("Passwords do not match. Password not changed.");
			return;
		}
		
		// Update password
		boolean success = managerController.changePassword(currentManager, currentPassword, newPassword);
		
		if (success) {
			System.out.println("Password changed successfully!");
		} else {
			System.out.println("Failed to change password.");
		}
	}

	/**
	 * Sets the controller instance for this view.
	 * 
	 * @param managerController The ManagerController to handle business logic
	 */
	public void setManagerController(ManagerController managerController) {
		this.managerController = managerController;
	}

	/**
	 * Sets the manager entity for this view.
	 * 
	 * @param currentManager The Manager whose account is being managed
	 */
	public void setCurrentManager(Manager currentManager) {
		this.currentManager = currentManager;
	}

	/**
	 * Gets the current manager controller.
	 * 
	 * @return The ManagerController instance used by this view
	 */
	public ManagerController getManagerController() {
		return this.managerController;
	}

	/**
	 * Gets the current manager entity.
	 * 
	 * @return The Manager entity whose account is being managed
	 */
	public Manager getCurrentManager() {
		return this.currentManager;
	}

	/**
	 * Default constructor that initializes the view with a new scanner and controller.
	 * Does not set a current manager, which should be done later with setCurrentManager().
	 */
	public ManagerAccountView() {
		this.scanner = new Scanner(System.in);
		this.managerController = new ManagerController();
	}
}