package utils;

import entity.User;
import java.util.ArrayList;

/**
 * Utility class providing core authentication functionality for the BTO Management System.
 * 
 * <p>This class contains static methods for authenticating users against a list of
 * user entities. It provides the foundational authentication logic used by the
 * specific authentication implementations (ApplicantAuth, OfficerAuth, ManagerAuth).</p>
 * 
 * <h2>Authentication Process:</h2>
 * <ol>
 *   <li>Iterate through the provided list of users</li>
 *   <li>Compare NRIC (case-sensitive) and password</li>
 *   <li>Return the matching user or null if not found</li>
 * </ol>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * ArrayList<Applicant> applicants = Applicant.getAllApplicants();
 * User user = Auth.authenticate(applicants, "S1234567A", "password123");
 * if (user != null) {
 *     System.out.println("Welcome, " + user.getName());
 * }
 * }</pre>
 * 
 * <h2>Security Considerations:</h2>
 * <ul>
 *   <li>Passwords are compared in plain text (consider hashing for production)</li>
 *   <li>Failed attempts are not logged (consider adding audit logging)</li>
 *   <li>No rate limiting is implemented (consider adding for production)</li>
 * </ul>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see interfaces.IAuth
 * @see controller.AuthController
 */
public class Auth {
    
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Auth() {
        throw new UnsupportedOperationException("Auth is a utility class and cannot be instantiated");
    }
    
    /**
     * Authenticates a user by checking their NRIC and password against a list of users.
     * 
     * <p>This method iterates through the provided list of users and attempts to
     * find a match for both the NRIC and password. The comparison is case-sensitive
     * for passwords but NRIC matching uses the stored value directly.</p>
     * 
     * @param <T>      the type of user, must extend User
     * @param users    the list of users to check against, typically from a database
     * @param nric     the NRIC to look up in the user database
     * @param password the password to verify against the stored password
     * @return the authenticated User object if credentials match, {@code null} otherwise
     * 
     * @see User#getPassword()
     * @see User#getNric()
     */
    public static <T extends User> User authenticate(ArrayList<T> users, String nric, String password) {
        if (users == null || nric == null || password == null) {
            return null;
        }
        
        for (User user : users) {
            if (user.getNric().equals(nric) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
