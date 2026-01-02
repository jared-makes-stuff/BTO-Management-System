package interfaces;

import entity.User;

/**
 * Interface defining the authentication contract for user login operations.
 * 
 * <p>This interface establishes the standard authentication mechanism used
 * throughout the BTO Management System. Implementations handle user credential
 * validation and return the authenticated user entity on success.</p>
 * 
 * <h2>Implementation Classes:</h2>
 * <ul>
 *   <li>{@link controller.ApplicantAuth} - Authenticates applicant users</li>
 *   <li>{@link controller.OfficerAuth} - Authenticates officer users</li>
 *   <li>{@link controller.ManagerAuth} - Authenticates manager users</li>
 * </ul>
 * 
 * <h2>Authentication Flow:</h2>
 * <ol>
 *   <li>User provides NRIC and password</li>
 *   <li>Implementation searches the appropriate user database</li>
 *   <li>Credentials are validated against stored values</li>
 *   <li>Authenticated User object is returned, or null on failure</li>
 * </ol>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * IAuth authenticator = new ApplicantAuth();
 * User user = authenticator.authenticate("S1234567A", "password123");
 * if (user != null) {
 *     // Authentication successful
 *     displayUserMenu(user);
 * }
 * }</pre>
 * 
 * <h2>Security Notes:</h2>
 * <ul>
 *   <li>Passwords are currently compared in plain text</li>
 *   <li>Consider implementing password hashing for production use</li>
 *   <li>Failed authentication attempts should be logged</li>
 * </ul>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see entity.User
 * @see controller.AuthController
 */
public interface IAuth {
    
    /**
     * Authenticates a user based on NRIC and password credentials.
     * 
     * <p>This method verifies the provided credentials against the user database
     * and returns the authenticated user entity if successful.</p>
     * 
     * @param nric     the NRIC (National Registration Identity Card) number,
     *                 must be in valid Singapore NRIC format
     * @param password the user's password for authentication
     * @return the authenticated {@link User} object if credentials are valid,
     *         {@code null} if authentication fails or user not found
     * @see entity.User#authenticate(String)
     */
    User authenticate(String nric, String password);
}
