package utils;

import interfaces.IInputValidation;

/**
 * Validation class for Singapore NRIC (National Registration Identity Card) numbers.
 * 
 * <p>This class implements the {@link IInputValidation} interface to provide
 * specific validation logic for Singapore NRIC format. It verifies that the
 * input string conforms to the standard NRIC structure.</p>
 * 
 * <h2>NRIC Format:</h2>
 * <ul>
 *   <li>Total length: 9 characters</li>
 *   <li>First character: S, T, F, or G (identifies citizen type and birth century)</li>
 *   <li>Characters 2-8: 7 numeric digits</li>
 *   <li>Last character: Alphabetic checksum letter</li>
 * </ul>
 * 
 * <h2>Prefix Meanings:</h2>
 * <ul>
 *   <li><strong>S</strong> - Singapore citizens and permanent residents born before 2000</li>
 *   <li><strong>T</strong> - Singapore citizens and permanent residents born from 2000 onwards</li>
 *   <li><strong>F</strong> - Foreigners issued with an NRIC before 2000</li>
 *   <li><strong>G</strong> - Foreigners issued with an NRIC from 2000 onwards</li>
 * </ul>
 * 
 * <h2>Validation Limitations:</h2>
 * <p>This implementation validates the format only. It does NOT validate:</p>
 * <ul>
 *   <li>The checksum digit (last character)</li>
 *   <li>Whether the NRIC is actually issued</li>
 *   <li>Whether the NRIC belongs to a real person</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * IInputValidation validator = new NricInputValidation();
 * 
 * // Valid NRIC format
 * validator.validateInput("S1234567A");  // returns true
 * validator.validateInput("T0012345B");  // returns true
 * 
 * // Invalid NRIC formats
 * validator.validateInput("12345678A");  // returns false (no prefix)
 * validator.validateInput("S123456A");   // returns false (too short)
 * validator.validateInput("S12345678");  // returns false (no suffix letter)
 * }</pre>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see interfaces.IInputValidation
 * @see ValidationUtils#isValidNRIC(String)
 */
public class NricInputValidation implements IInputValidation {

    /**
     * Validates the format of a Singapore NRIC number.
     * 
     * <p>This method checks the following criteria:</p>
     * <ol>
     *   <li>Input is not null and exactly 9 characters long</li>
     *   <li>First character is S, T, F, or G</li>
     *   <li>Characters 2-8 are all digits</li>
     *   <li>Last character is an alphabetic letter</li>
     * </ol>
     * 
     * @param input the NRIC string to validate
     * @return {@code true} if the NRIC has a valid format, {@code false} otherwise
     */
    @Override
    public boolean validateInput(String input) {
        // Check for null or incorrect length
        if (input == null || input.length() != ApplicationConstants.NRIC_LENGTH) {
            return false;
        }
        
        // First character should be S, T, F or G
        char firstChar = Character.toUpperCase(input.charAt(0));
        if (ApplicationConstants.VALID_NRIC_PREFIXES.indexOf(firstChar) == -1) {
            return false;
        }
        
        // Last character should be a letter
        char lastChar = input.charAt(8);
        if (!Character.isLetter(lastChar)) {
            return false;
        }
        
        // Middle 7 characters should be digits
        for (int i = 1; i < 8; i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return false;
            }
        }
        
        return true;
    }
}
