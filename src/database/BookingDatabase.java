package database;

import entity.*;
import interfaces.*;
import java.util.*;

/**
 * Database management class for Booking records in the BTO system.
 * Tracks all flat booking sessions made by applicants after successful
 * application to a BTO project. Implements printable functionality
 * for displaying booking data in a formatted way.
 */
public class BookingDatabase implements IDatabasePrintable {

	/**
	 * Collection of all flat bookings in the system
	 */
	private ArrayList<Booking> bookings;

	/**
	 * Default constructor that initializes an empty booking database.
	 * Creates a new collection to store booking records.
	 */
	public BookingDatabase() {
		this.bookings = new ArrayList<>();
	}

	/**
	 * Updates the entire collection of bookings in the database.
	 * Replaces any existing bookings with the provided collection.
	 * 
	 * @param bookings The new collection of bookings to store
	 */
	public void setBookings(ArrayList<Booking> bookings) {
		this.bookings = bookings;
	}

	/**
	 * Retrieves the complete collection of bookings from the database.
	 * 
	 * @return List of all bookings currently in the database
	 */
	public ArrayList<Booking> getBookings() {
		return this.bookings;
	}

	/**
	 * Displays the booking database in a formatted table.
	 * Shows key booking information including date, applicant details,
	 * project information, flat type, and current status. Useful for
	 * officers and managers to track the booking process.
	 */
	@Override
	public void printData() {
		System.out.println("===================== BOOKING DATABASE =====================");
		if (bookings.isEmpty()) {
			System.out.println("No bookings found in the database.");
		} else {
			System.out.printf("%-15s %-20s %-20s %-10s %-15s\n", "Date", "Applicant", "Project", "Flat Type", "Status");
			System.out.println("------------------------------------------------------------");
			
			for (Booking booking : bookings) {
				String applicantName = booking.getApplication() != null && booking.getApplication().getApplicant() != null ? booking.getApplication().getApplicant().getName() : "N/A";
				String projectName = booking.getApplication() != null && booking.getApplication().getProject() != null ? booking.getApplication().getProject().getProjectName() : "N/A";
						
				System.out.printf("%-15s %-20s %-20s %-10s %-15s\n", booking.getBookingDateTime(), applicantName, projectName, booking.getFlatType(), booking.getStatus());
			}
		}
		System.out.println("=============================================================");
	}
}