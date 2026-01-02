package entity;
import database.*;
import enums.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The Booking class represents a flat booking in the BTO housing application system.
 * A booking is created when an applicant with a successful application selects a specific flat.
 * Bookings are processed by officers and can be in various states (pending, confirmed, cancelled).
 * This class manages the booking process including confirmation, cancellation, and receipt generation.
 */
public class Booking {

	/**
	 * The booking ID associated with this booking
	 */
	private String bookingID;
	/**
	 * The date and time when the booking was made
	 */
	private LocalDate bookingDateTime;
	/**
	 * The application associated with this booking
	 */
	private BTOApplication application;
	/**
	 * The officer processing this booking
	 */
	private Officer processingOfficer;
	/**
	 * The type of flat being booked
	 */
	private FlatTypeEnum flatType;
	/**
	 * The current status of the booking (PENDING, CONFIRMED, CANCELLED)
	 */
	private BookingStatusEnum status;
	/**
	 * Static database reference for Booking persistence
	 */
	private static BookingDatabase database;

	/**
	 * Default constructor that initializes a Booking with default values.
	 * Creates a pending booking with current date/time and null references.
	 */
	public Booking() {
		this.bookingDateTime = LocalDate.now();
		this.application = null;
		this.processingOfficer = null;
		this.flatType = null;
		this.status = BookingStatusEnum.PENDING;
		generateBookingID();
	}

	/**
	 * Full constructor that initializes a Booking with all specified values.
	 *
	 * @param bookingDateTime The date and time when the booking was made
	 * @param application The application associated with this booking
	 * @param processingOfficer The officer processing this booking
	 * @param flatType The type of flat being booked
	 * @param status The status of the booking
	 */
	public Booking(LocalDate bookingDateTime, BTOApplication application, Officer processingOfficer, FlatTypeEnum flatType, BookingStatusEnum status) {
		this.bookingDateTime = bookingDateTime != null ? bookingDateTime : LocalDate.now();
		this.application = application;
		this.processingOfficer = processingOfficer;
		this.flatType = flatType;
		this.status = status != null ? status : BookingStatusEnum.PENDING;
		generateBookingID();
	}

	/**
	 * Full constructor that initializes a Booking with all specified values.
	 *
	 * @param bookingID The booking ID associated with this booking
	 * @param bookingDateTime The date and time when the booking was made
	 * @param application The application associated with this booking
	 * @param processingOfficer The officer processing this booking
	 * @param flatType The type of flat being booked
	 * @param status The status of the booking
	 */
	public Booking(String bookingID, LocalDate bookingDateTime, BTOApplication application, Officer processingOfficer, FlatTypeEnum flatType, BookingStatusEnum status) {
		this.bookingID = bookingID;
		this.bookingDateTime = bookingDateTime != null ? bookingDateTime : LocalDate.now();
		this.application = application;
		this.processingOfficer = processingOfficer;
		this.flatType = flatType;
		this.status = status != null ? status : BookingStatusEnum.PENDING;
	}

	/**
	 * Private helper method to generate a unique booking ID.
	 * Uses system time and a random number to ensure uniqueness.
	 */
	private void generateBookingID() {
		// Generate a unique booking ID based on current time
		this.bookingID = "BOOK-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
	}

	/**
	 * Sets the booking ID associated for this booking.
	 *
	 * @param bookingID The booking ID associated with this application
	 */
	public void setBookingID(String bookingID) {
		this.bookingID = bookingID;
	}

	/**
	 * Sets the date and time when the booking was made.
	 *
	 * @param bookingDateTime The booking date and time
	 */
	public void setBookingDateTime(LocalDate bookingDateTime) {
		this.bookingDateTime = bookingDateTime;
	}

	/**
	 * Sets the application associated with this booking.
	 *
	 * @param application The BTO application
	 */
	public void setApplication(BTOApplication application) {
		this.application = application;
	}

	/**
	 * Sets the officer who is processing this booking.
	 *
	 * @param processingOfficer The officer handling this booking
	 */
	public void setProcessingOfficer(Officer processingOfficer) {
		this.processingOfficer = processingOfficer;
	}

	/**
	 * Sets the type of flat being booked.
	 *
	 * @param flatType The flat type
	 */
	public void setFlatType(FlatTypeEnum flatType) {
		this.flatType = flatType;
	}

