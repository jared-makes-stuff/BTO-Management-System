package boundary.managerview;

import entity.*;
import interfaces.*;
import utils.DisplayMenu;

import java.util.Scanner;

/**
 * Boundary class for managing manager interactions with the BTO Management System.
 * This class serves as the main entry point for manager users to access various 
 * system functionalities such as project management, application processing,
 * officer registration approval, report generation, enquiry handling, and account management.
 * 
 * The class follows the MVC architecture by coordinating with a ManagerController
 * to handle business logic while providing a menu-driven user interface.
 * It delegates specific functionality to specialized sub-views for better organization.
 * 
 * @implements IDisplayable Interface for standardized display functionality
 */
public class ManagerView implements IDisplayable {

    private Manager currentManager;

    /**
     * Constructor that initializes a new ManagerView with the logged in user.
     * 
     * @param currentManager The Manager entity who is currently logged in
     */
    public ManagerView(Manager currentManager) {
        this.currentManager = currentManager;
    }

    /**
     * Sets the current manager for this view.
     * This must be called before using the view to establish the user context.
     * 
     * @param currentManager The Manager entity who is currently logged in
     */
    public void setCurrentManager(Manager currentManager) {
        this.currentManager = currentManager;
    }

    /**
     * Gets the current manager entity.
     * 
     * @return The Manager entity who is currently logged in
     */
    public Manager getCurrentManager() {
        return this.currentManager;
    }

    /**
     * Methods
     */
    /**
     * Displays the manager main menu and handles user interaction.
     * Provides navigation to various manager functionalities through sub-views.
     * 
     * This is the primary entry point for this view as required by the IDisplayable interface.
     * The method runs in a loop until the user chooses to logout by selecting option 0.
     * Each menu option directs the user to a specific sub-view for more detailed functionality.
     */
    @Override
    public void display() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n===== MANAGER MENU =====");
            System.out.println("Welcome, " + currentManager.getName() + "!");
            System.out.println("\n1. View Projects");
            System.out.println("2. Manage Projects");
            System.out.println("3. Process Applications");
            System.out.println("4. Manage Officer Registrations");
            System.out.println("5. Generate Reports");
            System.out.println("6. Manage Enquiries");
            System.out.println("7. Manage Account");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
            
            String input = scanner.nextLine();
            exit = getUserInput(input);
        }
    }

    /**
     * Processes the user's menu selection and navigates to the appropriate sub-view.
     * Routes user input to the corresponding functionality based on their menu choice.
     * 
     * @param input The user's menu choice as a string
     * @return true if the user wants to logout, false to stay in the menu
     */
    private boolean getUserInput(String input) {
        switch (input) {
            case "1":
            	DisplayMenu.displayMenu(new ManagerProjectsView(currentManager));
                return false;
            case "2":
                DisplayMenu.displayMenu(new ManagerManageProjectsView(currentManager));
                return false;
            case "3":
                DisplayMenu.displayMenu(new ManagerApplicationsView(currentManager));
                return false;
            case "4":
                DisplayMenu.displayMenu(new ManagerOfficerApplicationsView(currentManager));
                return false;
            case "5":
                DisplayMenu.displayMenu(new ManagerReportsView(currentManager));
                return false;
            case "6":
                DisplayMenu.displayMenu(new ManagerEnquiriesView(currentManager));
                return false;
            case "7":
                DisplayMenu.displayMenu(new ManagerAccountView(currentManager));
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