package utils;

import java.util.regex.Pattern;

/**
 * Centralized validation utility class for the BTO Management System.
 * 
 * <p>This class provides static methods for validating various types of user input
 * and data throughout the application. All validation logic is consolidated here
 * to ensure consistency and maintainability.</p>
 * 
 * <h2>Validation Categories:</h2>
 * <ul>
 *   <li>Password Validation - Strength and format requirements</li>
 *   <li>NRIC Validation - Singapore National Registration Identity Card format</li>
 *   <li>Age Validation - Age requirements for different user types</li>
 *   <li>String Validation - Common string format checks</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Validate password
 * ValidationResult result = ValidationUtils.validatePassword("mypassword123");
 * if (!result.isValid()) {
 *     System.out.println(result.getMessage());
 * }
 * 
 * // Validate NRIC
 * boolean isValidNric = ValidationUtils.isValidNRIC("S1234567A");
 * }</pre>
 * 
 * <h2>Design Principles:</h2>
 * <ul>
 *   <li>All methods are static - no instance required</li>
 *   <li>Methods return boolean or ValidationResult for detailed feedback</li>
 *   <li>Null-safe - all methods handle null inputs gracefully</li>
 * </ul>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see ApplicationConstants
 */
public final class ValidationUtils {
    
    // ============================================================================
    // REGEX PATTERNS - Compiled once for performance
    // ============================================================================
    
    /**
     * Pattern for validating Singapore NRIC format.
     * Format: [STFG] followed by 7 digits and ending with an alphabetic letter.
     */
    private static final Pattern NRIC_PATTERN = Pattern.compile("^[STFG]\\d{7}[A-Z]$");
    
    /**
     * Pattern for validating email addresses.
     * Standard email format with local part, @ symbol, and domain.
     */
    @SuppressWarnings("unused")
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    /**
     * Pattern for detecting special characters in passwords.
     * Used for password strength assessment.
     */
    @SuppressWarnings("unused")
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
    
    // ============================================================================
    // CONSTRUCTOR - Private to prevent instantiation
    // ============================================================================
    
    /**
     * Private constructor to prevent instantiation of this utility class.
     * 
     * @throws UnsupportedOperationException if instantiation is attempted
     */
    private ValidationUtils() {
        throw new UnsupportedOperationException("ValidationUtils is a utility class and cannot be instantiated");
    }
    
    // ============================================================================
    // PASSWORD VALIDATION
    // ============================================================================
    
