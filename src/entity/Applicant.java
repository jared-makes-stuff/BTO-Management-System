package entity;

import database.*;
import enums.*;
import java.util.*;

/**
 * Entity class representing an Applicant in the BTO Management System.
 * 
 * <p>The Applicant class represents a user who can apply for BTO (Build-To-Order) flats,
 * submit enquiries about projects, and make bookings. It extends the {@link User} class,
 * adding application, enquiry, and booking capabilities specific to housing applicants.</p>
 * 
 * <h2>Class Responsibilities:</h2>
 * <ul>
 *   <li>Maintain collections of BTO applications submitted by the applicant</li>
 *   <li>Track enquiries submitted about various projects</li>
 *   <li>Manage flat bookings associated with successful applications</li>
 *   <li>Provide search functionality for finding applicants in the database</li>
 * </ul>
 * 
 * <h2>Inheritance Hierarchy:</h2>
 * <pre>
 * {@link User}
 *   └── Applicant
 *         └── {@link Officer} (Officers can also apply as applicants)
 * </pre>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Create a new applicant
 * Applicant applicant = new Applicant();
 * applicant.setName("John Doe");
 * applicant.setNric("S1234567A");
 * applicant.setAge(30);
 * applicant.setMaritalStatus(MarriageStatusEnum.MARRIED);
 * 
 * // Add to database
 * Applicant.addToDatabase(applicant);
 * 
 * // Find applicant by NRIC
 * Applicant found = Applicant.findApplicantByNRIC("S1234567A");
 * }</pre>
 * 
 * <h2>Design Pattern:</h2>
 * <p>This class uses the Active Record pattern where entity objects contain
 * both data and database operations. Static methods provide database access
 * while instance methods manage the entity's state.</p>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see User
 * @see BTOApplication
 * @see Enquiry
 * @see Booking
 */
public class Applicant extends User {

	// ============================================================================
	// INSTANCE VARIABLES
	// ============================================================================

	/**
	 * Collection of BTO applications submitted by this applicant.
	 * 
	 * <p>Each applicant can have multiple applications over time, though
	 * only one active (non-withdrawn) application is typically allowed at a time.</p>
	 * 
	 * @see BTOApplication
	 */
	protected ArrayList<BTOApplication> applications;
	
	/**
	 * Collection of enquiries submitted by this applicant.
	 * 
	 * <p>Enquiries are questions or requests for information about
	 * BTO projects that can be answered by officers or managers.</p>
	 * 
	 * @see Enquiry
	 */
	protected ArrayList<Enquiry> enquiries;
	
	/**
	 * Collection of flat bookings made by this applicant.
	 * 
	 * <p>Bookings are created when an applicant with a successful
	 * application proceeds to select and book a specific flat unit.</p>
	 * 
	 * @see Booking
	 */
	protected ArrayList<Booking> bookings;
	
	// ============================================================================
	// STATIC VARIABLES
	// ============================================================================

	/**
	 * Static reference to the database containing all applicants.
	 * 
	 * <p>This reference must be set during application initialization
	 * before any database operations can be performed.</p>
	 * 
	 * @see #setDatabase(ApplicantDatabase)
	 * @see #getDatabase()
	 */
	private static ApplicantDatabase database;

	// ============================================================================
	// CONSTRUCTORS
	// ============================================================================

	/**
	 * Default constructor that initializes an Applicant with empty collections.
	 * 
	 * <p>Creates a new Applicant with:</p>
	 * <ul>
	 *   <li>Empty applications list</li>
	 *   <li>Empty enquiries list</li>
	 *   <li>Empty bookings list</li>
	 *   <li>Default User fields (empty name, NRIC, etc.)</li>
	 * </ul>
	 * 
	 * @see User#User()
	 */
	public Applicant() {
		super();
		this.applications = new ArrayList<>();
		this.enquiries = new ArrayList<>();
		this.bookings = new ArrayList<>();
	}

