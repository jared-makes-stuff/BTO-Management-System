package entity;

import enums.*;

/**
 * Abstract base class representing a user in the BTO Management System.
 * 
 * <p>This class provides the foundation for all user types in the system including
 * Applicants, Officers, and Managers. It manages essential personal information like
 * name, NRIC, age, and authentication credentials, as well as housing-related attributes
 * like marital status and search preferences.</p>
 * 
 * <h2>Inheritance Hierarchy:</h2>
 * <pre>
 * User (abstract)
 *   ├── {@link Applicant}
 *   │     └── {@link Officer}
 *   └── {@link Manager}
 * </pre>
 * 
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Store and manage personal information (name, NRIC, age)</li>
 *   <li>Handle authentication credentials (password)</li>
 *   <li>Track marital status for eligibility calculations</li>
 *   <li>Maintain user preferences via Filter object</li>
 * </ul>
 * 
 * <h2>Authentication:</h2>
 * <p>The {@link #authenticate(String)} method provides basic password verification.
 * Passwords are currently stored in plain text - consider implementing hashing
 * for production use.</p>
 * 
 * <h2>Design Notes:</h2>
 * <ul>
 *   <li>This class is abstract and cannot be instantiated directly</li>
 *   <li>All user types inherit common attributes and behaviors</li>
 *   <li>Subclasses implement specialized functionality</li>
 * </ul>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see Applicant
 * @see Officer  
 * @see Manager
 */
public abstract class User {

	// ============================================================================
	// INSTANCE VARIABLES
	// ============================================================================

	/**
	 * The full name of the user.
	 */
	protected String name;

	/**
	 * The NRIC (National Registration Identity Card) number.
	 * This serves as the unique identifier for each user in the system.
	 * Format: [STFG] + 7 digits + 1 letter (e.g., S1234567A)
	 */
	protected String nric;

	/**
	 * The age of the user in years.
	 * Used for eligibility calculations (e.g., minimum age requirements).
	 */
	protected int age;

	/**
	 * The password for account authentication.
	 * Note: Currently stored in plain text. Consider implementing hashing
	 * for production use.
	 */
	protected String password;

	/**
	 * The marital status of the user.
	 * Affects eligibility for certain flat types and age requirements.
	 * @see MarriageStatusEnum
	 */
	protected MarriageStatusEnum maritalStatus;

	/**
	 * User's personalized search preferences for filtering housing projects.
	 * @see Filter
	 */
	protected Filter filter;

	/**
	 * Default constructor that initializes a User with empty values.
	 * Primarily used for temporary objects or when details will be set later.
	 */
	public User() {
		this.name = "";
		this.nric = "";
		this.age = 0;
		this.password = "";
		this.maritalStatus = null;
		this.filter = null;
	}

	/**
	 * Creates a fully initialized User with all required personal information.
	 * 
	 * @param name The full name of the user
	 * @param nric The NRIC (National Registration Identity Card) number, a unique identifier for each user
	 * @param age The age of the user in years
	 * @param maritalStatus The current marital status (Single, Married, etc.)
	 * @param password The account password for secure authentication
	 * @param filter The personalized search preferences for filtering housing projects
	 */
	public User(String name, String nric, int age, MarriageStatusEnum maritalStatus, String password, Filter filter) {
		this.name = name;
		this.nric = nric;
		this.age = age;
		this.maritalStatus = maritalStatus;
		this.password = password;
		this.filter = filter;
	}

	/**
	 * Setters for modifying user attributes
	 */
	
	/**
	 * Updates the user's name in the system.
	 * 
	 * @param name The new name to assign to this user
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Updates the user's NRIC identification number.
	 * 
	 * @param nric The new NRIC number to assign to this user
	 */
	public void setNric(String nric) {
		this.nric = nric;
	}

	/**
	 * Updates the user's recorded age.
	 * 
	 * @param age The new age value in years
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Changes the user's account password.
	 * 
	 * @param password The new password for account access
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Updates the user's marital status, which may affect eligibility for certain housing options.
	 * 
	 * @param maritalStatus The new marital status to set
	 */
	public void setMaritalStatus(MarriageStatusEnum maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
	 * Updates the user's project search preferences.
	 * 
	 * @param filter The search filter containing criteria like location, flat type, and budget
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	/**
	 * Getters for accessing user attributes
	 */
	
	/**
	 * Gets the user's full name.
	 * 
	 * @return The name of this user
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the user's NRIC identification number.
	 * 
	 * @return The unique NRIC number of this user
	 */
	public String getNric() {
		return this.nric;
	}

	/**
	 * Gets the user's current age.
	 * 
	 * @return The age in years
	 */
	public int getAge() {
		return this.age;
	}

	/**
	 * Gets the user's account password.
	 * 
	 * @return The password string used for authentication
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Gets the user's current marital status.
	 * 
	 * @return The marital status as an enum value
	 */
	public MarriageStatusEnum getMaritalStatus() {
		return this.maritalStatus;
	}

	/**
	 * Gets the user's project search preferences.
	 * 
	 * @return The filter object containing the user's criteria for housing projects
	 */
	public Filter getFilter() {
		return this.filter;
	}

	/**
	 * User functionality methods
	 */
	
	/**
	 * Checks if the provided password matches the stored password.
	 * This is the core authentication method used during login.
	 * 
	 * @param password The password to verify against the stored credential
	 * @return true if the passwords match, false otherwise or if either password is null
	 */
	public boolean authenticate(String password) {
		if (this.password == null || password == null) {
			return false;
		}
		return this.password.equals(password);
	}

	/**
	 * Creates a human-readable string representation of this user.
	 * Useful for debugging, logging, and display purposes.
	 * 
	 * @return A formatted string containing the user's core information
	 */
	@Override()
	public String toString() {
		return "User [name=" + name + ", nric=" + nric + ", age=" + age + ", maritalStatus=" + maritalStatus + "]";
	}
}