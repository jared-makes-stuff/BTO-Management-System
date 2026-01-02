package entity;

import database.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Entity class representing a receipt for a BTO flat booking.
 * This class tracks the transaction details when an applicant successfully books a flat,
 * including the receipt number, transaction date, and booking information.
 * 
 * A receipt is generated after a booking is confirmed and serves as proof of the transaction.
 * Each receipt has a unique number and is associated with exactly one booking.
 */
public class Receipt {

	/**
	 * Unique identifier for the receipt
	 */
	private String receiptNumber;
	
	/**
	 * Date when the receipt was generated
	 */
	private LocalDate date;
	
	/**
	 * Reference to the booking this receipt is for
	 */
	private Booking booking;
	
	/**
	 * Static reference to the database of all receipts in the system
	 */
	private static ReceiptDatabase database;

	/**
	 * Default constructor that initializes a new receipt with current date,
	 * generates a unique receipt number, and sets the booking to null.
	 */
	public Receipt() {
		this.receiptNumber = generateReceiptNumber();
		this.date = LocalDate.now();
		this.booking = null;
	}

	/**
	 * Full constructor that initializes a receipt with all properties.
	 * If some parameters are null, default values are applied.
	 * 
	 * @param receiptNumber Unique identifier for the receipt, generated if null
	 * @param date Date when the receipt was generated
	 * @param booking Reference to the booking this receipt is for
	 */
	public Receipt(String receiptNumber, LocalDate date, Booking booking) {
		this.receiptNumber = receiptNumber != null ? receiptNumber : generateReceiptNumber();
		this.date = date != null ? date : LocalDate.now();
		this.booking = booking;
	}

	/**
	 * Sets the receipt number.
	 * 
	 * @param receiptNumber The unique identifier to set for this receipt
	 */
	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	/**
	 * Sets the date when the receipt was generated.
	 * 
	 * @param date The LocalDate representing the generation date
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * Sets the booking this receipt is for.
	 * 
	 * @param booking The Booking entity this receipt is associated with
	 */
	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	/**
	 * Sets the static database reference for all Receipt objects.
	 * 
	 * @param database The ReceiptDatabase to use for storing receipts
	 */
	public static void setDatabase(ReceiptDatabase database) {
		Receipt.database = database;
	}

	/**
	 * Gets the unique receipt number.
	 * 
	 * @return The receipt number string
	 */
	public String getReceiptNumber() {
		return this.receiptNumber;
	}

	/**
	 * Gets the date when the receipt was generated.
	 * 
	 * @return The LocalDate representing the generation date
	 */
	public LocalDate getDate() {
		return this.date;
	}

	/**
	 * Gets the booking this receipt is for.
	 * 
	 * @return The Booking entity associated with this receipt
	 */
	public Booking getBooking() {
		return this.booking;
	}

	/**
	 * Gets the static database reference for all Receipt objects.
	 * 
	 * @return The ReceiptDatabase being used to store receipts
	 */
	public static ReceiptDatabase getDatabase() {
		return Receipt.database;
	}

	/**
	 * Generates a unique receipt number based on current time and a random number.
	 * The format is "RCP-" followed by the current timestamp and a random value.
	 * 
	 * @return A unique string identifier for the receipt
	 */
	private String generateReceiptNumber() {
		// Generate a unique receipt number based on current time
		return "RCP-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
	}

	/**
	 * Finds a receipt by its unique receipt number.
	 * 
	 * @param receiptNumber The receipt number to search for
	 * @return The matching Receipt object, or null if not found
	 */
	public static Receipt findReceiptByNumber(String receiptNumber) {
		if (database == null || receiptNumber == null) {
			return null;
		}
		
		for (Receipt receipt : database.getReceipts()) {
			if (receiptNumber.equals(receipt.getReceiptNumber())) {
				return receipt;
			}
		}
		return null;
	}

	/**
	 * Finds all receipts generated on a specific date.
	 * 
	 * @param date The LocalDate to search for
	 * @return ArrayList of Receipt objects generated on the specified date
	 */
	public static ArrayList<Receipt> findReceiptByDate(LocalDate date) {
		ArrayList<Receipt> result = new ArrayList<>();
		if (database == null || date == null) {
			return result;
		}
		
		for (Receipt receipt : database.getReceipts()) {
			if (date.equals(receipt.getDate())) {
				result.add(receipt);
			}
		}
		return result;
	}

	/**
	 * Finds the receipt for a specific booking.
	 * 
	 * @param booking The Booking entity to search for
	 * @return The matching Receipt object, or null if not found
	 */
	public static Receipt findReceiptByBooking(Booking booking) {
		if (database == null || booking == null) {
			return null;
		}
		
		for (Receipt receipt : database.getReceipts()) {
			if (booking.equals(receipt.getBooking())) {
				return receipt;
			}
		}
		return null;
	}

	/**
	 * Adds a receipt to the database if it doesn't already exist.
	 * 
	 * @param receipt The Receipt object to add to the database
	 * @return boolean True if the receipt was successfully added, false otherwise
	 */
	public static boolean addToDatabase(Receipt receipt) {
		if (database == null || receipt == null) {
			return false;
		}
		
		ArrayList<Receipt> receipts = database.getReceipts();
		if (receipts.contains(receipt)) {
			return false; // Already in database
		}
		
		receipts.add(receipt);
		return true;
	}

	/**
	 * Removes a receipt from the database.
	 * 
	 * @param receipt The Receipt object to remove from the database
	 * @return boolean True if the receipt was successfully removed, false otherwise
	 */
	public static boolean removeFromDatabase(Receipt receipt) {
		if (database == null || receipt == null) {
			return false;
		}
		
		ArrayList<Receipt> receipts = database.getReceipts();
		return receipts.remove(receipt);
	}

	/**
	 * Retrieves all receipts stored in the database.
	 * 
	 * @return ArrayList containing all Receipt objects in the database
	 */
	public static ArrayList<Receipt> getAllReceipts() {
		if (database == null) {
			return new ArrayList<>();
		}
		return database.getReceipts();
	}

	/**
	 * Returns a string representation of this receipt.
	 * Includes the receipt number, date, and booking information.
	 * The date is formatted as dd/MM/yyyy for readability.
	 * 
	 * @return String representation of the receipt
	 */
	@Override()
	public String toString() {
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return "Receipt [receiptNumber=" + receiptNumber + ", date=" + (date != null ? date.format(dateFormat) : "N/A") + ", booking=" + (booking != null ? booking.toString() : "None") + "]";
	}
}