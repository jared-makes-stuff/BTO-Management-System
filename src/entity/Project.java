package entity;

import database.*;
import enums.*;
import java.time.LocalDate;
import java.util.*;

/**
 * The Project class represents a housing project within the housing application system.
 * It contains details about a specific housing project including its name, location,
 * application period, available flat types, and the staff members associated with it.
 * 
 * Projects can be managed by a manager and have officers assigned to them. They also
 * have visibility settings that control whether they appear in public listings.
 */
public class Project {

	/**
	 * Instance variables that store project details
	 */
	private String projectName;               // The name of the housing project
	private String neighborhood;              // The neighborhood/location of the project
	private LocalDate applicationStartDate;   // When applications for this project open
	private LocalDate applicationEndDate;     // When applications for this project close
	private Manager manager;                  // The manager responsible for this project
	private int officerSlots;                 // Maximum number of officers that can be assigned
	private VisibilityEnum visibility;        // Current visibility status of the project
	private ArrayList<FlatType> flatTypes;    // List of flat types available in this project
	private ArrayList<Officer> assignedOfficers; // Officers currently assigned to this project
	
	/**
	 * Static database reference for Project persistence
	 */
	private static ProjectDatabase database;

	/**
	 * Default constructor that initializes a Project with empty/default values.
	 * Creates a new Project with blank fields and HIDDEN visibility.
	 */
	public Project() {
		this.projectName = "";
		this.neighborhood = "";
		this.applicationStartDate = null;
		this.applicationEndDate = null;
		this.flatTypes = new ArrayList<>();
		this.manager = null;
		this.officerSlots = 0;
		this.assignedOfficers = new ArrayList<>();
		this.visibility = VisibilityEnum.HIDDEN;
	}

	/**
	 * Complete constructor that initializes a Project with all specified values.
	 * 
	 * @param projectName The name identifier for this housing project
	 * @param neighborhood The area or district where the project is located
	 * @param applicationStartDate When citizens can begin applying for this project
	 * @param applicationEndDate When the application period for this project closes
	 * @param flatTypes List of flat types (sizes and configurations) available in this project
	 * @param manager The manager who oversees this project
	 * @param officerSlots Maximum number of officers that can be assigned to this project
	 * @param assignedOfficers List of officers currently assigned to this project
	 * @param visibility Whether the project is visible to the public or hidden
	 */
	public Project(String projectName, String neighborhood, LocalDate applicationStartDate, LocalDate applicationEndDate, ArrayList<FlatType> flatTypes, Manager manager, int officerSlots, ArrayList<Officer> assignedOfficers, VisibilityEnum visibility) {
		this.projectName = projectName;
		this.neighborhood = neighborhood;
		this.applicationStartDate = applicationStartDate;
		this.applicationEndDate = applicationEndDate;
		this.flatTypes = flatTypes != null ? flatTypes : new ArrayList<>();
		this.manager = manager;
		this.officerSlots = officerSlots;
		this.assignedOfficers = assignedOfficers != null ? assignedOfficers : new ArrayList<>();
		this.visibility = visibility;
		
		// Add this project to manager's managed projects if manager is not null
		if (manager != null) {
			manager.addProject(this);
		}
	}

	/**
	 * Setters for modifying Project attributes
	 */
	
	/**
	 * Sets the name of this housing project.
	 * 
	 * @param projectName The new name for this project
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * Sets the neighborhood/location of this housing project.
	 * 
	 * @param neighborhood The new neighborhood for this project
	 */
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	/**
	 * Sets the date when applications for this project will begin.
	 * 
	 * @param applicationStartDate The new application start date
	 */
	public void setApplicationStartDate(LocalDate applicationStartDate) {
		this.applicationStartDate = applicationStartDate;
	}

	/**
	 * Sets the date when applications for this project will close.
	 * 
	 * @param applicationEndDate The new application end date
	 */
	public void setApplicationEndDate(LocalDate applicationEndDate) {
		this.applicationEndDate = applicationEndDate;
	}