	/**
	 * Parameterized constructor that initializes an Applicant with all attributes.
	 * 
	 * <p>Creates a fully initialized Applicant with all personal information,
	 * authentication credentials, and associated collections.</p>
	 * 
	 * @param name          the full name of the applicant
	 * @param nric          the NRIC (National Registration Identity Card) number,
	 *                      must be in valid Singapore NRIC format
	 * @param age           the age of the applicant in years, must be positive
	 * @param maritalStatus the marital status (SINGLE or MARRIED)
	 * @param password      the password for account authentication
	 * @param filter        the search filter preferences for project browsing
	 * @param applications  the list of BTO applications submitted by this applicant,
	 *                      can be null (will be treated as empty list)
	 * @param enquiries     the list of enquiries submitted by this applicant,
	 *                      can be null (will be treated as empty list)
	 * @param bookings      the list of bookings made by this applicant,
	 *                      can be null (will be treated as empty list)
	 * 
	 * @see User#User(String, String, int, MarriageStatusEnum, String, Filter)
	 */
	public Applicant(String name, String nric, int age, MarriageStatusEnum maritalStatus, 
			String password, Filter filter, ArrayList<BTOApplication> applications, 
			ArrayList<Enquiry> enquiries, ArrayList<Booking> bookings) {
		super(name, nric, age, maritalStatus, password, filter);
		this.applications = applications != null ? applications : new ArrayList<>();
		this.enquiries = enquiries != null ? enquiries : new ArrayList<>();
		this.bookings = bookings != null ? bookings : new ArrayList<>();
	}

	// ============================================================================
	// SETTERS
	// ============================================================================

	/**
	 * Sets a single BTO application, replacing any existing applications.
	 * 
	 * <p><strong>Note:</strong> This method clears all existing applications
	 * before adding the new one. For adding without clearing, use
	 * {@link #addApplication(BTOApplication)}.</p>
	 * 
	 * @param application the BTO application to set, or null to clear all applications
	 * @see #addApplication(BTOApplication)
	 * @see #getApplications()
	 */
	public void setApplication(BTOApplication application) {
		this.applications.clear();
		if (application != null) {
			this.applications.add(application);
		}
	}

	/**
	 * Sets the complete list of applications for this applicant.
	 * 
	 * @param applications the list of BTO applications to set
	 * @see #addApplication(BTOApplication)
	 */
	public void setApplications(ArrayList<BTOApplication> applications) {
		this.applications = applications != null ? applications : new ArrayList<>();
	}

	/**
	 * Sets the list of enquiries for this applicant.
	 * 
	 * @param enquiries the list of enquiries to set
	 * @see #addEnquiry(Enquiry)
	 */
	public void setEnquiries(ArrayList<Enquiry> enquiries) {
		this.enquiries = enquiries != null ? enquiries : new ArrayList<>();
	}

	/**
	 * Sets the list of bookings for this applicant.
	 * 
	 * @param bookings the list of bookings to set
	 * @see #addBooking(Booking)
	 */
	public void setBookings(ArrayList<Booking> bookings) {
		this.bookings = bookings != null ? bookings : new ArrayList<>();
	}

	/**
	 * Sets the static database reference for the Applicant class.
	 * 
	 * <p><strong>Important:</strong> This method must be called during
	 * application initialization before any database operations are performed.</p>
	 * 
	 * @param database the ApplicantDatabase instance to use for all Applicant operations
	 * @see #getDatabase()
	 */
	public static void setDatabase(ApplicantDatabase database) {
		Applicant.database = database;
	}

	// ============================================================================
	// GETTERS
	// ============================================================================

	/**
	 * Returns the list of BTO applications submitted by this applicant.
	 * 
	 * @return ArrayList of BTO applications, never null
	 * @see #addApplication(BTOApplication)
	 * @see #setApplication(BTOApplication)
	 */
	public ArrayList<BTOApplication> getApplications() {
		return this.applications;
	}

