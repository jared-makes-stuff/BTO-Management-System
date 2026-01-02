package database;

import interfaces.*;
import java.util.*;
import entity.*;

/**
 * Database class for storing and managing officer applications.
 * This class maintains a collection of OfficerApplication objects and provides
 * methods to access and display these applications.
 * 
 * Officer applications represent requests by officers to be assigned to specific BTO projects.
 * These applications are reviewed by managers who can approve or reject them.
 * 
 * @implements IDatabasePrintable Interface for database display functionality
 */
public class OfficerApplicationDatabase implements IDatabasePrintable {

	/**
	 * Collection of officer applications stored in the database.
	 */
	private ArrayList<OfficerApplication> applications;

	/**
	 * Default constructor that initializes an empty officer application database.
	 * Creates a new ArrayList to store OfficerApplication objects.
	 */
	public OfficerApplicationDatabase() {
		this.applications = new ArrayList<>();
	}

	/**
	 * Sets the list of officer applications in the database.
	 * Replaces the existing collection with a new collection of applications.
	 * 
	 * @param applications The ArrayList of OfficerApplication objects to set as the database content
	 */
	public void setApplications(ArrayList<OfficerApplication> applications) {
		this.applications = applications;
	}

	/**
	 * Retrieves the complete list of officer applications from the database.
	 * 
	 * @return ArrayList containing all OfficerApplication objects stored in the database
	 */
	public ArrayList<OfficerApplication> getApplications() {
		return this.applications;
	}

	/**
	 * Prints all officer applications in the database in a formatted table.
	 * Displays application date, officer name, project name, and application status.
	 * If the database is empty, displays a message indicating no applications are found.
	 * 
	 * This method implements the printData method required by the IDatabasePrintable interface.
	 */
	@Override
	public void printData() {
		System.out.println("===================== OFFICER APPLICATION DATABASE =====================");
		if (applications.isEmpty()) {
			System.out.println("No officer applications found in the database.");
		} else {
			System.out.printf("%-15s %-20s %-20s %-15s\n", "Date", "Officer", "Project", "Status");
			System.out.println("------------------------------------------------------------------");
			
			for (OfficerApplication application : applications) {
				String officerName = application.getOfficer() != null ? application.getOfficer().getName() : "N/A";
				String projectName = application.getProject() != null ? application.getProject().getProjectName() : "N/A";
						
				System.out.printf("%-15s %-20s %-20s %-15s\n", application.getApplicationDate(), officerName,projectName, application.getStatus());
			}
		}
		System.out.println("=====================================================================");
	}
}