	/**
	 * Updates the manager responsible for this project.
	 * If the project already has a manager, it will be removed from their list of projects.
	 * 
	 * @param manager The new manager for this project
	 */
	public void setManager(Manager manager) {
		// Don't re-set the same manager
		if (this.manager == manager) {
			return;
		}
		
		// If there's an existing manager, remove this project from their list
		if (this.manager != null) {
			this.manager.removeProject(this);
		}
		
		this.manager = manager;
	}

	/**
	 * Sets the maximum number of officers that can be assigned to this project.
	 * 
	 * @param officerSlots The new number of officer slots
	 */
	public void setOfficerSlots(int officerSlots) {
		this.officerSlots = officerSlots;
	}

	/**
	 * Updates the visibility status of this project.
	 * 
	 * @param visibility The new visibility status (VISIBLE or HIDDEN)
	 */
	public void setVisibility(VisibilityEnum visibility) {
		this.visibility = visibility;
	}

	/**
	 * Sets the list of flat types available in this project.
	 * 
	 * @param flatTypes The new list of flat types
	 */
	public void setFlatTypes(ArrayList<FlatType> flatTypes) {
		this.flatTypes = flatTypes;
	}

	/**
	 * Updates the list of officers assigned to this project.
	 * 
	 * @param assignedOfficers The new list of assigned officers
	 */
	public void setAssignedOfficers(ArrayList<Officer> assignedOfficers) {
		this.assignedOfficers = assignedOfficers;
	}

	/**
	 * Sets the static database reference for Project persistence.
	 * 
	 * @param database The database instance to use for Project storage
	 */
	public static void setDatabase(ProjectDatabase database) {
		Project.database = database;
	}

	/**
	 * Getters for accessing Project attributes
	 */
	
	/**
	 * Gets the name of this housing project.
	 * 
	 * @return The project name
	 */
	public String getProjectName() {
		return this.projectName;
	}

	/**
	 * Gets the neighborhood/location of this housing project.
	 * 
	 * @return The project's neighborhood
	 */
	public String getNeighborhood() {
		return this.neighborhood;
	}

	/**
	 * Gets the date when applications for this project will begin.
	 * 
	 * @return The application start date
	 */
	public LocalDate getApplicationStartDate() {
		return this.applicationStartDate;
	}

	/**
	 * Gets the date when applications for this project will close.
	 * 
	 * @return The application end date
	 */
	public LocalDate getApplicationEndDate() {
		return this.applicationEndDate;
	}

	/**
	 * Gets the manager responsible for this project.
	 * 
	 * @return The project manager
	 */
	public Manager getManager() {
		return this.manager;
	}

	/**
	 * Gets the maximum number of officers that can be assigned to this project.
	 * 
	 * @return The number of officer slots
	 */
	public int getOfficerSlots() {
		return this.officerSlots;
	}

	/**
	 * Gets the visibility status of this project.
	 * 
	 * @return The visibility status (VISIBLE or HIDDEN)
	 */
	public VisibilityEnum getVisibility() {
		return this.visibility;
	}

	/**
	 * Gets the list of flat types available in this project.
	 * 
	 * @return The list of flat types
	 */
	public ArrayList<FlatType> getFlatTypes() {
		return this.flatTypes;
	}

	/**
	 * Gets the list of officers assigned to this project.
	 * 
	 * @return The list of assigned officers
	 */
	public ArrayList<Officer> getAssignedOfficers() {
		return this.assignedOfficers;
	}

	/**
	 * Gets the static database reference for Project persistence.
	 * 
	 * @return The Project database instance
	 */
	public static ProjectDatabase getDatabase() {
		return Project.database;
	}

	/**
	 * Instance Methods for Project management
	 */
	
	/**
	 * Adds a new flat type to this project if it's not already included.
	 * 
	 * @param flatType The flat type to add to the project
	 */
	public void addFlatType(FlatType flatType) {
		if (flatType != null && this.flatTypes != null) {
			if (!this.flatTypes.contains(flatType)){
				this.flatTypes.add(flatType);
			}
		}
	}

