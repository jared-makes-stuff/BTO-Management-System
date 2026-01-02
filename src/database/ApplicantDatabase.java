package database;

import interfaces.*;
import java.util.*;
import entity.*;

/**
 * Database management class for Applicant entities in the BTO system.
 * Stores and manages a collection of applicants and implements printable
 * functionality for displaying applicant data in a tabular format.
 */
public class ApplicantDatabase implements IDatabasePrintable {

	/**
	 * Collection of all applicants in the system
	 */
	private ArrayList<Applicant> applicants;

	/**
	 * Default constructor that initializes an empty applicant database.
	 * Creates a new empty collection to store applicant records.
	 */
	public ApplicantDatabase() {
		this.applicants = new ArrayList<>();
	}

	/**
	 * Updates the entire collection of applicants in the database.
	 * Replaces any existing applicants with the provided collection.
	 * 
	 * @param applicants The new collection of applicants to store
	 */
	public void setApplicants(ArrayList<Applicant> applicants) {
		this.applicants = applicants;
	}

	/**
	 * Retrieves the complete collection of applicants from the database.
	 * 
	 * @return List of all applicants currently in the database
	 */
	public ArrayList<Applicant> getApplicants() {
		return this.applicants;
	}

	/**
	 * Displays the applicant database in a formatted table.
	 * Shows each applicant's basic information along with counts of 
	 * their applications and enquiries for quick reference.
	 */
	@Override
	public void printData() {
		System.out.println("===================== APPLICANT DATABASE =====================");
		if (applicants.isEmpty()) {
			System.out.println("No applicants found in the database.");
		} else {
			System.out.printf("%-20s %-15s %-5s %-10s %-20s %-20s\n", "Name", "NRIC", "Age", "Marital Status", "Applications", "Enquiries");
			System.out.println("------------------------------------------------------------");
			
			for (Applicant applicant : applicants) {
				System.out.printf("%-20s %-15s %-5d %-10s %-20d %-20d\n", applicant.getName(), applicant.getNric(), applicant.getAge(), applicant.getMaritalStatus(), applicant.getApplications().size(), applicant.getEnquiries().size());
			}
		}
		System.out.println("=============================================================");
	}
}