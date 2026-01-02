package boundary.managerview;

import controller.*;
import entity.*;
import enums.*;
import interfaces.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Boundary class for handling and displaying project management functionality for managers.
 * This view allows managers to create, edit, delete, and manage BTO projects,
 * including setting project details, managing flat types, and controlling project visibility.
 * 
 * @implements IDisplayable
 */
public class ManagerManageProjectsView implements IDisplayable {

    private Scanner scanner;
    private Manager currentManager;
    private ManagerController managerController;

    /**
     * Constructor that initializes the view with a specified manager
     * 
     * @param manager The Manager entity who will be using this view
     */
    public ManagerManageProjectsView(Manager manager) {
        this.scanner = new Scanner(System.in);
        this.managerController = new ManagerController();
        this.currentManager = manager;
    }

    /**
     * Displays the main menu for project management and handles user interaction.
     * Provides options to create, edit, delete, and view projects, 
     * toggle project visibility, or return to the main manager menu.
     */
    @Override
    public void display() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n===== MANAGE PROJECTS =====");
            System.out.println("1. Create New Project");
            System.out.println("2. Edit Project");
            System.out.println("3. Delete Project");
            System.out.println("4. Toggle Project Visibility");
            System.out.println("5. View All Projects");
            System.out.println("6. View My Projects");
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
                createNewProject();
                return false;
            case "2":
                editProject();
                return false;
            case "3":
                deleteProject();
                return false;
            case "4":
                toggleProjectVisibility();
                return false;
            case "5":
                viewAllProjects();
                return false;
            case "6":
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
     * Allows the manager to create a new BTO project with all required details.
     * Collects project name, neighborhood, application dates, officer slots,
     * flat types with their details, and visibility settings.
     */
    private void createNewProject() {
        System.out.println("\n===== CREATE NEW PROJECT =====");
        
        // Get project details
        System.out.print("Project Name: ");
        String projectName = scanner.nextLine().trim();
        
        if (projectName.isEmpty()) {
            System.out.println("Project name cannot be empty. Project creation cancelled.");
            return;
        }
        
        System.out.print("Neighborhood: ");
        String neighborhood = scanner.nextLine().trim();
        
        if (neighborhood.isEmpty()) {
            System.out.println("Neighborhood cannot be empty. Project creation cancelled.");
            return;
        }
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate applicationStartDate = null;
        LocalDate applicationEndDate = null;
        
        try {
            System.out.print("Application Start Date (YYYY-MM-DD): ");
            String startDateStr = scanner.nextLine().trim();
            applicationStartDate = LocalDate.parse(startDateStr, dateFormatter);
            
            System.out.print("Application End Date (YYYY-MM-DD): ");
            String endDateStr = scanner.nextLine().trim();
            applicationEndDate = LocalDate.parse(endDateStr, dateFormatter);
            
            if (applicationEndDate.isBefore(applicationStartDate)) {
                System.out.println("End date cannot be before start date. Project creation cancelled.");
                return;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Project creation cancelled.");
            return;
        }
        
        System.out.print("Number of Officer Slots (max 10): ");
        int officerSlots;
        try {
            officerSlots = Integer.parseInt(scanner.nextLine().trim());
            if (officerSlots < 1 || officerSlots > 10) {
                System.out.println("Officer slots must be between 1 and 10. Project creation cancelled.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Project creation cancelled.");
            return;
        }
        
        // Get flat types
        ArrayList<FlatType> flatTypes = new ArrayList<>();
        boolean addMoreFlatTypes = true;
        
        while (addMoreFlatTypes) {
            System.out.println("\nAdd Flat Type:");
            System.out.println("1. Two-Room");
            System.out.println("2. Three-Room");
            System.out.println("0. Finish Adding Flat Types");
            System.out.print("Enter your choice: ");
            
            String flatTypeChoice = scanner.nextLine().trim();
            
            if (flatTypeChoice.equals("0")) {
                if (flatTypes.isEmpty()) {
                    System.out.println("Project must have at least one flat type. Project creation cancelled.");
                    return;
                }
                addMoreFlatTypes = false;
                continue;
            }
            
            FlatTypeEnum flatTypeEnum = null;
            switch (flatTypeChoice) {
                case "1":
                    flatTypeEnum = FlatTypeEnum.TWO_ROOM;
                    break;
                case "2":
                    flatTypeEnum = FlatTypeEnum.THREE_ROOM;
                    break;
                default:
                    System.out.println("Invalid flat type choice. Please try again.");
                    continue;
            }
            
            // Check if flat type already exists
            boolean flatTypeExists = false;
            for (FlatType existingFlatType : flatTypes) {
                if (existingFlatType.getType() == flatTypeEnum) {
                    flatTypeExists = true;
                    break;
                }
            }
            
            if (flatTypeExists) {
                System.out.println("This flat type already exists in the project. Please choose a different flat type.");
                continue;
            }
            
            System.out.print("Number of Units: ");
            int numUnits;
            try {
                numUnits = Integer.parseInt(scanner.nextLine().trim());
                if (numUnits < 1) {
                    System.out.println("Number of units must be positive. Please try again.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please try again.");
                continue;
            }
            
            System.out.print("Selling Price (SGD): ");
            double sellingPrice;
            try {
                sellingPrice = Double.parseDouble(scanner.nextLine().trim());
                if (sellingPrice <= 0) {
                    System.out.println("Selling price must be positive. Please try again.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please try again.");
                continue;
            }
            
            // Create and add flat type
            FlatType flatType = new FlatType();
            flatType.setType(flatTypeEnum);
            flatType.setNumUnits(numUnits);
            flatType.setAvailableUnits(numUnits); // Initially all units available
            flatType.setSellingPrice(sellingPrice);
            
            flatTypes.add(flatType);
            System.out.println("Flat type added: " + flatTypeEnum);
            
            System.out.print("Add another flat type? (Y/N): ");
            String addMore = scanner.nextLine().trim().toUpperCase();
            addMoreFlatTypes = addMore.equals("Y");
        }
        
        // Project visibility
        System.out.print("Set project as visible to applicants? (Y/N): ");
        String visibleStr = scanner.nextLine().trim().toUpperCase();
        VisibilityEnum visibility = visibleStr.equals("Y") ? VisibilityEnum.VISIBLE : VisibilityEnum.HIDDEN;
        
        // Create project
        Project newProject = null;
        try {
            newProject = managerController.createProject(currentManager, projectName, neighborhood, 
                    applicationStartDate, applicationEndDate, officerSlots, visibility);
        } catch (Exception e) {
            System.out.println("Error creating project: " + e.getMessage());
            return;
        }

        if (newProject != null) {
            // Add flat types to project
            for (FlatType flatType : flatTypes) {
                newProject.addFlatType(flatType);
            }
            
            System.out.println("Project created successfully: " + newProject.getProjectName());
        } else {
            // The project creation has already printed the error message, no need to add a generic error
            return;
        }
    }

    /**
     * Allows the manager to edit an existing project's details.
     * The manager can modify project name, neighborhood, application dates,
     * officer slots, and add or modify flat types.
     */
    private void editProject() {
        System.out.println("\n===== EDIT PROJECT =====");
        
        // Get managed projects
        ArrayList<Project> managedProjects = managerController.getManagedProjects(currentManager);
        
        if (managedProjects.isEmpty()) {
            System.out.println("You are not managing any projects.");
            return;
        }
        
        // Display managed projects
        System.out.println("\nYour Managed Projects:");
        for (int i = 0; i < managedProjects.size(); i++) {
            Project project = managedProjects.get(i);
            System.out.println((i + 1) + ". " + project.getProjectName() + " (" + project.getNeighborhood() + ")");
        }
        
        // Select project to edit
        System.out.print("\nEnter project number to edit (or 0 to cancel): ");
        int projectNumber;
        try {
            projectNumber = Integer.parseInt(scanner.nextLine().trim());
            if (projectNumber == 0) {
                return;
            }
            if (projectNumber < 1 || projectNumber > managedProjects.size()) {
                System.out.println("Invalid project number. Operation cancelled.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Operation cancelled.");
            return;
        }
        
        Project selectedProject = managedProjects.get(projectNumber - 1);
        
        // Edit project details
        System.out.println("\nEditing Project: " + selectedProject.getProjectName());
        System.out.println("Enter new values (leave blank to keep current):");
        
        System.out.print("Project Name [" + selectedProject.getProjectName() + "]: ");
        String projectName = scanner.nextLine().trim();
        if (projectName.isEmpty()) {
            projectName = selectedProject.getProjectName();
        }
        
        System.out.print("Neighborhood [" + selectedProject.getNeighborhood() + "]: ");
        String neighborhood = scanner.nextLine().trim();
        if (neighborhood.isEmpty()) {
            neighborhood = selectedProject.getNeighborhood();
        }
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate applicationStartDate = selectedProject.getApplicationStartDate();
        LocalDate applicationEndDate = selectedProject.getApplicationEndDate();
        
        try {
            System.out.print("Application Start Date [" + applicationStartDate + "] (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine().trim();
            if (!startDateStr.isEmpty()) {
                applicationStartDate = LocalDate.parse(startDateStr, dateFormatter);
            }
            
            System.out.print("Application End Date [" + applicationEndDate + "] (YYYY-MM-DD): ");
            String endDateStr = scanner.nextLine().trim();
            if (!endDateStr.isEmpty()) {
                applicationEndDate = LocalDate.parse(endDateStr, dateFormatter);
            }
            
            if (applicationEndDate.isBefore(applicationStartDate)) {
                System.out.println("End date cannot be before start date. These fields will not be updated.");
                applicationStartDate = selectedProject.getApplicationStartDate();
                applicationEndDate = selectedProject.getApplicationEndDate();
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. These fields will not be updated.");
            applicationStartDate = selectedProject.getApplicationStartDate();
            applicationEndDate = selectedProject.getApplicationEndDate();
        }
        
        // Keep existing flat types
        ArrayList<FlatType> flatTypes = new ArrayList<>(selectedProject.getFlatTypes());
        
        // Edit existing flat types or add new ones
        System.out.println("\nEdit flat types?");
        System.out.println("1. Edit existing flat types");
        System.out.println("2. Add new flat type");
        System.out.println("0. Keep current flat types");
        System.out.print("Enter your choice: ");
        
        String flatTypeChoice = scanner.nextLine().trim();
        
        if (flatTypeChoice.equals("1")) {
            // Display and edit existing flat types
            for (int i = 0; i < flatTypes.size(); i++) {
                FlatType flatType = flatTypes.get(i);
                System.out.println("\nFlat Type #" + (i + 1) + ": " + flatType.getType());
                
                System.out.print("Total Units [" + flatType.getNumUnits() + "]: ");
                String numUnitsStr = scanner.nextLine().trim();
                if (!numUnitsStr.isEmpty()) {
                    try {
                        int numUnits = Integer.parseInt(numUnitsStr);
                        if (numUnits >= flatType.getNumUnits() - flatType.getAvailableUnits()) {
                            flatType.setAvailableUnits(numUnits - (flatType.getNumUnits() - flatType.getAvailableUnits()));
                            flatType.setNumUnits(numUnits);
                        } else {
                            System.out.println("Cannot reduce total units below booked units. This field will not be updated.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format. This field will not be updated.");
                    }
                }
                
                System.out.print("Selling Price [" + flatType.getSellingPrice() + "]: ");
                String priceStr = scanner.nextLine().trim();
                if (!priceStr.isEmpty()) {
                    try {
                        double price = Double.parseDouble(priceStr);
                        if (price > 0) {
                            flatType.setSellingPrice(price);
                        } else {
                            System.out.println("Price must be positive. This field will not be updated.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format. This field will not be updated.");
                    }
                }
            }
        } else if (flatTypeChoice.equals("2")) {
            // Add new flat type
            boolean typeTwoRoomExists = false;
            boolean typeThreeRoomExists = false;
            
            for (FlatType flatType : flatTypes) {
                if (flatType.getType() == FlatTypeEnum.TWO_ROOM) {
                    typeTwoRoomExists = true;
                } else if (flatType.getType() == FlatTypeEnum.THREE_ROOM) {
                    typeThreeRoomExists = true;
                }
            }
            
            System.out.println("\nAdd Flat Type:");
            if (!typeTwoRoomExists) {
                System.out.println("1. Two-Room");
            }
            if (!typeThreeRoomExists) {
                System.out.println("2. Three-Room");
            }
            if (typeTwoRoomExists && typeThreeRoomExists) {
                System.out.println("All flat types already added.");
                System.out.println("0. Cancel");
            } else {
                System.out.println("0. Cancel");
                System.out.print("Enter your choice: ");
                
                String newTypeChoice = scanner.nextLine().trim();
                
                FlatTypeEnum newFlatTypeEnum = null;
                if (newTypeChoice.equals("1") && !typeTwoRoomExists) {
                    newFlatTypeEnum = FlatTypeEnum.TWO_ROOM;
                } else if (newTypeChoice.equals("2") && !typeThreeRoomExists) {
                    newFlatTypeEnum = FlatTypeEnum.THREE_ROOM;
                } else if (!newTypeChoice.equals("0")) {
                    // Check if the flat type already exists in the project
                    boolean flatTypeExists = false;
                    if (newTypeChoice.equals("1")) {
                        flatTypeExists = typeTwoRoomExists;
                    } else if (newTypeChoice.equals("2")) {
                        flatTypeExists = typeThreeRoomExists;
                    }
                    
                    if (flatTypeExists) {
                        System.out.println("This flat type already exists in the project. Please choose a different flat type.");
                    } else {
                        System.out.println("Invalid choice. Operation cancelled.");
                    }
                    return;
                }
                
                if (newFlatTypeEnum != null) {
                    System.out.print("Number of Units: ");
                    int numUnits;
                    try {
                        numUnits = Integer.parseInt(scanner.nextLine().trim());
                        if (numUnits < 1) {
                            System.out.println("Number of units must be positive. Operation cancelled.");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format. Operation cancelled.");
                        return;
                    }
                    
                    System.out.print("Selling Price (SGD): ");
                    double sellingPrice;
                    try {
                        sellingPrice = Double.parseDouble(scanner.nextLine().trim());
                        if (sellingPrice <= 0) {
                            System.out.println("Selling price must be positive. Operation cancelled.");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format. Operation cancelled.");
            return;
        }
        
                    // Create and add new flat type
                    FlatType newFlatType = new FlatType();
                    newFlatType.setType(newFlatTypeEnum);
                    newFlatType.setNumUnits(numUnits);
                    newFlatType.setAvailableUnits(numUnits);
                    newFlatType.setSellingPrice(sellingPrice);
                    
                    flatTypes.add(newFlatType);
                    System.out.println("New flat type added: " + newFlatTypeEnum);
                }
            }
        }
        
        // Update project
        boolean updated = managerController.editProject(selectedProject, projectName, neighborhood, 
                applicationStartDate, applicationEndDate, flatTypes);
        
        if (updated) {
            System.out.println("Project updated successfully.");
        } else {
            System.out.println("Failed to update project. Please try again.");
        }
    }

    /**
     * Allows the manager to delete a project they are managing.
     * Confirms the deletion with the manager before proceeding.
     */
    private void deleteProject() {
        System.out.println("\n===== DELETE PROJECT =====");
        
        // Get managed projects
        ArrayList<Project> managedProjects = managerController.getManagedProjects(currentManager);
        
        if (managedProjects.isEmpty()) {
            System.out.println("You are not managing any projects.");
            return;
        }
        
        // Display managed projects
        System.out.println("\nYour Managed Projects:");
        for (int i = 0; i < managedProjects.size(); i++) {
            Project project = managedProjects.get(i);
            System.out.println((i + 1) + ". " + project.getProjectName() + " (" + project.getNeighborhood() + ")");
        }
        
        // Select project to delete
        System.out.print("\nEnter project number to delete (or 0 to cancel): ");
        int projectNumber;
        try {
            projectNumber = Integer.parseInt(scanner.nextLine().trim());
            if (projectNumber == 0) {
                return;
            }
            if (projectNumber < 1 || projectNumber > managedProjects.size()) {
                System.out.println("Invalid project number. Operation cancelled.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Operation cancelled.");
            return;
        }
        
        Project selectedProject = managedProjects.get(projectNumber - 1);
        
        // Confirm deletion
        System.out.print("\nAre you sure you want to delete project '" + selectedProject.getProjectName() + 
                "'? This action cannot be undone. (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();
        
        if (!confirm.equals("Y")) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        // Delete project
        boolean deleted = managerController.deleteProject(selectedProject);
        
        if (deleted) {
            System.out.println("Project deleted successfully.");
        } else {
            System.out.println("Failed to delete project. The project may have active applications or bookings.");
        }
    }

    /**
     * Allows the manager to toggle the visibility of a project.
     * Changing visibility controls whether applicants can view and apply to the project.
     */
    private void toggleProjectVisibility() {
        System.out.println("\n===== TOGGLE PROJECT VISIBILITY =====");
        
        // Get managed projects
        ArrayList<Project> managedProjects = managerController.getManagedProjects(currentManager);
        
        if (managedProjects.isEmpty()) {
            System.out.println("You are not managing any projects.");
            return;
        }
        
        // Display managed projects with current visibility
        System.out.println("\nYour Managed Projects:");
        for (int i = 0; i < managedProjects.size(); i++) {
            Project project = managedProjects.get(i);
            System.out.println((i + 1) + ". " + project.getProjectName() + 
                    " (" + project.getNeighborhood() + ") - Visibility: " + project.getVisibility());
        }
        
        // Select project to toggle visibility
        System.out.print("\nEnter project number to toggle visibility (or 0 to cancel): ");
        int projectNumber;
        try {
            projectNumber = Integer.parseInt(scanner.nextLine().trim());
            if (projectNumber == 0) {
                return;
            }
            if (projectNumber < 1 || projectNumber > managedProjects.size()) {
                System.out.println("Invalid project number. Operation cancelled.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Operation cancelled.");
            return;
        }
        
        Project selectedProject = managedProjects.get(projectNumber - 1);
        
        // Toggle visibility
        VisibilityEnum newVisibility = selectedProject.getVisibility() == VisibilityEnum.VISIBLE ? 
                VisibilityEnum.HIDDEN : VisibilityEnum.VISIBLE;
        
        boolean updated = managerController.updateProjectVisibility(selectedProject, newVisibility);
        
        if (updated) {
            System.out.println("Project '" + selectedProject.getProjectName() + "' visibility changed to " + newVisibility);
        } else {
            System.out.println("Failed to update project visibility. Please try again.");
        }
    }

    /**
     * Displays all projects in the system, regardless of the manager assigned.
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
     * Displays only the projects that are managed by the current manager.
     */
    private void viewMyProjects() {
        System.out.println("\n===== MY PROJECTS =====");
        ArrayList<Project> managedProjects = managerController.getManagedProjects(currentManager);
        
        if (managedProjects.isEmpty()) {
            System.out.println("You are not managing any projects.");
        } else {
            displayProjects(managedProjects);
        }
    }
    
    /**
     * Displays a formatted list of projects with their details.
     * 
     * @param projects ArrayList of Project objects to display
     */
    private void displayProjects(ArrayList<Project> projects) {
        System.out.println("\n-----------------------------------------------------------------");
        System.out.printf("%-20s | %-15s | %-10s | %-15s | %-10s\n", 
                "Project Name", "Neighborhood", "Visibility", "Application Period", "Manager");
        System.out.println("-----------------------------------------------------------------");
        
        for (Project project : projects) {
            String period = project.getApplicationStartDate() + " to " + project.getApplicationEndDate();
            String managerName = project.getManager() != null ? project.getManager().getName() : "None";
            
            System.out.printf("%-20s | %-15s | %-10s | %-15s | %-10s\n", 
                    project.getProjectName(), 
                    project.getNeighborhood(),
                    project.getVisibility(), 
                    period, 
                    managerName);
            
            // Display flat types
            for (FlatType flatType : project.getFlatTypes()) {
                System.out.printf("  %-10s: %d/%d units available, Price: $%.2f\n", 
                        flatType.getType(), 
                        flatType.getAvailableUnits(), 
                        flatType.getNumUnits(), 
                        flatType.getSellingPrice());
            }
            
            // Display assigned officers
            if (!project.getAssignedOfficers().isEmpty()) {
                System.out.println("  Assigned Officers (" + project.getAssignedOfficers().size() + 
                        "/" + project.getOfficerSlots() + "):");
                for (Officer officer : project.getAssignedOfficers()) {
                    System.out.println("  - " + officer.getName());
                }
            } else {
                System.out.println("  No officers assigned yet.");
            }
            
            System.out.println("-----------------------------------------------------------------");
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
    public ManagerManageProjectsView() {
        this.scanner = new Scanner(System.in);
        this.managerController = new ManagerController();
    }
}