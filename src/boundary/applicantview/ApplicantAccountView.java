package boundary.applicantview;

import controller.*;
import entity.*;
import enums.*;
import interfaces.*;
import java.util.Scanner;

/**
 * Boundary class for managing an applicant's account profile and settings.
 * 
 * This class provides functionality for applicants to view and update their personal
 * information, including profile details and authentication credentials. It serves
 * as a specialized view that handles all account-related operations for applicants
 * in the BTO Management System.
 */
public class ApplicantAccountView implements IDisplayable {

    /**
     * Scanner for reading user input from the console
     */
    private Scanner scanner;
    
    /**
     * Reference to the currently logged-in applicant
     */
    private Applicant currentApplicant;
    
    /**
     * Controller that handles business logic for applicant actions
     */
    private ApplicantController applicantController;

    /**
     * Constructs an ApplicantAccountView with a specific applicant
     * 
     * @param applicant The applicant whose account will be managed
     */
    public ApplicantAccountView(Applicant applicant) {
        this.scanner = new Scanner(System.in);
        this.currentApplicant = applicant;
        this.applicantController = new ApplicantController();
    }

    /**
     * Displays the account management menu and processes user selections.
     * Provides options for viewing and editing profile information and changing password.
     */
    @Override
    public void display() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n===== MANAGE ACCOUNT =====");
            System.out.println("1. View Profile");
            System.out.println("2. Edit Profile");
            System.out.println("3. Change Password");
            System.out.println("0. Return to Applicant Menu");
            System.out.print("Enter your choice: ");
            
            String input = scanner.nextLine();
            exit = getUserInput(input);
        }
    }

    /**
     * Processes the user's menu selection and executes the appropriate account management function.
     * 
     * @param input The user's menu choice
     * @return true if the user wants to exit to the main menu, false otherwise
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
     * Displays the current applicant's profile information, including
     * NRIC, name, age, and marital status.
     */
    private void viewProfile() {
        System.out.println("\n===== MY PROFILE =====");
        System.out.println("NRIC: " + currentApplicant.getNric());
        System.out.println("Name: " + currentApplicant.getName());
        System.out.println("Age: " + currentApplicant.getAge());
        System.out.println("Marital Status: " + currentApplicant.getMaritalStatus());
    }

    /**
     * Allows the applicant to update their profile information.
     * Profile updates include name, age, and marital status.
     * For each field, the applicant can choose to keep the current value.
     */
    private void editProfile() {
        System.out.println("\n===== EDIT PROFILE =====");
        System.out.println("Current Profile:");
        System.out.println("Name: " + currentApplicant.getName());
        System.out.println("Age: " + currentApplicant.getAge());
        System.out.println("Marital Status: " + currentApplicant.getMaritalStatus());
        
        // Get new values
        System.out.println("\nEnter new values (leave blank to keep current):");
        
        System.out.print("Name [" + currentApplicant.getName() + "]: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Age [" + currentApplicant.getAge() + "]: ");
        String ageStr = scanner.nextLine().trim();
        
        System.out.print("Marital Status [" + currentApplicant.getMaritalStatus() + "] (SINGLE/MARRIED): ");
        String maritalStatusStr = scanner.nextLine().trim().toUpperCase();
        
        // Update applicant information
        if (!name.isEmpty()) {
            currentApplicant.setName(name);
        }
        
        if (!ageStr.isEmpty()) {
            try {
                int age = Integer.parseInt(ageStr);
                currentApplicant.setAge(age);
            } catch (NumberFormatException e) {
                System.out.println("Invalid age format. Age not updated.");
            }
        }
        
        if (!maritalStatusStr.isEmpty()) {
            try {
                MarriageStatusEnum maritalStatus = MarriageStatusEnum.valueOf(maritalStatusStr);
                currentApplicant.setMaritalStatus(maritalStatus);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid marital status. Marital status not updated.");
            }
        }
        
        System.out.println("Profile updated successfully!");
    }

    /**
     * Allows the applicant to change their account password.
     * The process includes verification of the current password and validation
     * of the new password's strength and confirmation matching.
     */
    private void changePassword() {
        System.out.println("\n===== CHANGE PASSWORD =====");
        
        // Get current password for verification
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();
        
        // Verify current password
        if (!currentApplicant.authenticate(currentPassword)) {
            System.out.println("Incorrect password. Password not changed.");
            return;
        }
        
        // Get new password
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        
        // Validate password strength
        if (!ApplicantController.isValidPassword(newPassword)) {
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
        boolean success = applicantController.changePassword(currentApplicant, newPassword);
        
        if (success) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Failed to change password.");
        }
    }

    /**
     * Sets the current applicant whose account is being managed.
     * 
     * @param currentApplicant The applicant whose account will be managed
     */
    public void setCurrentApplicant(Applicant currentApplicant) {
        this.currentApplicant = currentApplicant;
    }

    /**
     * Sets the controller for handling business logic related to account operations.
     * 
     * @param applicantController The controller instance to use for this view
     */
    public void setApplicantController(ApplicantController applicantController) {
        this.applicantController = applicantController;
    }

    /**
     * Gets the currently managed applicant.
     * 
     * @return The current applicant using the account management features
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
     * Default constructor that initializes a new ApplicantAccountView with a Scanner
     * and controller but no applicant yet.
     */
    public ApplicantAccountView() {
        this.scanner = new Scanner(System.in);
        this.applicantController = new ApplicantController();
    }
}