	/**
	 * Returns the list of enquiries submitted by this applicant.
	 * 
	 * @return ArrayList of enquiries, never null
	 * @see #addEnquiry(Enquiry)
	 * @see #setEnquiries(ArrayList)
	 */
	public ArrayList<Enquiry> getEnquiries() {
		return this.enquiries;
	}

	/**
	 * Returns the list of bookings made by this applicant.
	 * 
	 * @return ArrayList of bookings, never null
	 * @see #addBooking(Booking)
	 * @see #setBookings(ArrayList)
	 */
	public ArrayList<Booking> getBookings() {
		return this.bookings;
	}

	/**
	 * Returns the list of bookings made by this applicant.
	 * 
	 * @return ArrayList of bookings, never null
	 * @deprecated Use {@link #getBookings()} instead. This method is retained
	 *             for backward compatibility.
	 */
	@Deprecated
	public ArrayList<Booking> getBooking() {
		return this.bookings;
	}

	/**
	 * Returns the static database reference containing all applicants.
	 * 
	 * @return the ApplicantDatabase instance, or null if not initialized
	 * @see #setDatabase(ApplicantDatabase)
	 */
	public static ApplicantDatabase getDatabase() {
		return Applicant.database;
	}

	// ============================================================================
	// COLLECTION MANAGEMENT METHODS
	// ============================================================================

	/**
	 * Adds a BTO application to this applicant's list of applications.
	 * 
	 * <p>If the application is null, this method does nothing.</p>
	 * 
	 * @param application the BTO application to add
	 * @see #getApplications()
	 * @see #setApplication(BTOApplication)
	 */
	public void addApplication(BTOApplication application) {
		if (application != null && this.applications != null) {
			this.applications.add(application);
		}
	}

	/**
	 * Adds an enquiry to this applicant's list of enquiries.
	 * 
	 * <p>If the enquiry is null, this method does nothing.</p>
	 * 
	 * @param enquiry the enquiry to add
	 * @see #getEnquiries()
	 * @see #setEnquiries(ArrayList)
	 */
	public void addEnquiry(Enquiry enquiry) {
		if (enquiry != null && this.enquiries != null) {
			this.enquiries.add(enquiry);
		}
	}

	/**
	 * Adds a booking to this applicant's list of bookings.
	 * 
	 * <p>If the booking is null, this method does nothing.</p>
	 * 
	 * @param booking the booking to add
	 * @see #getBookings()
	 * @see #setBookings(ArrayList)
	 */
	public void addBooking(Booking booking) {
		if (booking != null && this.bookings != null) {
			this.bookings.add(booking);
		}
	}

	// ============================================================================
	// STATIC FINDER METHODS
	// ============================================================================

	/**
	 * Finds and returns an applicant by their name.
	 * 
	 * <p><strong>Note:</strong> This performs an exact match on the full name.
	 * If multiple applicants have the same name, only the first match is returned.</p>
	 * 
	 * @param name the name of the applicant to search for
	 * @return the matching Applicant, or {@code null} if not found or database not initialized
	 * @see #findApplicantByNRIC(String)
	 */
	public static Applicant findApplicantByName(String name) {
		if (database == null || name == null) {
			return null;
		}
		
		for (Applicant applicant : database.getApplicants()) {
			if (name.equals(applicant.getName())) {
				return applicant;
			}
		}
		return null;
	}

	/**
	 * Finds and returns an applicant by their NRIC.
	 * 
	 * @param nric The NRIC of the applicant to search for
	 * @return The matching Applicant, or null if not found
	 */
	public static Applicant findApplicantByNRIC(String nric) {
		if (database == null || nric == null) {
			return null;
		}
		
		for (Applicant applicant : database.getApplicants()) {
			if (nric.equals(applicant.getNric())) {
				return applicant;
			}
		}
		return null;
	}

