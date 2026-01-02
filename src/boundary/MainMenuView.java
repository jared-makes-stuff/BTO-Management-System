package boundary;

import boundary.filehandlerview.*;
import interfaces.*;
import java.util.Scanner;

/**
 * Main entry point view for the BTO Management System user interface.
 * 
 * <p>This view serves as the primary navigation hub for the entire application,
 * providing access to data file management and user authentication. It is the
 * first screen users see when launching the application.</p>
 * 
 * <h2>Menu Structure:</h2>
 * <pre>
 * ┌─────────────────────────────────────────────────────────────┐
 * │              BTO MANAGEMENT SYSTEM                          │
 * ├─────────────────────────────────────────────────────────────┤
 * │  1. Handle Data Files  →  Import/Export CSV data            │
 * │  2. Login              →  Authenticate and access system    │
 * │  0. Exit Program       →  Terminate application             │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 * 
 * <h2>Typical Usage Flow:</h2>
 * <ol>
 *   <li><strong>First-time Setup:</strong> Select "Handle Data Files" to import
 *       initial data from CSV files</li>
 *   <li><strong>Login:</strong> Select "Login" to authenticate with NRIC and password</li>
 *   <li><strong>Use System:</strong> Access role-specific features</li>
 *   <li><strong>Save Data:</strong> Before exiting, export data to preserve changes</li>
 *   <li><strong>Exit:</strong> Select "Exit Program" to terminate</li>
 * </ol>
 * 
 * <h2>Component Views:</h2>
 * <ul>
 *   <li>{@link FileHandlerView} - CSV file import/export operations</li>
 *   <li>{@link LoginView} - User authentication interface</li>
 * </ul>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see FileHandlerView
 * @see LoginView
 * @see IDisplayable
 */
public class MainMenuView implements IDisplayable {

	// ============================================================================
	// INSTANCE VARIABLES
	// ============================================================================

	/**
	 * View for handling data file import/export operations.
	 */
	private FileHandlerView fileHandlerView;
	
	/**
	 * View for user authentication.
	 */
	private LoginView loginView;
	
	/**
	 * Scanner for reading user input from the console.
	 */
	private Scanner scanner;

	// ============================================================================
	// CONSTRUCTORS
	// ============================================================================

	/**
	 * Default constructor that initializes the main menu view.
	 * 
	 * <p>Sets up the Scanner for user input and creates instances of
	 * the file handler and login sub-views.</p>
	 */
	public MainMenuView() {
		this.scanner = new Scanner(System.in);
		this.fileHandlerView = new FileHandlerView();
		this.loginView = new LoginView();
	}

	// ============================================================================
	// IDisplayable IMPLEMENTATION
	// ============================================================================

	/**
	 * Displays the main menu and handles user navigation.
	 * 
	 * <p>This method implements the main application loop, continuously
	 * displaying the menu and processing user selections until the
	 * user chooses to exit (option 0).</p>
	 * 
	 * <h3>Menu Options:</h3>
	 * <ul>
	 *   <li><strong>1 - Handle Data Files:</strong> Opens the file management interface
	 *       for importing/exporting CSV data</li>
	 *   <li><strong>2 - Login:</strong> Opens the login screen for user authentication</li>
	 *   <li><strong>0 - Exit Program:</strong> Terminates the application</li>
	 * </ul>
	 */
	@Override
	public void display() {
		boolean exit = false;
		
		while (!exit) {
			System.out.println("===== BTO MANAGEMENT SYSTEM =====");
			System.out.println("1. Handle Data Files");
			System.out.println("2. Login");
			System.out.println("0. Exit Program");
			System.out.print("Enter your choice: ");
			
			String input = scanner.nextLine();
			exit = getUserInput(input);
		}
	}

	// ============================================================================
	// INPUT PROCESSING
	// ============================================================================

	/**
	 * Processes user input from the main menu.
	 * 
	 * <p>Routes to appropriate sub-views based on the user's selection.
	 * Invalid inputs display an error message and return to the menu.</p>
	 * 
	 * @param input the menu option selected by the user (as a String)
	 * @return {@code true} if the user chooses to exit (option "0");
	 *         {@code false} otherwise to continue the menu loop
	 */
	public boolean getUserInput(String input) {
		switch (input) {
			case "1":
				showFileHandlerMenu();
				return false;
			case "2":
				showLoginMenu();
				return false;
			case "0":
				System.out.println("Exiting program...");
				return true;
			default:
				System.out.println("Invalid choice. Please try again.");
				return false;
		}
	}

	// ============================================================================
	// NAVIGATION METHODS
	// ============================================================================

	/**
	 * Displays the file handler sub-menu.
	 * 
	 * <p>Navigates to the {@link FileHandlerView} which provides options
	 * for reading and writing CSV data files.</p>
	 */
	private void showFileHandlerMenu() {
		fileHandlerView.display();
	}

	/**
	 * Displays the login screen.
	 * 
	 * <p>Navigates to the {@link LoginView} which handles user authentication
	 * and routes users to their role-specific interfaces after successful login.</p>
	 */
	private void showLoginMenu() {
		loginView.display();
	}

	// ============================================================================
	// GETTERS AND SETTERS
	// ============================================================================

	/**
	 * Sets the file handler view component.
	 * 
	 * @param fileHandlerView the file handler view to use
	 */
	public void setFileHandlerView(FileHandlerView fileHandlerView) {
		this.fileHandlerView = fileHandlerView;
	}

	/**
	 * Sets the login view component.
	 * 
	 * @param loginView the login view to use
	 */
	public void setLoginView(LoginView loginView) {
		this.loginView = loginView;
	}

	/**
	 * Gets the file handler view component.
	 * 
	 * @return the current file handler view
	 */
	public FileHandlerView getFileHandlerView() {
		return this.fileHandlerView;
	}

	/**
	 * Gets the login view component.
	 * 
	 * @return the current login view
	 */
	public LoginView getLoginView() {
		return this.loginView;
	}
}