package interfaces;

/**
 * Interface for input validation components in the BTO Management System.
 * 
 * <p>This interface defines the contract for classes that validate user input.
 * Implementations provide specific validation logic for different types of
 * input data such as NRIC, passwords, dates, etc.</p>
 * 
 * <h2>Implementation Classes:</h2>
 * <ul>
 *   <li>{@link utils.NricInputValidation} - Validates Singapore NRIC format</li>
 * </ul>
 * 
 * <h2>Design Pattern:</h2>
 * <p>This interface enables the Strategy pattern for input validation,
 * allowing different validation strategies to be applied based on the
 * type of input being validated.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * IInputValidation validator = new NricInputValidation();
 * boolean isValid = validator.validateInput("S1234567A");
 * if (!isValid) {
 *     System.out.println("Invalid NRIC format");
 * }
 * }</pre>
 * 
 * <h2>Relationship to ValidationUtils:</h2>
 * <p>For new validation requirements, consider using the centralized
 * {@link utils.ValidationUtils} class which provides comprehensive
 * validation methods with detailed result feedback.</p>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see utils.NricInputValidation
 * @see utils.ValidationUtils
 */
public interface IInputValidation {

    /**
     * Validates the provided input string according to implementation-specific rules.
     * 
     * @param input the input string to validate
     * @return {@code true} if the input is valid according to the validation rules,
     *         {@code false} otherwise
     */
    boolean validateInput(String input);
}
