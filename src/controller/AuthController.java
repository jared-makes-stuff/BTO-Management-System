package controller;

import entity.*;
import interfaces.IAuth;

/**
 * Central authentication controller for the BTO Management System.
 * 
 * <p>This controller serves as the main entry point for user authentication,
 * handling credential validation and routing users to their appropriate views
 * based on their role (Applicant, Officer, or Manager).</p>
 * 
 * <h2>Authentication Flow:</h2>
 * <ol>
 *   <li>Validate input credentials (non-null, non-empty)</li>
 *   <li>Attempt authentication as Applicant using {@link ApplicantAuth}</li>
 *   <li>If not found, attempt authentication as Manager using {@link ManagerAuth}</li>
 *   <li>If not found, attempt authentication as Officer using {@link OfficerAuth}</li>
 *   <li>Return authentication result</li>
 * </ol>
 * 
 * <h2>Design Pattern:</h2>
 * <p>This class implements the <strong>Strategy Pattern</strong> for authentication,
 * delegating to specific authentication strategies ({@link IAuth} implementations)
 * based on user type. This allows for flexible extension of authentication
 * methods without modifying this controller.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * AuthController authController = new AuthController();
 * boolean success = authController.authenticate("S1234567A", "password123");
 * if (success) {
 *     // User authenticated and redirected to appropriate menu
 * } else {
 *     // Authentication failed - show error message
 * }
 * }</pre>
 * 
 * <h2>Security Notes:</h2>
 * <ul>
 *   <li>Password validation is delegated to specific auth controllers</li>
 *   <li>Failed attempts are logged with generic messages to prevent information leakage</li>
 *   <li>NRIC format validation occurs in individual auth controllers</li>
 * </ul>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see IAuth
 * @see ApplicantAuth
 * @see OfficerAuth
 * @see ManagerAuth
 */
public class AuthController {

    // ============================================================================
    // CONSTRUCTORS
    // ============================================================================

    /**
     * Default constructor for the authentication controller.
     * 
     * <p>Creates a new AuthController instance. This controller is stateless
     * and creates new authentication strategy instances for each authentication
     * attempt.</p>
     */
    public AuthController() {
        // Default constructor - stateless controller
    }

    // ============================================================================
    // PUBLIC METHODS
    // ============================================================================

    /**
     * Main authentication method that verifies credentials and redirects to appropriate view.
     * 
     * <p>This method attempts to authenticate the user against all user databases
     * in sequence: Applicant, Manager, then Officer. The first successful match
     * determines the user's role and triggers the appropriate menu display.</p>
     * 
     * <h3>Authentication Order:</h3>
     * <ol>
     *   <li><strong>Applicant:</strong> Standard housing applicants</li>
     *   <li><strong>Manager:</strong> Project managers with administrative privileges</li>
     *   <li><strong>Officer:</strong> Staff who process applications and bookings</li>
     * </ol>
     * 
     * <p><strong>Note:</strong> Officers are also Applicants (inheritance), so they
     * may be authenticated as Applicants first. The auth controllers handle this
     * by checking the most specific type first.</p>
     * 
     * @param nric     the NRIC identification number provided by the user;
     *                 must be non-null and non-empty
     * @param password the password provided by the user;
     *                 must be non-null and non-empty
     * @return {@code true} if authentication succeeds and user is redirected;
     *         {@code false} if authentication fails due to invalid credentials
     * 
     * @see ApplicantAuth#authenticate(String, String)
     * @see ManagerAuth#authenticate(String, String)
     * @see OfficerAuth#authenticate(String, String)
     */
    public boolean authenticate(String nric, String password) {
        // Validate inputs - fail fast on invalid credentials
        if (nric == null || password == null || nric.isEmpty() || password.isEmpty()) {
            System.out.println("Invalid credentials. Please try again.");
            return false;
        }
 
        // Try to authenticate as different user types using strategy pattern
        User user = null;
        if (user == null) user = authenticateUser(nric, password, new ApplicantAuth());
        if (user == null) user = authenticateUser(nric, password, new ManagerAuth());
        if (user == null) user = authenticateUser(nric, password, new OfficerAuth());

        return (user != null);
    }

    // ============================================================================
    // PRIVATE HELPER METHODS
    // ============================================================================

    /**
     * Helper method that delegates authentication to a specific strategy.
     * 
     * <p>This method implements the Strategy Pattern by accepting any
     * {@link IAuth} implementation and delegating the authentication
     * logic to it.</p>
     * 
     * @param nric           the NRIC to authenticate
     * @param password       the password to verify
     * @param authController the authentication strategy to use
     * @return the authenticated {@link User} if successful; {@code null} otherwise
     */
    private static User authenticateUser(String nric, String password, IAuth authController) {
        return authController.authenticate(nric, password);
    }
}