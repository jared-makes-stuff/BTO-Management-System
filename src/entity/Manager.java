package entity;

import database.*;
import enums.*;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 * Entity class representing a Manager in the BTO Management System.
 * Managers are responsible for overseeing BTO projects, handling enquiries,
 * approving officer applications, and managing the overall project lifecycle.
 * Each manager can manage multiple projects and has access to system reporting
 * and administrative functions.
 * 
 * This class extends the User class and adds project management capabilities.
 */
public class Manager extends User {

	/**
	 * List of projects managed by this manager
	 */
	private ArrayList<Project> managedProjects;
	
	/**
	 * Static reference to the database of all managers in the system
	 */
	private static ManagerDatabase database;

	/**
	 * Default constructor that initializes a new manager with default values
	 * and an empty list of managed projects.
	 */
	public Manager() {
		super();
		this.managedProjects = new ArrayList<>();
	}

	/**
	 * Full constructor that initializes a manager with all properties.
	 * Inherits properties from the User parent class and adds manager-specific fields.
	 * 
	 * @param name The name of the manager
	 * @param nric The NRIC (National Registration Identity Card) number of the manager
	 * @param age The age of the manager
	 * @param password The login password for the manager
	 * @param maritalStatus The marital status of the manager (SINGLE or MARRIED)
	 * @param filter Any filtering preferences set by the manager
	 * @param managedProjects List of projects this manager is responsible for
	 */
	public Manager(String name, String nric, int age, String password, MarriageStatusEnum maritalStatus, Filter filter, ArrayList<Project> managedProjects) {
		super(name, nric, age, maritalStatus, password, filter);
		this.managedProjects = managedProjects != null ? managedProjects : new ArrayList<>();
	}

	/**
	 * Sets the list of projects managed by this manager.
	 * 
	 * @param managedProjects The ArrayList of Project objects to set as managed projects
	 */
	public void setManagedProjects(ArrayList<Project> managedProjects) {
		this.managedProjects = managedProjects;
	}

	/**
	 * Sets the static database reference for all Manager objects.
	 * 
	 * @param database The ManagerDatabase to use for storing managers
	 */
	public static void setDatabase(ManagerDatabase database) {
		Manager.database = database;
	}

	/**
	 * Gets the list of projects managed by this manager.
	 * 
	 * @return ArrayList containing all Project objects managed by this manager
	 */
	public ArrayList<Project> getManagedProjects() {
		return this.managedProjects;
	}

	/**
	 * Gets the static database reference for all Manager objects.
	 * 
	 * @return The ManagerDatabase being used to store managers
	 */
	public static ManagerDatabase getDatabase() {
		return Manager.database;
	}

	/**
	 * Adds a project to this manager's list of managed projects.
	 * Validates that the project does not overlap with existing projects' application periods.
	 * Also updates the project's manager reference to create a bidirectional relationship.
	 * 
	 * @param project The project to add to this manager's responsibility
	 * @return true if the project was added successfully, false if the project is null, 
	 *         already managed by this manager, or has an overlapping application period
	 */
	public boolean addProject(Project project) {
		if (project == null || this.managedProjects.contains(project)) {
			return false;
		}
		
		// Check if the manager is already handling a project within the same application period
		for (Project managedProject : this.managedProjects) {
			if (isOverlapping(managedProject, project)) {
				System.out.println("Cannot add project: Manager is already handling a project in the same application period.");
				return false; // Exit if an overlapping project is found
			}
		}
		
        this.managedProjects.add(project);
        
        // Update the project's manager reference, but only if it doesn't create a circular dependency
        if (project.getManager() != this) {
        	project.setManager(this);
        }
        
        return true;
    }

	/**
	 * Removes a project from this manager's list of managed projects.
	 * Also updates the project's manager reference to maintain consistency.
	 * 
	 * @param project The project to remove from this manager's responsibility
	 * @return true if the project was successfully removed, false if the project is null
	 *         or not managed by this manager
	 */
	public boolean removeProject(Project project) {
		if (project != null && this.managedProjects.contains(project)) {
			if (project.getManager() == this) {
				project.setManager(null);
			}
			return this.managedProjects.remove(project);
		}
		return false;
	}

	/**
	 * Finds a manager by name in the database.
	 * 
	 * @param name The name to search for
	 * @return The matching Manager object, or null if not found
	 */
	public static Manager findManagerByName(String name) {
		if (database == null || name == null) {
			return null;
		}
		
		for (Manager manager : database.getManagers()) {
			if (name.equals(manager.getName())) {
				return manager;
			}
		}
		return null;
	}

