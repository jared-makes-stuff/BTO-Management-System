package boundary;

import controller.*;
import interfaces.*;
import utils.NricInputValidation;
import java.util.Scanner;

/**
 * Login view for the BTO Management System.
 * 
 * <p>This boundary class provides the user interface for authentication,
 * allowing users to enter their NRIC and password credentials. Upon successful
 * authentication, users are redirected to their role-specific menu.</p>
 * 
 * <h2>Supported User Types:</h2>
 * <ul>
 *   <li><strong>Applicant:</strong> Can apply for BTO flats, submit enquiries</li>
 *   <li><strong>Officer:</strong> Can process bookings, respond to enquiries</li>
 *   <li><strong>Manager:</strong> Can create projects, approve applications</li>
 * </ul>
 * 
 * <h2>Login Flow:</h2>
 * <pre>
 * ┌──────────────────────────────────────────────┐
 * │              LOGIN SCREEN                     │
 * ├──────────────────────────────────────────────┤
 * │  1. Enter NRIC (with format validation)      │
 * │  2. Enter Password                            │
 * │  3. Authenticate via AuthController           │
 * │  4. Redirect to role-specific menu            │
 * └──────────────────────────────────────────────┘
 * </pre>
 * 
 * <h2>NRIC Validation:</h2>
 * <p>The NRIC must conform to Singapore's format:</p>
 * <ul>
 *   <li>9 characters in length</li>
 *   <li>Starts with S, T, F, or G</li>
 *   <li>Followed by 7 digits</li>
 *   <li>Ends with an alphabetic character</li>
 * </ul>
 * 
 * <h2>Exit Mechanism:</h2>
 * <p>Users can type 'exit' at any prompt to return to the main menu.</p>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see AuthController
 * @see IDisplayable
 * @see NricInputValidation
 */
public class LoginView implements IDisplayable {

    // ============================================================================
    // INSTANCE VARIABLES
    // ============================================================================

    /**
     * Authentication controller for verifying user credentials.
     */
    private AuthController authController;
    
    /**
     * Scanner for reading user input from the console.
     */
    private Scanner scanner;

    // ============================================================================
    // CONSTRUCTORS
    // ============================================================================

    /**
     * Default constructor that initializes the login view.
     * 
     * <p>Sets up the Scanner for user input and creates a new
     * authentication controller instance.</p>
     */
    public LoginView() {
        this.scanner = new Scanner(System.in);
        this.authController = new AuthController();
    }

    // ============================================================================
    // IDisplayable IMPLEMENTATION
    // ============================================================================

    /**
     * Displays the login screen and processes user authentication.
     * 
     * <p>This method implements the main login loop:</p>
     * <ol>
     *   <li>Display login prompt</li>
     *   <li>Collect and validate NRIC</li>
     *   <li>Collect password</li>
     *   <li>Attempt authentication</li>
     *   <li>On success: redirect to user menu, then return here after logout</li>
     *   <li>On failure: display error and allow retry</li>
     * </ol>
     * 
     * <p>The loop continues until the user types 'exit' at either prompt.</p>
     */
    @Override
    public void display() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n===== LOGIN =====");
            
            String[] credentials = getCredentials();
            String nric = credentials[0];
            String password = credentials[1];
            
            // Exit condition - user typed 'exit'
            if (nric.equalsIgnoreCase("exit") || password.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the system...");
                exit = true;
                continue;
            }
            
            // Delegate authentication to AuthController
            boolean authenticated = authController.authenticate(nric, password);
            
            if (!authenticated) {
                System.out.println("Login failed. Invalid credentials.");
                System.out.println("Please try again or type 'exit' to quit.");
            } else {
                // User successfully logged in, used the system, and logged out
                System.out.println("You have been logged out.");
            }
        }
    }

    // ============================================================================
    // PRIVATE HELPER METHODS
    // ============================================================================

    /**
     * Collects and validates user credentials from console input.
     * 
     * <p>This method prompts the user for their NRIC and password,
     * with format validation for the NRIC. If an invalid NRIC is
     * entered, the user is prompted to re-enter until a valid
     * format is provided or 'exit' is typed.</p>
     * 
     * @return a String array where index 0 contains the NRIC and
     *         index 1 contains the password; either may be "exit"
     *         if the user chose to quit
     */
    private String[] getCredentials() {
        String[] credentials = new String[2];
        boolean validNRIC = false;
        
        // Get NRIC with validation
        System.out.print("Enter NRIC (or 'exit' to quit): ");
        credentials[0] = scanner.nextLine();
        
        if (!credentials[0].equalsIgnoreCase("exit")) {
            validNRIC = isValidNRIC(credentials[0]);
            
            // Keep asking until valid NRIC is provided
            while (!validNRIC) {
                System.out.println("Invalid NRIC format. NRIC must be 9 characters long, starting with S, T, F, or G.");
                System.out.print("Enter NRIC (or 'exit' to quit): ");
                credentials[0] = scanner.nextLine();
                
                if (credentials[0].equalsIgnoreCase("exit")) {
                    credentials[1] = "exit";
                    return credentials;
                }
                
                validNRIC = isValidNRIC(credentials[0]);
            }
            
            // Get password (no format validation, just collect input)
            System.out.print("Enter Password: ");
            credentials[1] = scanner.nextLine();
        } else {
            credentials[1] = "exit";
        }
        
        return credentials;
    }

    // ============================================================================
    // STATIC VALIDATION METHODS
    // ============================================================================

    /**
     * Validates the format of an NRIC string.
     * 
     * <p>Delegates to {@link NricInputValidation} for format checking.
     * The NRIC must:</p>
     * <ul>
     *   <li>Be exactly 9 characters long</li>
     *   <li>Start with S, T, F, or G (case-insensitive)</li>
     *   <li>Have 7 digits in the middle</li>
     *   <li>End with an alphabetic character</li>
     * </ul>
     * 
     * @param nric the NRIC string to validate
     * @return {@code true} if the NRIC format is valid; {@code false} otherwise
     * @see NricInputValidation#validateInput(String)
     */
    public static boolean isValidNRIC(String nric) {
        IInputValidation validator = new NricInputValidation();
        return validator.validateInput(nric);
    }
}