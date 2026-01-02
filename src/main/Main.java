package main;

import boundary.*;
import database.*;
import entity.*;
import utils.DisplayMenu;
import utils.FileHandler;

/**
 * Main entry point for the BTO Management System application.
 * 
 * <p>This class serves as the bootstrap for the entire application and is responsible
 * for initializing all system components including databases, entity-database connections,
 * and file handling capabilities.</p>
 * 
 * <h2>System Overview:</h2>
 * <p>The BTO (Build-To-Order) Management System is a comprehensive housing application
 * platform that enables:</p>
 * <ul>
 *   <li><strong>Applicants:</strong> Apply for BTO flats, submit enquiries, make bookings</li>
 *   <li><strong>Officers:</strong> Process bookings, handle enquiries, manage assigned projects</li>
 *   <li><strong>Managers:</strong> Create projects, approve applications, generate reports</li>
 * </ul>
 * 
 * <h2>Initialization Sequence:</h2>
 * <ol>
 *   <li>Create database instances for all entity types</li>
 *   <li>Attach databases to entity classes (Active Record pattern)</li>
 *   <li>Configure FileHandler with database references</li>
 *   <li>Display main menu for user interaction</li>
 * </ol>
 * 
 * <h2>Architecture:</h2>
 * <pre>
 * ┌─────────────────────────────────────────────────────────────────┐
 * │                        Boundary Layer                           │
 * │  (LoginView, ApplicantView, OfficerView, ManagerView, etc.)    │
 * ├─────────────────────────────────────────────────────────────────┤
 * │                       Controller Layer                          │
 * │  (ApplicantController, OfficerController, ManagerController)   │
 * ├─────────────────────────────────────────────────────────────────┤
 * │                         Entity Layer                            │
 * │  (User, Applicant, Officer, Manager, Project, BTOApplication)  │
 * ├─────────────────────────────────────────────────────────────────┤
 * │                        Database Layer                           │
 * │  (ApplicantDatabase, ProjectDatabase, BookingDatabase, etc.)   │
 * └─────────────────────────────────────────────────────────────────┘
 * </pre>
 * 
 * <h2>Usage:</h2>
 * <pre>{@code
 * // Run from command line:
 * java main.Main
 * 
 * // Or from IDE:
 * // Run Main.java as Java Application
 * }</pre>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see boundary.MainMenuView
 * @see utils.FileHandler
 */
public class Main {

	// ============================================================================
	// INSTANCE VARIABLES
	// ============================================================================

	/**
	 * Reference to the main menu view that serves as the primary user interface.
	 * This view is displayed after system initialization and provides access
	 * to all system functionality.
	 */
	private static MainMenuView mainMenuView;

	// ============================================================================
	// MAIN METHOD
	// ============================================================================

	/**
	 * Main entry point for the BTO Management System.
	 * 
	 * <p>This method performs the following steps:</p>
	 * <ol>
	 *   <li>Calls {@link #initialize()} to set up all system components</li>
	 *   <li>Displays the main menu for user interaction</li>
	 * </ol>
	 * 
	 * <p>The application continues running until the user chooses to exit
	 * from the main menu.</p>
	 * 
	 * @param args command line arguments (not used in current implementation)
	 */
	public static void main(String[] args) {
		initialize();
		DisplayMenu.displayMenu(mainMenuView);
	}

	// ============================================================================
	// INITIALIZATION
	// ============================================================================

	/**
	 * Initializes all components of the BTO Management System.
	 * 
	 * <p>This method performs comprehensive system initialization including:</p>
	 * 
	 * <h3>1. View Creation</h3>
	 * <p>Creates the MainMenuView which serves as the entry point for user interaction.</p>
	 * 
	 * <h3>2. Database Initialization</h3>
	 * <p>Creates instances of all database classes:</p>
	 * <ul>
	 *   <li>{@link ApplicantDatabase} - Stores applicant users</li>
	 *   <li>{@link OfficerDatabase} - Stores officer users</li>
	 *   <li>{@link ManagerDatabase} - Stores manager users</li>
	 *   <li>{@link ProjectDatabase} - Stores BTO projects</li>
	 *   <li>{@link BTOApplicationDatabase} - Stores BTO applications</li>
	 *   <li>{@link OfficerApplicationDatabase} - Stores officer project applications</li>
	 *   <li>{@link BookingDatabase} - Stores flat bookings</li>
	 *   <li>{@link ReceiptDatabase} - Stores booking receipts</li>
	 *   <li>{@link EnquiryDatabase} - Stores user enquiries</li>
	 * </ul>
	 * 
	 * <h3>3. Entity-Database Connections</h3>
	 * <p>Attaches database instances to entity classes using the Active Record pattern.
	 * This allows entities to perform database operations through static methods.</p>
	 * 
	 * <h3>4. FileHandler Configuration</h3>
	 * <p>Configures the FileHandler with all database references to enable
	 * data persistence through CSV file operations.</p>
	 * 
	 * <p><strong>Note:</strong> This method must be called before any user interaction
	 * or database operations can occur.</p>
	 * 
	 * @see FileHandler
	 */
	public static void initialize() {
		// Create main views
		mainMenuView = new MainMenuView();

		// Initialize database classes
		ApplicantDatabase applicantDatabase = new ApplicantDatabase();
		OfficerDatabase officerDatabase = new OfficerDatabase();
		ManagerDatabase managerDatabase = new ManagerDatabase();
		ProjectDatabase projectDatabase = new ProjectDatabase();
		BTOApplicationDatabase btoApplicationDatabase = new BTOApplicationDatabase();
		OfficerApplicationDatabase officerApplicationDatabase = new OfficerApplicationDatabase();
		BookingDatabase bookingDatabase = new BookingDatabase();
		ReceiptDatabase receiptDatabase = new ReceiptDatabase();
		EnquiryDatabase enquiryDatabase = new EnquiryDatabase();
		
		// Attach databases to entity classes (Active Record pattern)
		Applicant.setDatabase(applicantDatabase);
		Officer.setDatabase(officerDatabase);
		Manager.setDatabase(managerDatabase);
		Project.setDatabase(projectDatabase);
		BTOApplication.setDatabase(btoApplicationDatabase);
		OfficerApplication.setDatabase(officerApplicationDatabase);
		Booking.setDatabase(bookingDatabase);
		Receipt.setDatabase(receiptDatabase);
		Enquiry.setDatabase(enquiryDatabase);
		
		// Configure FileHandler with database references for data persistence
		FileHandler.setApplicantDatabase(applicantDatabase);
		FileHandler.setOfficerDatabase(officerDatabase);
		FileHandler.setManagerDatabase(managerDatabase);
		FileHandler.setProjectDatabase(projectDatabase);
		FileHandler.setBtoApplicationDatabase(btoApplicationDatabase);
		FileHandler.setOfficerApplicationDatabase(officerApplicationDatabase);
		FileHandler.setBookingDatabase(bookingDatabase);
		FileHandler.setReceiptDatabase(receiptDatabase);
		FileHandler.setEnquiryDatabase(enquiryDatabase);
		
		System.out.println("BTO Management System initialized successfully.");
	}
}