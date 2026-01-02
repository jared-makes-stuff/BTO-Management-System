package database;

import interfaces.*;
import java.util.*;
import entity.*;

/**
 * Database management class for Project entities in the BTO system.
 * Maintains a collection of all housing projects and provides methods
 * for data display and manipulation. Used as a central repository for
 * project information across the application.
 */
public class ProjectDatabase implements IDatabasePrintable {

	/**
	 * Collection of all housing projects in the system
	 */
	private ArrayList<Project> projects;

	/**
	 * Default constructor that initializes an empty project database.
	 * Creates a new collection to hold project records.
	 */
	public ProjectDatabase() {
		this.projects = new ArrayList<>();
	}

	/**
	 * Sets the list of projects in the database.
	 * Replaces existing projects with the provided collection.
	 * 
	 * @param projects The list of projects to store in the database
	 */
	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}

	/**
	 * Retrieves the list of all projects in the database.
	 * 
	 * @return The collection of Project objects stored in this database
	 */
	public ArrayList<Project> getProjects() {
		return this.projects;
	}

	/**
	 * Displays the projects database in a formatted table.
	 * Shows comprehensive project information including name, location,
	 * application dates, manager details, and available flat types.
	 * Also displays assigned officers for each project.
	 */
	@Override
	public void printData() {
		System.out.println("===================== PROJECT DATABASE =====================");
		if (projects.isEmpty()) {
			System.out.println("No projects found in the database.");
		} else {
			System.out.printf("%-25s %-15s %-15s %-15s %-15s %-10s %-10s\n", 
					"Project Name", "Neighborhood", "Start Date", "End Date", "Manager", "Visibility", "Flat Types");
			System.out.println("-----------------------------------------------------------------------------------");
			
			for (Project project : projects) {
				String managerName = project.getManager() != null ? project.getManager().getName() : "N/A";
						
				System.out.printf("%-25s %-15s %-15s %-15s %-15s %-10s %-10d\n", project.getProjectName(), project.getNeighborhood(), project.getApplicationStartDate(), project.getApplicationEndDate(), managerName, project.getVisibility(), project.getFlatTypes().size());
				
				// Print flat types
				if (!project.getFlatTypes().isEmpty()) {
					System.out.println("  Flat Types:");
					for (FlatType flatType : project.getFlatTypes()) {
						System.out.printf("    - %s (Units: %d/%d, Price: $%.2f)\n", flatType.getType(), flatType.getAvailableUnits(), flatType.getNumUnits(), flatType.getSellingPrice());
					}
				}
				
				// Print officers assigned to the project
				if (!project.getAssignedOfficers().isEmpty()) {
					System.out.println("  Assigned Officers:");
					for (Officer officer : project.getAssignedOfficers()) {
						System.out.println("    - " + officer.getName());
					}
				}
				
				System.out.println("-----------------------------------------------------------------------------------");
			}
		}
		System.out.println("=================================================================");
	}
}