	/**
	 * Sets the status of the booking.
	 *
	 * @param status The booking status
	 */
	public void setStatus(BookingStatusEnum status) {
		this.status = status;
	}

	/**
	 * Sets the static database reference for Booking persistence.
	 *
	 * @param database The database instance to use for Booking storage
	 */
	public static void setDatabase(BookingDatabase database) {
		Booking.database = database;
	}

	/**
	 * Gets the booking ID associated with this booking.
	 *
	 * @return The booking ID
	 */
	public String getBookingID() {
		return this.bookingID;
	}

	/**
	 * Gets the date and time when the booking was made.
	 *
	 * @return The booking date and time
	 */
	public LocalDate getBookingDateTime() {
		return this.bookingDateTime;
	}

	/**
	 * Gets the application associated with this booking.
	 *
	 * @return The BTO application
	 */
	public BTOApplication getApplication() {
		return this.application;
	}

	/**
	 * Gets the officer who is processing this booking.
	 *
	 * @return The processing officer
	 */
	public Officer getProcessingOfficer() {
		return this.processingOfficer;
	}

	/**
	 * Gets the type of flat being booked.
	 *
	 * @return The flat type
	 */
	public FlatTypeEnum getFlatType() {
		return this.flatType;
	}

	/**
	 * Gets the current status of the booking.
	 *
	 * @return The booking status
	 */
	public BookingStatusEnum getStatus() {
		return this.status;
	}

	/**
	 * Gets the database used for Booking storage.
	 *
	 * @return The Booking database instance
	 */
	public static BookingDatabase getDatabase() {
		return Booking.database;
	}

	/**
	 * Generates a receipt for a confirmed booking.
	 * Only confirmed bookings can have receipts generated.
	 *
	 * @return The generated Receipt, or null if the booking is not confirmed
	 */
	public Receipt generateReceipt() {
		if (this.status != BookingStatusEnum.CONFIRMED) {
			return null; // Can only generate receipt for confirmed bookings
		}

		Receipt receipt = new Receipt(null, LocalDate.now(), this);
		Receipt.addToDatabase(receipt);
		return receipt;
	}

	/**
	 * Cancels a pending booking.
	 * Updates the booking status to cancelled and marks the application as unsuccessful.
	 *
	 * @return true if the booking was cancelled successfully, false if it wasn't in pending status
	 */
	public boolean cancelBooking() {
		if (this.status == BookingStatusEnum.PENDING) {
			this.status = BookingStatusEnum.CANCELLED;

			// Update application status if applicable
			if (this.application != null) {
				this.application.setStatus(BTOApplicationStatusEnum.UNSUCCESSFUL);
			}

			return true;
		}
		return false;
	}

	/**
	 * Confirms a pending booking.
	 * Updates the booking status to confirmed, marks the application as successful,
	 * and decreases the available units count for the flat type.
	 *
	 * @return true if the booking was confirmed successfully, false if it wasn't in pending status
	 */
	public boolean confirmBooking() {
		if (this.status == BookingStatusEnum.PENDING) {
			this.status = BookingStatusEnum.CONFIRMED;

			// Update application status if applicable
			if (this.application != null) {
				this.application.setStatus(BTOApplicationStatusEnum.SUCCESSFUL);
			}

			// Decrease available units for this flat type in the project
			if (this.application != null && this.application.getProject() != null) {
				Project project = this.application.getProject();
				for (FlatType flatType : project.getFlatTypes()) {
					if (this.flatType == flatType.getType()) {
						flatType.decreaseAvailableUnits();
						break;
					}
				}
			}

			return true;
		}
		return false;
	}

	/**
	 * Finds all bookings made on a specific date.
	 *
	 * @param date The date to search for
	 * @return ArrayList of matching Bookings, empty list if none found
	 */
	public static ArrayList<Booking> findBookingByDate(LocalDate date) {
		ArrayList<Booking> result = new ArrayList<>();
		if (database == null || date == null) {
			return result;
		}

		for (Booking booking : database.getBookings()) {
			if (date.equals(booking.getBookingDateTime())) {
				result.add(booking);
			}
		}
		return result;
	}

