package database;

import entity.*;
import interfaces.*;
import java.util.*;

/**
 * Database class for storing and managing officer entities.
 * This class maintains a collection of Officer objects and provides
 * methods to access and display officer information.
 * 
 * Officers are users who can be assigned to BTO projects to manage applications,
 * process bookings, and handle various project-related tasks. This database
 * tracks all registered officers in the system and their project assignments.
 * 
 * @implements IDatabasePrintable Interface for database display functionality
 */
public class OfficerDatabase implements IDatabasePrintable {

	/**
	 * Collection of officers stored in the database.
	 */
	private ArrayList<Officer> officers;

	/**
	 * Default constructor that initializes an empty officer database.
	 * Creates a new ArrayList to store Officer objects.
	 */
	public OfficerDatabase() {
		this.officers = new ArrayList<>();
	}

	/**
	 * Sets the list of officers in the database.
	 * Replaces the existing collection with a new collection of officers.
	 * 
	 * @param officers The ArrayList of Officer objects to set as the database content
	 */
	public void setOfficers(ArrayList<Officer> officers) {
		this.officers = officers;
	}

	/**
	 * Retrieves the complete list of officers from the database.
	 * 
	 * @return ArrayList containing all Officer objects stored in the database
	 */
	public ArrayList<Officer> getOfficers() {
		return this.officers;
	}

	/**
	 * Prints all officers in the database in a formatted table.
	 * Displays officer personal information including name, NRIC, age, marital status,
	 * and a list of projects they are assigned to.
	 * If the database is empty, displays a message indicating no officers are found.
	 * 
	 * This method implements the printData method required by the IDatabasePrintable interface.
	 */
	@Override
	public void printData() {
		System.out.println("===================== OFFICER DATABASE =====================");
		if (officers.isEmpty()) {
			System.out.println("No officers found in the database.");
		} else {
			System.out.printf("%-20s %-15s %-5s %-15s %-20s\n", "Name", "NRIC", "Age", "Marital Status", "Assigned Projects");
			System.out.println("------------------------------------------------------------");
			
			for (Officer officer : officers) {
				System.out.printf("%-20s %-15s %-5d %-15s\n", officer.getName(), officer.getNric(), officer.getAge(), officer.getMaritalStatus());
				
				// Print assigned projects if any
				ArrayList<Project> assignedProjects = officer.getAssignedProjects();
				if (assignedProjects != null && !assignedProjects.isEmpty()) {
					System.out.println("  Assigned Projects:");
					for (Project project : assignedProjects) {
						System.out.println("    - " + project.getProjectName());
					}
				}
			}
		}
		System.out.println("=============================================================");
	}
}