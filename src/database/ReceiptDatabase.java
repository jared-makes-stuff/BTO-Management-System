package database;

import interfaces.*;
import java.util.*;
import entity.*;

/**
 * Database class for storing and managing receipt records.
 * This class maintains a collection of Receipt objects and provides
 * methods to access and display receipt information.
 * 
 * Receipts are generated when an applicant successfully books a flat in a BTO project.
 * Each receipt contains details about the booking, including the applicant, project,
 * flat type, and transaction date.
 * 
 * @implements IDatabasePrintable Interface for database display functionality
 */
public class ReceiptDatabase implements IDatabasePrintable {

	/**
	 * Collection of receipts stored in the database.
	 */
	private ArrayList<Receipt> receipts;

	/**
	 * Default constructor that initializes an empty receipt database.
	 * Creates a new ArrayList to store Receipt objects.
	 */
	public ReceiptDatabase() {
		this.receipts = new ArrayList<>();
	}

	/**
	 * Sets the list of receipts in the database.
	 * Replaces the existing collection with a new collection of receipts.
	 * 
	 * @param receipts The ArrayList of Receipt objects to set as the database content
	 */
	public void setReceipts(ArrayList<Receipt> receipts) {
		this.receipts = receipts;
	}

	/**
	 * Retrieves the complete list of receipts from the database.
	 * 
	 * @return ArrayList containing all Receipt objects stored in the database
	 */
	public ArrayList<Receipt> getReceipts() {
		return this.receipts;
	}

	/**
	 * Prints all receipts in the database in a formatted table.
	 * Displays receipt number, date, applicant name, project name, and flat type.
	 * If the database is empty, displays a message indicating no receipts are found.
	 * 
	 * The method handles null references gracefully by displaying "N/A" for
	 * missing information in the receipt or its related entities.
	 * 
	 * This method implements the printData method required by the IDatabasePrintable interface.
	 */
	@Override
	public void printData() {
		System.out.println("===================== RECEIPT DATABASE =====================");
		if (receipts.isEmpty()) {
			System.out.println("No receipts found in the database.");
		} else {
			System.out.printf("%-20s %-15s %-20s %-20s %-15s\n", "Receipt Number", "Date", "Applicant", "Project", "Flat Type");
			System.out.println("------------------------------------------------------------");
			
			for (Receipt receipt : receipts) {
				Booking booking = receipt.getBooking();
				String applicantName = "N/A";
				String projectName = "N/A";
				String flatType = "N/A";
				
				if (booking != null) {
					flatType = booking.getFlatType() != null ? booking.getFlatType().toString() : "N/A";
					
					if (booking.getApplication() != null) {
						applicantName = booking.getApplication().getApplicant() != null ? booking.getApplication().getApplicant().getName() : "N/A";
						projectName = booking.getApplication().getProject() != null ? booking.getApplication().getProject().getProjectName() : "N/A";
					}
				}
				
				System.out.printf("%-20s %-15s %-20s %-20s %-15s\n", receipt.getReceiptNumber(), receipt.getDate(), applicantName, projectName, flatType);
			}
		}
		System.out.println("=============================================================");
	}
}