	/**
	 * Attempts to assign an officer to this project.
	 * The assignment will fail if there are no available slots or if the officer
	 * is already assigned to this project.
	 * 
	 * @param officer The officer to assign to this project
	 * @return true if the officer was successfully assigned, false otherwise
	 */
	public boolean assignOfficer(Officer officer) {
		if (officer == null || this.assignedOfficers == null) {
			return false;
		}
		
		// Check if there are available slots
		if (this.assignedOfficers.size() >= this.officerSlots) {
			return false;
		}
		
		// Check if officer is already assigned
		if (this.isOfficerAssigned(officer)) {
			return false;
		}
		
		this.assignedOfficers.add(officer);
		return true;
	}

	/**
	 * Removes an officer from this project's assigned officers list.
	 * 
	 * @param officer The officer to remove from this project
	 * @return true if the officer was successfully removed, false otherwise
	 */
	public boolean unassignOfficer(Officer officer) {
		if (officer == null || this.assignedOfficers == null) {
			return false;
		}
		
		return this.assignedOfficers.remove(officer);
	}

	/**
	 * Checks if a specific officer is currently assigned to this project.
	 * 
	 * @param officer The officer to check
	 * @return true if the officer is assigned to this project, false otherwise
	 */
	public boolean isOfficerAssigned(Officer officer) {
		if (officer == null || this.assignedOfficers == null) {
			return false;
		}
		
		return this.assignedOfficers.contains(officer);
	}

	/**
	 * Determines if the project is currently accepting applications.
	 * A project is open for applications if the current date is between
	 * the application start and end dates (inclusive).
	 * 
	 * @return true if the project is currently accepting applications, false otherwise
	 */
	public boolean isApplicationOpen() {
		LocalDate today = LocalDate.now();
		return applicationStartDate != null && applicationEndDate != null && 
				!today.isBefore(applicationStartDate) && !today.isAfter(applicationEndDate);
	}

	/**
	 * Static Methods for Project lookup and management
	 */
	
	/**
	 * Finds a project by its name in the database.
	 * 
	 * @param projectName The name of the project to find
	 * @return The Project with the matching name, or null if not found
	 */
	public static Project findProjectByName(String projectName) {
		if (database == null || projectName == null) {
			return null;
		}
		
		for (Project project : database.getProjects()) {
			if (projectName.equals(project.getProjectName())) {
				return project;
			}
		}
		return null;
	}

	/**
	 * Finds all projects in a specific neighborhood.
	 * 
	 * @param neighborhood The neighborhood to search for
	 * @return A list of Projects in the specified neighborhood
	 */
	public static ArrayList<Project> findProjectsByNeighborhood(String neighborhood) {
		ArrayList<Project> result = new ArrayList<>();
		if (database == null || neighborhood == null) {
			return result;
		}
		
		for (Project project : database.getProjects()) {
			if (neighborhood.equals(project.getNeighborhood())) {
				result.add(project);
			}
		}
		return result;
	}

	/**
	 * Finds all projects with a specific application start date.
	 * 
	 * @param startDate The application start date to search for
	 * @return A list of Projects with the matching application start date
	 */
	public static ArrayList<Project> findProjectsByApplicationStartDate(LocalDate startDate) {
		ArrayList<Project> result = new ArrayList<>();
		if (database == null || startDate == null) {
			return result;
		}
		
		for (Project project : database.getProjects()) {
			if (startDate.equals(project.getApplicationStartDate())) {
				result.add(project);
			}
		}
		return result;
	}

	/**
	 * Finds all projects with a specific application end date.
	 * 
	 * @param endDate The application end date to search for
	 * @return A list of Projects with the matching application end date
	 */
	public static ArrayList<Project> findProjectsByApplicationEndDate(LocalDate endDate) {
		ArrayList<Project> result = new ArrayList<>();
		if (database == null || endDate == null) {
			return result;
		}
		
		for (Project project : database.getProjects()) {
			if (endDate.equals(project.getApplicationEndDate())) {
				result.add(project);
			}
		}
		return result;
	}

