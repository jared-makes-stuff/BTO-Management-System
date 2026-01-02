package boundary.managerview;

import controller.*;
import entity.*;
import enums.*;
import interfaces.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * View class for managers to browse and filter projects in the system.
 * Provides functionality for viewing all projects, applying filters,
 * and specifically viewing projects managed by the current manager.
 * Implements the IDisplayable interface to provide a user interface.
 */
public class ManagerProjectsView implements IDisplayable {

    private Scanner scanner;
    private Manager currentManager;
    private ManagerController managerController;

    /**
     * Constructor with parameters to initialize the view with a specific manager
     * 
     * @param manager The Manager entity who is currently logged in
     */
    public ManagerProjectsView(Manager manager) {
        this.scanner = new Scanner(System.in);
        this.managerController = new ManagerController();
        this.currentManager = manager;
    }

    /**
     * Displays the projects menu and handles user interaction.
     * Provides options for viewing all projects, filtered projects, and manager's own projects.
     */
    @Override
    public void display() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n===== VIEW PROJECTS =====");
            System.out.println("1. View All Projects");
            System.out.println("2. View Projects with Filter");
            System.out.println("3. View My Projects");
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
                viewAllProjects();
                return false;
            case "2":
                viewProjectsWithFilter();
                return false;
            case "3":
                viewMyProjects();
                return false;
            case "0":
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
    }

    /**
     * Displays a list of all projects in the system.
     * Retrieves projects through the manager controller and displays them.
     */
    private void viewAllProjects() {
        System.out.println("\n===== ALL PROJECTS =====");
        ArrayList<Project> allProjects = managerController.getAllProjects();
        
        if (allProjects.isEmpty()) {
            System.out.println("No projects available at this time.");
        } else {
            displayProjects(allProjects);
        }
    }

    /**
     * Allows the manager to filter projects based on various criteria.
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
        ArrayList<Project> filteredProjects = managerController.getFilteredProjects(filter);
        
        System.out.println("\n===== FILTERED PROJECTS =====");
        if (filteredProjects.isEmpty()) {
            System.out.println("No projects match your filter criteria.");
        } else {
            displayProjects(filteredProjects);
        }
    }

    /**
     * Displays only the projects that are managed by the current manager.
     * Retrieves the manager's projects through the controller and displays them.
     */
    private void viewMyProjects() {
        System.out.println("\n===== MY PROJECTS =====");
        ArrayList<Project> managedProjects = managerController.getManagedProjects(currentManager);
        
        if (managedProjects.isEmpty()) {
            System.out.println("You are not managing any projects yet.");
        } else {
            displayProjects(managedProjects);
        }
    }

    /**
     * Formats and displays a list of projects with their details.
     * Shows project name, neighborhood, visibility, application period, manager,
     * and available flat types with pricing information.
     * 
     * @param projects The list of projects to display
     */
    private void displayProjects(ArrayList<Project> projects) {
        System.out.println("\n----------------------------------------------------------------");
        System.out.printf("%-20s | %-15s | %-10s | %-15s | %-10s\n", 
                "Project Name", "Neighborhood", "Visibility", "Application Period", "Manager");
        System.out.println("----------------------------------------------------------------");
        
        for (Project project : projects) {
            String visibility = project.getVisibility() == VisibilityEnum.VISIBLE ? "Visible" : "Hidden";
            String period = project.getApplicationStartDate() + " to " + project.getApplicationEndDate();
            String managerName = project.getManager() != null ? project.getManager().getName() : "None";
            
            System.out.printf("%-20s | %-15s | %-10s | %-15s | %-10s\n", 
                    project.getProjectName(), 
                    project.getNeighborhood(), 
                    visibility, 
                    period, 
                    managerName);
            
            // Display flat types available
            for (FlatType flatType : project.getFlatTypes()) {
                System.out.printf("  %-10s: %d units available (Total: %d), Price: $%.2f\n", 
                        flatType.getType(), flatType.getAvailableUnits(), flatType.getNumUnits(), flatType.getSellingPrice());
            }
            
            System.out.println("----------------------------------------------------------------");
        }
    }

    /**
     * Sets the manager entity for this view.
     * 
     * @param currentManager The Manager whose projects are being viewed
     */
    public void setCurrentManager(Manager currentManager) {
        this.currentManager = currentManager;
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
     * Gets the current manager entity.
     * 
     * @return The Manager entity whose projects are being viewed
     */
    public Manager getCurrentManager() {
        return this.currentManager;
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
     * Default constructor that initializes the view with a scanner and controller.
     * Does not set a current manager, which should be done later with setCurrentManager().
     */
    public ManagerProjectsView() {
        this.scanner = new Scanner(System.in);
        this.managerController = new ManagerController();
    }
}