	/**
	 * Finds and returns all applicants of a specific age.
	 * 
	 * @param age The age to search for
	 * @return ArrayList of matching Applicants
	 */
	public static ArrayList<Applicant> findApplicantsByAge(int age) {
		ArrayList<Applicant> result = new ArrayList<>();
		if (database == null) {
			return result;
		}
		
		for (Applicant applicant : database.getApplicants()) {
			if (applicant.getAge() == age) {
				result.add(applicant);
			}
		}
		return result;
	}

	/**
	 * Finds and returns the applicant associated with a specific BTO application.
	 * 
	 * @param application The BTO application to search for
	 * @return The matching Applicant, or null if not found
	 */
	public static Applicant findApplicantByApplication(BTOApplication application) {
		if (database == null || application == null) {
			return null;
		}
		
		for (Applicant applicant : database.getApplicants()) {
			ArrayList<BTOApplication> apps = applicant.getApplications();
			if (apps.contains(application)) {
				return applicant;
			}
		}
		return null;
	}

	/**
	 * Finds and returns the applicant associated with a specific enquiry.
	 * 
	 * @param enquiry The enquiry to search for
	 * @return The matching Applicant, or null if not found
	 */
	public static Applicant findApplicantByEnquiry(Enquiry enquiry) {
		if (database == null || enquiry == null) {
			return null;
		}
		
		for (Applicant applicant : database.getApplicants()) {
			ArrayList<Enquiry> enqs = applicant.getEnquiries();
			if (enqs.contains(enquiry)) {
				return applicant;
			}
		}
		return null;
	}

	/**
	 * Finds and returns all applicants with a specific marital status.
	 * 
	 * @param status The marital status to search for
	 * @return ArrayList of matching Applicants
	 */
	public static ArrayList<Applicant> findApplicantsByMaritalStatus(MarriageStatusEnum status) {
		ArrayList<Applicant> result = new ArrayList<>();
		if (database == null || status == null) {
			return result;
		}
		
		for (Applicant applicant : database.getApplicants()) {
			if (status.equals(applicant.getMaritalStatus())) {
				result.add(applicant);
			}
		}
		return result;
	}

	/**
	 * Adds an applicant to the applicant database.
	 * 
	 * This static method adds the specified applicant to the database.
	 * If the database hasn't been initialized, the method will return false.
	 * @param applicant The applicant to add to the database
	 * @return True if the applicant was successfully added, false otherwise
	 */
	public static boolean addToDatabase(Applicant applicant) {
		if (database == null || applicant == null) {
			return false;
		}
		
		ArrayList<Applicant> applicants = database.getApplicants();
		if (applicants.contains(applicant)) {
			return false; // Already in database
		}
		
		applicants.add(applicant);
		return true;
	}

	/**
	 * Removes an applicant from the applicant database.
	 * 
	 * This static method removes the specified applicant from the database.
	 * If the database hasn't been initialized, the method will return false.
	 * @param applicant The applicant to remove from the database
	 * @return True if the applicant was successfully removed, false otherwise
	 */
	public static boolean removeFromDatabase(Applicant applicant) {
		if (database == null || applicant == null) {
			return false;
		}
		
		ArrayList<Applicant> applicants = database.getApplicants();
		return applicants.remove(applicant);
	}

	/**
	 * Returns all applicants stored in the database.
	 * 
	 * @return ArrayList containing all applicants, or an empty list if the database is not initialized
	 */
	public static ArrayList<Applicant> getAllApplicants() {
		if (database == null) {
			return new ArrayList<>();
		}
		return database.getApplicants();
	}

	// ============================================================================
	// OBJECT METHODS
	// ============================================================================

	/**
	 * Returns a string representation of this Applicant.
	 * 
	 * <p>The string includes the applicant's name, NRIC, age, marital status,
	 * number of applications, and number of enquiries.</p>
	 * 
	 * @return a formatted string representation of this Applicant
	 */
	@Override
	public String toString() {
		return "Applicant [name=" + getName() + ", nric=" + getNric() + 
			", age=" + getAge() + ", maritalStatus=" + getMaritalStatus() + 
			", applications=" + applications.size() + 
			", enquiries=" + enquiries.size() + "]";
	}
}