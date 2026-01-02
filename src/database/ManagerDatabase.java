package database;

import interfaces.*;
import java.util.*;
import entity.*;

/**
 * Database class for managing Manager entities in the system.
 * Stores, retrieves, and displays all Manager users.
 * Implements the IDatabasePrintable interface to provide data display functionality.
 */
public class ManagerDatabase implements IDatabasePrintable {

	/**
	 * Collection of all managers in the system
	 */
	private ArrayList<Manager> managers;

	/**
	 * Default constructor that initializes an empty managers collection
	 */
	public ManagerDatabase() {
		this.managers = new ArrayList<>();
	}

	/**
	 * Sets the managers collection to a new list
	 * 
	 * @param managers The new list of managers to use
	 */
	public void setManagers(ArrayList<Manager> managers) {
		this.managers = managers;
	}

	/**
	 * Retrieves the complete list of managers
	 * 
	 * @return ArrayList containing all managers in the database
	 */
	public ArrayList<Manager> getManagers() {
		return this.managers;
	}

	/**
	 * Displays all managers in a formatted table
	 * Shows manager name, NRIC, age, marital status, and number of managed projects
	 */
	@Override
	public void printData() {
		System.out.println("===================== MANAGER DATABASE =====================");
		if (managers.isEmpty()) {
			System.out.println("No managers found in the database.");
		} else {
			System.out.printf("%-20s %-15s %-5s %-15s %-20s\n", "Name", "NRIC", "Age", "Marital Status", "Managed Projects");
			System.out.println("------------------------------------------------------------");
			
			for (Manager manager : managers) {				
				System.out.printf("%-20s %-15s %-5d %-15s %-20d\n", manager.getName(), manager.getNric(), manager.getAge(), manager.getMaritalStatus(), manager.getManagedProjects().size());
				
				// Print managed projects if any
				if (!manager.getManagedProjects().isEmpty()) {
					System.out.println("  Managed Projects:");
					for (Project project : manager.getManagedProjects()) {
						System.out.println("    - " + project.getProjectName());
					}
				}
			}
		}
		System.out.println("=============================================================");
	}
}