	/**
	 * Finds all projects that offer a specific flat type.
	 * 
	 * @param flatType The type of flat to search for
	 * @return A list of Projects offering the specified flat type
	 */
	public static ArrayList<Project> findProjectsByFlatType(enums.FlatTypeEnum flatType) {
		ArrayList<Project> result = new ArrayList<>();
		if (database == null || flatType == null) {
			return result;
		}
		
		for (Project project : database.getProjects()) {
			for (FlatType ft : project.getFlatTypes()) {
				if (flatType.equals(ft.getType())) {
					result.add(project);
					break; // Found a match, move to next project
				}
			}
		}
		return result;
	}

	/**
	 * Finds all projects managed by a specific manager.
	 * 
	 * @param manager The manager to search for
	 * @return A list of Projects managed by the specified manager
	 */
	public static ArrayList<Project> findProjectsByManager(Manager manager) {
		ArrayList<Project> result = new ArrayList<>();
		if (database == null || manager == null) {
			return result;
		}
		
		for (Project project : database.getProjects()) {
			if (manager.equals(project.getManager())) {
				result.add(project);
			}
		}
		return result;
	}

	/**
	 * Finds all projects that have a specific officer assigned.
	 * 
	 * @param officer The officer to search for
	 * @return A list of Projects with the specified officer assigned
	 */
	public static ArrayList<Project> findProjectsByOfficer(Officer officer) {
		ArrayList<Project> result = new ArrayList<>();
		if (database == null || officer == null) {
			return result;
		}
		
		for (Project project : database.getProjects()) {
			if (project.isOfficerAssigned(officer)) {
				result.add(project);
			}
		}
		return result;
	}

	/**
	 * Finds all projects with a specific visibility status.
	 * 
	 * @param visibility The visibility status to search for
	 * @return A list of Projects with the specified visibility status
	 */
	public static ArrayList<Project> findProjectsByVisibility(VisibilityEnum visibility) {
		ArrayList<Project> result = new ArrayList<>();
		if (database == null || visibility == null) {
			return result;
		}
		
		for (Project project : database.getProjects()) {
			if (visibility.equals(project.getVisibility())) {
				result.add(project);
			}
		}
		return result;
	}

	/**
	 * Adds a project to the database if it doesn't already exist.
	 * 
	 * @param project The project to add to the database
	 * @return true if the project was added successfully, false otherwise
	 */
	public static boolean addToDatabase(Project project) {
		if (database == null || project == null) {
			return false;
		}
		
		ArrayList<Project> projects = database.getProjects();
		if (projects.contains(project)) {
			return false; // Already in database
		}
		
		projects.add(project);
		return true;
	}

	/**
	 * Removes a project from the database.
	 * 
	 * @param project The project to remove from the database
	 * @return true if the project was removed successfully, false otherwise
	 */
	public static boolean removeFromDatabase(Project project) {
		if (database == null || project == null) {
			return false;
		}
		
		ArrayList<Project> projects = database.getProjects();
		return projects.remove(project);
	}

	/**
	 * Gets all projects currently stored in the database.
	 * 
	 * @return A list of all Projects in the database
	 */
	public static ArrayList<Project> getAllProjects() {
		if (database == null) {
			return new ArrayList<>();
		}
		return database.getProjects();
	}

	/**
	 * Returns a string representation of this project with its key details.
	 * 
	 * @return A string containing the project's name, neighborhood, and application dates
	 */
	@Override()
	public String toString() {
		return "Project [projectName=" + projectName + ", neighborhood=" + neighborhood + ", applicationPeriod=" + applicationStartDate + " to " + applicationEndDate + ", flatTypes=" + flatTypes.size() + ", officerSlots=" + officerSlots + ", assignedOfficers=" + assignedOfficers.size() + ", visibility=" + visibility + "]";
	}

}