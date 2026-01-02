package utils;

/**
 * Central repository for all application-wide constants used in the BTO Management System.
 * 
 * <p>This class provides a single source of truth for magic numbers, string literals,
 * and configuration values used throughout the application. By centralizing these values,
 * we improve maintainability and reduce the risk of inconsistencies.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * if (applicant.getAge() < ApplicationConstants.MIN_AGE_MARRIED_APPLICANT) {
 *     return false;
 * }
 * }</pre>
 * 
 * <h2>Categories:</h2>
 * <ul>
 *   <li>Age Requirements - Minimum age thresholds for different applicant types</li>
 *   <li>Password Validation - Password strength requirements</li>
 *   <li>File Paths - Default data file locations</li>
 *   <li>ID Prefixes - Prefixes used for generating unique identifiers</li>
 *   <li>Display Formatting - Console output formatting constants</li>
 * </ul>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 */
public final class ApplicationConstants {
    
    // ============================================================================
    // CONSTRUCTOR - Private to prevent instantiation
    // ============================================================================
    
    /**
     * Private constructor to prevent instantiation of this utility class.
     * 
     * @throws UnsupportedOperationException if instantiation is attempted
     */
    private ApplicationConstants() {
        throw new UnsupportedOperationException("ApplicationConstants is a utility class and cannot be instantiated");
    }
    
    // ============================================================================
    // AGE REQUIREMENTS
    // ============================================================================
    
    /**
     * Minimum age requirement for married applicants to apply for BTO flats.
     * Married couples must have at least one applicant aged 21 or above.
     */
    public static final int MIN_AGE_MARRIED_APPLICANT = 21;
    
    /**
     * Minimum age requirement for single applicants to apply for BTO flats.
     * Single applicants must be at least 35 years old to be eligible.
     */
    public static final int MIN_AGE_SINGLE_APPLICANT = 35;
    
    /**
     * Minimum age for any user to register in the system.
     * This is the absolute minimum age threshold for system registration.
     */
    public static final int MIN_REGISTRATION_AGE = 21;
    
    // ============================================================================
    // PASSWORD VALIDATION
    // ============================================================================
    
    /**
     * Minimum length required for user passwords.
     * Passwords must be at least this many characters long.
     */
    public static final int MIN_PASSWORD_LENGTH = 8;
    
    /**
     * Maximum length allowed for user passwords.
     * Passwords exceeding this length will be rejected.
     */
    public static final int MAX_PASSWORD_LENGTH = 128;
    
    // ============================================================================
    // FILE PATHS AND NAMES
    // ============================================================================
    
    /**
     * Default directory path for data files relative to the application root.
     * All CSV data files are expected to be in this directory.
     * Automatically detects whether running from project root or bin directory.
     */
    public static final String DEFAULT_DATA_PATH;
    
    static {
        // Determine correct data path at runtime based on working directory
        java.io.File localPath = new java.io.File("datafiles/");
        if (localPath.exists() && localPath.isDirectory()) {
            DEFAULT_DATA_PATH = "datafiles/";
        } else {
            DEFAULT_DATA_PATH = "../datafiles/";
        }
    }
    
    /**
     * Filename for applicant data storage.
     */
    public static final String APPLICANT_FILE = "ApplicantList.csv";
    
    /**
     * Filename for officer data storage.
     */
    public static final String OFFICER_FILE = "OfficerList.csv";
    
    /**
     * Filename for manager data storage.
     */
    public static final String MANAGER_FILE = "ManagerList.csv";
    
    /**
     * Filename for project data storage.
     */
    public static final String PROJECT_FILE = "ProjectList.csv";
    
    /**
     * Filename for BTO application data storage.
     */
    public static final String BTO_APPLICATION_FILE = "ApplicationList.csv";
    
    /**
     * Filename for officer application data storage.
     */
    public static final String OFFICER_APPLICATION_FILE = "OfficerApplicationList.csv";
    
    /**
     * Filename for booking data storage.
     */
    public static final String BOOKING_FILE = "BookingList.csv";
    
    /**
     * Filename for receipt data storage.
     */
    public static final String RECEIPT_FILE = "ReceiptList.csv";
    
    /**
     * Filename for enquiry data storage.
     */
    public static final String ENQUIRY_FILE = "EnquiryList.csv";
    
    // ============================================================================
    // ID PREFIXES - Used for generating unique identifiers
    // ============================================================================
    
    /**
     * Prefix for BTO application IDs.
     * Format: BTO-APP-{timestamp}-{random}
     */
    public static final String BTO_APPLICATION_ID_PREFIX = "BTO-APP-";
    
    /**
     * Prefix for officer application IDs.
     * Format: OFF-APP-{timestamp}-{random}
     */
    public static final String OFFICER_APPLICATION_ID_PREFIX = "OFF-APP-";
    
    /**
     * Prefix for booking IDs.
     * Format: BOOK-{timestamp}-{random}
     */
    public static final String BOOKING_ID_PREFIX = "BOOK-";
    
    /**
     * Prefix for receipt numbers.
     * Format: RCP-{timestamp}-{random}
     */
    public static final String RECEIPT_ID_PREFIX = "RCP-";
    
    /**
     * Prefix for enquiry IDs.
     * Format: ENQ-{timestamp}-{random}
     */
    public static final String ENQUIRY_ID_PREFIX = "ENQ-";
    
    // ============================================================================
    // NRIC VALIDATION
    // ============================================================================
    
    /**
     * Required length for Singapore NRIC numbers.
     * NRIC format: 1 letter + 7 digits + 1 letter = 9 characters
     */
    public static final int NRIC_LENGTH = 9;
    
    /**
     * Valid starting characters for Singapore NRIC numbers.
     * S/T = Citizens/PRs born before 2000
     * F/G = Foreigners
     */
    public static final String VALID_NRIC_PREFIXES = "STFG";
    
    // ============================================================================
    // DISPLAY FORMATTING
    // ============================================================================
    
    /**
     * Standard separator line for console output formatting.
     */
    public static final String SEPARATOR_LINE = "============================================================";
    
    /**
     * Standard dashed line for console output formatting.
     */
    public static final String DASHED_LINE = "------------------------------------------------------------";
    
    /**
     * Default width for formatted table columns.
     */
    public static final int DEFAULT_COLUMN_WIDTH = 20;
    
    // ============================================================================
    // SYSTEM LIMITS
    // ============================================================================
    
    /**
     * Maximum number of officers that can be assigned to a single project.
     * This is a system-wide limit, individual projects may have lower limits.
     */
    public static final int MAX_OFFICERS_PER_PROJECT = 10;
    
    /**
     * Maximum number of active applications an applicant can have at once.
     * Applicants can only have one active application at a time.
     */
    public static final int MAX_ACTIVE_APPLICATIONS_PER_APPLICANT = 1;
}