	/**
	 * Finds a booking associated with a specific application.
	 *
	 * @param application The application to search for
	 * @return The matching Booking if found, null otherwise
	 */
	public static Booking findBookingByApplication(BTOApplication application) {
		if (database == null || application == null) {
			return null;
		}

		for (Booking booking : database.getBookings()) {
			if (application.equals(booking.getApplication())) {
				return booking;
			}
		}
		return null;
	}

	/**
	 * Finds all bookings processed by a specific officer.
	 *
	 * @param officer The officer to search for
	 * @return ArrayList of matching Bookings, empty list if none found
	 */
	public static ArrayList<Booking> findBookingsByOfficer(Officer officer) {
		ArrayList<Booking> result = new ArrayList<>();
		if (database == null || officer == null) {
			return result;
		}

		for (Booking booking : database.getBookings()) {
			if (officer.equals(booking.getProcessingOfficer())) {
				result.add(booking);
			}
		}
		return result;
	}

	/**
	 * Retrieves all bookings from the database.
	 *
	 * @return ArrayList of all Bookings in the database, empty list if none
	 */
	public static ArrayList<Booking> getAllBookings() {
		if (database == null) {
			return new ArrayList<>();
		}
		return database.getBookings();
	}

	/**
	 * Finds all bookings for a specific flat type.
	 *
	 * @param flatType The flat type to search for
	 * @return ArrayList of matching Bookings, empty list if none found
	 */
	public static ArrayList<Booking> findBookingsByFlatType(FlatTypeEnum flatType) {
		ArrayList<Booking> result = new ArrayList<>();
		if (database == null || flatType == null) {
			return result;
		}

		for (Booking booking : database.getBookings()) {
			if (flatType.equals(booking.getFlatType())) {
				result.add(booking);
			}
		}
		return result;
	}

	/**
	 * Finds all bookings with a specific status.
	 *
	 * @param status The status to search for
	 * @return ArrayList of matching Bookings, empty list if none found
	 */
	public static ArrayList<Booking> findBookingsByStatus(BookingStatusEnum status) {
		ArrayList<Booking> result = new ArrayList<>();
		if (database == null || status == null) {
			return result;
		}

		for (Booking booking : database.getBookings()) {
			if (status.equals(booking.getStatus())) {
				result.add(booking);
			}
		}
		return result;
	}

	/**
	 * Finds all bookings with a specific status.
	 *
	 * @param status The status to search for
	 * @return ArrayList of matching Bookings, empty list if none found
	 */
	public static ArrayList<Booking> findPendingBookingsByProject(Project project) {
		// Get pending bookings for the selected project
		ArrayList<Booking> pendingBookings = new ArrayList<>();
		ArrayList<Booking> allBookings = Booking.getAllBookings();
		
		for (Booking booking : allBookings) {
			if (booking.getStatus() == BookingStatusEnum.PENDING && 
					booking.getApplication().getProject().equals(project)) {
				pendingBookings.add(booking);
			}
		}
		return pendingBookings;
	}

	/**
	 * Adds a booking to the database.
	 *
	 * @param booking The booking to add
	 * @return true if the booking was added successfully, false otherwise
	 */
	public static boolean addToDatabase(Booking booking) {
		if (database == null || booking == null) {
			return false;
		}

		ArrayList<Booking> bookings = database.getBookings();
		if (bookings.contains(booking)) {
			return false; // Already in database
		}

		bookings.add(booking);
		return true;
	}

	/**
	 * Removes a booking from the database.
	 *
	 * @param booking The booking to remove
	 * @return true if the booking was removed successfully, false otherwise
	 */
	public static boolean removeFromDatabase(Booking booking) {
		if (database == null || booking == null) {
			return false;
		}

		ArrayList<Booking> bookings = database.getBookings();
		return bookings.remove(booking);
	}

	/**
	 * Creates a string representation of this booking.
	 *
	 * @return A string containing booking details
	 */
	@Override()
	public String toString() {
		String officer = (processingOfficer != null) ? processingOfficer.getName() : "N/A";
		return "Booking [bookingID=" + bookingID + ", datetime=" + bookingDateTime + ", application=" + (application != null ? application.toString() : "None") + ", officer=" + officer + ", flatType=" + flatType + ", status=" + status + "]";
	}
}