    /**
     * Validates a password against the system's minimum requirements.
     * 
     * <p>Current requirements:</p>
     * <ul>
     *   <li>Minimum length of {@link ApplicationConstants#MIN_PASSWORD_LENGTH} characters</li>
     *   <li>Not null or empty</li>
     * </ul>
     * 
     * @param password the password string to validate
     * @return {@code true} if the password meets minimum requirements, {@code false} otherwise
     * @see #validatePasswordStrength(String) for comprehensive password validation
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= ApplicationConstants.MIN_PASSWORD_LENGTH;
    }
    
    /**
     * Performs comprehensive password strength validation.
     * 
     * <p>This method checks for:</p>
     * <ul>
     *   <li>Minimum length requirement</li>
     *   <li>Maximum length limit</li>
     *   <li>Presence of uppercase letters (optional, logged as recommendation)</li>
     *   <li>Presence of lowercase letters (optional, logged as recommendation)</li>
     *   <li>Presence of digits (optional, logged as recommendation)</li>
     * </ul>
     * 
     * @param password the password string to validate
     * @return a {@link ValidationResult} containing validation status and detailed message
     */
    public static ValidationResult validatePasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return new ValidationResult(false, "Password cannot be null or empty");
        }
        
        if (password.length() < ApplicationConstants.MIN_PASSWORD_LENGTH) {
            return new ValidationResult(false, 
                "Password must be at least " + ApplicationConstants.MIN_PASSWORD_LENGTH + " characters long");
        }
        
        if (password.length() > ApplicationConstants.MAX_PASSWORD_LENGTH) {
            return new ValidationResult(false, 
                "Password cannot exceed " + ApplicationConstants.MAX_PASSWORD_LENGTH + " characters");
        }
        
        // Build strength assessment message with recommendations
        StringBuilder strengthNotes = new StringBuilder("Password accepted.");
        
        if (!password.chars().anyMatch(Character::isUpperCase)) {
            strengthNotes.append(" Consider adding uppercase letters for stronger security.");
        }
        
        if (!password.chars().anyMatch(Character::isLowerCase)) {
            strengthNotes.append(" Consider adding lowercase letters.");
        }
        
        if (!password.chars().anyMatch(Character::isDigit)) {
            strengthNotes.append(" Consider adding numbers.");
        }
        
        return new ValidationResult(true, strengthNotes.toString());
    }
    
    // ============================================================================
    // NRIC VALIDATION
    // ============================================================================
    
    /**
     * Validates a Singapore NRIC (National Registration Identity Card) number format.
     * 
     * <p>Valid NRIC format:</p>
     * <ul>
     *   <li>Exactly 9 characters long</li>
     *   <li>First character: S, T, F, or G</li>
     *   <li>Characters 2-8: Seven digits</li>
     *   <li>Last character: An alphabetic letter</li>
     * </ul>
     * 
     * <p>Note: This method only validates the format, not the checksum digit
     * which would require additional validation logic.</p>
     * 
     * @param nric the NRIC string to validate
     * @return {@code true} if the NRIC format is valid, {@code false} otherwise
     */
    public static boolean isValidNRIC(String nric) {
        if (nric == null || nric.length() != ApplicationConstants.NRIC_LENGTH) {
            return false;
        }
        
        // Convert to uppercase for consistent validation
        String upperNric = nric.toUpperCase();
        
        return NRIC_PATTERN.matcher(upperNric).matches();
    }
    
    /**
     * Validates NRIC format and returns detailed feedback.
     * 
     * @param nric the NRIC string to validate
     * @return a {@link ValidationResult} with validation status and message
     */
    public static ValidationResult validateNRIC(String nric) {
        if (nric == null || nric.isEmpty()) {
            return new ValidationResult(false, "NRIC cannot be null or empty");
        }
        
        if (nric.length() != ApplicationConstants.NRIC_LENGTH) {
            return new ValidationResult(false, 
                "NRIC must be exactly " + ApplicationConstants.NRIC_LENGTH + " characters long");
        }
        
        char firstChar = Character.toUpperCase(nric.charAt(0));
        if (ApplicationConstants.VALID_NRIC_PREFIXES.indexOf(firstChar) == -1) {
            return new ValidationResult(false, 
                "NRIC must start with S, T, F, or G");
        }
        
        // Check middle 7 characters are digits
        for (int i = 1; i < 8; i++) {
            if (!Character.isDigit(nric.charAt(i))) {
                return new ValidationResult(false, 
                    "NRIC characters 2-8 must be digits");
            }
        }
        
        // Check last character is a letter
        if (!Character.isLetter(nric.charAt(8))) {
            return new ValidationResult(false, 
                "NRIC must end with a letter");
        }
        
        return new ValidationResult(true, "NRIC format is valid");
    }
    
    // ============================================================================
    // AGE VALIDATION
    // ============================================================================
    
    /**
     * Validates if an age meets the minimum requirement for married applicants.
     * 
     * @param age the age to validate
     * @return {@code true} if age is valid for married applicants, {@code false} otherwise
     */
    public static boolean isValidAgeForMarriedApplicant(int age) {
        return age >= ApplicationConstants.MIN_AGE_MARRIED_APPLICANT;
    }
    
    /**
     * Validates if an age meets the minimum requirement for single applicants.
     * 
     * @param age the age to validate
     * @return {@code true} if age is valid for single applicants, {@code false} otherwise
     */
    public static boolean isValidAgeForSingleApplicant(int age) {
        return age >= ApplicationConstants.MIN_AGE_SINGLE_APPLICANT;
    }
    
    /**
     * Validates if an age is positive and reasonable (0-150).
     * 
     * @param age the age to validate
     * @return {@code true} if age is within valid range, {@code false} otherwise
     */
    public static boolean isValidAge(int age) {
        return age > 0 && age <= 150;
    }
    
    // ============================================================================
    // STRING VALIDATION
    // ============================================================================
    
    /**
     * Checks if a string is null or empty (after trimming whitespace).
     * 
     * @param str the string to check
     * @return {@code true} if the string is null or empty, {@code false} otherwise
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Checks if a string is not null and not empty (after trimming whitespace).
     * 
     * @param str the string to check
     * @return {@code true} if the string has content, {@code false} otherwise
     */
    public static boolean hasContent(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Validates an email address format.
     * 
     * @param email the email string to validate
     * @return {@code true} if the email format is valid, {@code false} otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    // ============================================================================
    // VALIDATION RESULT INNER CLASS
    // ============================================================================
    
    /**
     * Represents the result of a validation operation.
     * 
     * <p>This class encapsulates both the validation status (pass/fail) and
     * a descriptive message explaining the result. Use this class when
     * detailed validation feedback is needed.</p>
     * 
     * <h3>Usage Example:</h3>
     * <pre>{@code
     * ValidationResult result = ValidationUtils.validatePassword("short");
     * if (!result.isValid()) {
     *     displayError(result.getMessage());
     * }
     * }</pre>
     */
    public static class ValidationResult {
        
        private final boolean valid;
        private final String message;
        
        /**
         * Creates a new validation result.
         * 
         * @param valid whether the validation passed
         * @param message descriptive message about the validation result
         */
        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        /**
         * Returns whether the validation passed.
         * 
         * @return {@code true} if validation passed, {@code false} otherwise
         */
        public boolean isValid() {
            return valid;
        }
        
        /**
         * Returns the validation message.
         * 
         * @return descriptive message about the validation result
         */
        public String getMessage() {
            return message;
        }
        
        /**
         * Returns a string representation of this validation result.
         * 
         * @return formatted string showing validity and message
         */
        @Override
        public String toString() {
            return "ValidationResult{valid=" + valid + ", message='" + message + "'}";
        }
    }
}
