package database;

import interfaces.*;
import java.util.*;
import entity.*;

/**
 * Database class for managing BTO applications in the system.
 * Stores, retrieves, and displays all BTOApplication entities.
 * Implements the IDatabasePrintable interface to provide data display functionality.
 */
public class BTOApplicationDatabase implements IDatabasePrintable {
	
	/**
	 * Collection of all BTO applications in the system
	 */
	private ArrayList<BTOApplication> applications;

	/**
	 * Default constructor that initializes an empty applications collection
	 */
	public BTOApplicationDatabase() {
		this.applications = new ArrayList<>();
	}

	/**
	 * Sets the applications collection to a new list
	 * 
	 * @param applications The new list of BTO applications to use
	 */
	public void setApplications(ArrayList<BTOApplication> applications) {
		this.applications = applications;
	}

	/**
	 * Retrieves the complete list of BTO applications
	 * 
	 * @return ArrayList containing all BTO applications in the database
	 */
	public ArrayList<BTOApplication> getApplications() {
		return this.applications;
	}

	/**
	 * Displays all BTO applications in a formatted table
	 * Shows application date, applicant, project, flat type, status, and withdrawal status
	 */
	@Override
	public void printData() {
		System.out.println("===================== BTO APPLICATION DATABASE =====================");
		if (applications.isEmpty()) {
			System.out.println("No BTO applications found in the database.");
		} else {
			System.out.printf("%-15s %-20s %-20s %-10s %-15s %-15s\n", "Date", "Applicant", "Project", "Flat Type", "Status", "Withdrawal");
			System.out.println("------------------------------------------------------------------");
			
			for (BTOApplication application : applications) {
				String applicantName = application.getApplicant() != null ? application.getApplicant().getName() : "N/A";
				String projectName = application.getProject() != null ? application.getProject().getProjectName() : "N/A";
						
				System.out.printf("%-15s %-20s %-20s %-10s %-15s %-15s\n", application.getApplicationDate(), applicantName, projectName, application.getFlatType(), application.getStatus(), application.getWithdrawalStatus());
			}
		}
		System.out.println("===================================================================");
	}
}