package database;

import interfaces.*;
import java.util.*;
import entity.*;

/**
 * Database class for managing enquiries in the system.
 * Stores, retrieves, and displays all Enquiry entities.
 * Implements the IDatabasePrintable interface to provide data display functionality.
 */
public class EnquiryDatabase implements IDatabasePrintable {

	/**
	 * Collection of all enquiries in the system
	 */
	private ArrayList<Enquiry> enquiries;

	/**
	 * Default constructor that initializes an empty enquiries collection
	 */
	public EnquiryDatabase() {
		this.enquiries = new ArrayList<>();
	}

	/**
	 * Sets the enquiries collection to a new list
	 * 
	 * @param enquiries The new list of enquiries to use
	 */
	public void setEnquiries(ArrayList<Enquiry> enquiries) {
		this.enquiries = enquiries;
	}

	/**
	 * Retrieves the complete list of enquiries
	 * 
	 * @return ArrayList containing all enquiries in the database
	 */
	public ArrayList<Enquiry> getEnquiries() {
		return this.enquiries;
	}

	/**
	 * Displays all enquiries in a formatted table
	 * Shows enquiry ID, date, submitter, project, status, respondent, content, and reply
	 */
	@Override
	public void printData() {
		System.out.println("===================== ENQUIRY DATABASE =====================");
		if (enquiries.isEmpty()) {
			System.out.println("No enquiries found in the database.");
		} else {
			System.out.printf("%-15s %-15s %-20s %-20s %-15s %-20s\n", "Enquiry ID", "Date", "Submitter", "Project", "Status", "Respondent");
			System.out.println("------------------------------------------------------------");
			
			for (Enquiry enquiry : enquiries) {
				String submitterName = enquiry.getSubmittedBy() != null ? enquiry.getSubmittedBy().getName() : "N/A";
				String projectName = enquiry.getProject() != null ? enquiry.getProject().getProjectName() : "N/A";
				String respondentName = enquiry.getRespondent() != null ? enquiry.getRespondent().getName() : "N/A";
						
				System.out.printf("%-15s %-15s %-20s %-20s %-15s %-20s\n", enquiry.getEnquiryID(), enquiry.getDateTime(), submitterName, projectName, enquiry.getStatus(), respondentName);
				
				System.out.println("Content: " + enquiry.getContent());
				if (enquiry.getReply() != null && !enquiry.getReply().isEmpty()) {
					System.out.println("Reply: " + enquiry.getReply() + " (on " + enquiry.getReplyDate() + ")");
				}
				System.out.println("------------------------------------------------------------");
			}
		}
		System.out.println("=============================================================");
	}
}