	/**
	 * Finds a manager by NRIC in the database.
	 * 
	 * @param nric The NRIC number to search for
	 * @return The matching Manager object, or null if not found
	 */
	public static Manager findManagerByNRIC(String nric) {
		if (database == null || nric == null) {
			return null;
		}
		
		for (Manager manager : database.getManagers()) {
			if (nric.equals(manager.getNric())) {
				return manager;
			}
		}
		return null;
	}

	/**
	 * Finds all managers of a specific age.
	 * 
	 * @param age The age to search for
	 * @return ArrayList of Manager objects matching the specified age
	 */
	public static ArrayList<Manager> findManagersByAge(int age) {
		ArrayList<Manager> result = new ArrayList<>();
		if (database == null) {
			return result;
		}
		
		for (Manager manager : database.getManagers()) {
			if (manager.getAge() == age) {
				result.add(manager);
			}
		}
		return result;
	}

	/**
	 * Finds all managers responsible for a specific project.
	 * 
	 * @param project The Project entity to search for
	 * @return ArrayList of Manager objects managing the specified project
	 */
	public static ArrayList<Manager> findManagersByProject(Project project) {
		ArrayList<Manager> result = new ArrayList<>();
		if (database == null || project == null) {
			return result;
		}
		
		for (Manager manager : database.getManagers()) {
			if (manager.getManagedProjects().contains(project)) {
				result.add(manager);
			}
		}
		return result;
	}

	/**
	 * Finds all managers with a specific marital status.
	 * 
	 * @param status The MarriageStatusEnum value to search for
	 * @return ArrayList of Manager objects with the specified marital status
	 */
	public static ArrayList<Manager> findManagerByMaritalStatus(MarriageStatusEnum status) {
		ArrayList<Manager> result = new ArrayList<>();
		if (database == null || status == null) {
			return result;
		}
		
		for (Manager manager : database.getManagers()) {
			if (status.equals(manager.getMaritalStatus())) {
				result.add(manager);
			}
		}
		return result;
	}

	/**
	 * Adds a manager to the database if it doesn't already exist.
	 * 
	 * @param manager The Manager object to add to the database
	 * @return boolean True if the manager was successfully added, false otherwise
	 */
	public static boolean addToDatabase(Manager manager) {
		if (database == null || manager == null) {
			return false;
		}
		
		ArrayList<Manager> managers = database.getManagers();
		if (managers.contains(manager)) {
			return false; // Already in database
		}
		
		managers.add(manager);
		return true;
	}

	/**
	 * Removes a manager from the database.
	 * 
	 * @param manager The Manager object to remove from the database
	 * @return boolean True if the manager was successfully removed, false otherwise
	 */
	public static boolean removeFromDatabase(Manager manager) {
		if (database == null || manager == null) {
			return false;
		}
		
		ArrayList<Manager> managers = database.getManagers();
		return managers.remove(manager);
	}

	/**
	 * Retrieves all managers stored in the database.
	 * 
	 * @return ArrayList containing all Manager objects in the database
	 */
	public static ArrayList<Manager> getAllManagers() {
		if (database == null) {
			return new ArrayList<>();
		}
		return database.getManagers();
	}

	/**
	 * Returns a string representation of this manager.
	 * Includes name, NRIC, age, marital status, and number of managed projects.
	 * 
	 * @return String representation of the manager
	 */
	@Override()
	public String toString() {
		return "Manager [name=" + getName() + ", nric=" + getNric() + ", age=" + getAge() + ", maritalStatus=" + getMaritalStatus() + ", managedProjects=" + managedProjects.size() + "]";
	}

	/**
	 * Helper method to check if two projects have overlapping application periods.
	 * This is used to prevent a manager from handling projects with conflicting timelines.
	 * 
	 * @param project1 The first project to compare
	 * @param project2 The second project to compare
	 * @return true if the application periods overlap, false otherwise
	 */
	private boolean isOverlapping(Project project1, Project project2) {
		LocalDate start1 = project1.getApplicationStartDate();
		LocalDate end1 = project1.getApplicationEndDate();
		LocalDate start2 = project2.getApplicationStartDate();
		LocalDate end2 = project2.getApplicationEndDate();

		// Check if the periods overlap
		return (start1.isEqual(end2) || start1.isBefore(end2)) && (end1.isEqual(start2) || end1.isAfter(start2));
	}
}