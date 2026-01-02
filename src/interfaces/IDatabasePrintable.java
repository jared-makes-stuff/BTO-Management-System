package interfaces;

/**
 * Interface for database classes that support formatted console output.
 * 
 * <p>This interface provides a standardized way for database classes to
 * display their contents in a human-readable format. It is primarily used
 * for debugging, administration, and data verification purposes.</p>
 * 
 * <h2>Implementation Classes:</h2>
 * <ul>
 *   <li>{@link database.ApplicantDatabase}</li>
 *   <li>{@link database.OfficerDatabase}</li>
 *   <li>{@link database.ManagerDatabase}</li>
 *   <li>{@link database.ProjectDatabase}</li>
 *   <li>{@link database.BTOApplicationDatabase}</li>
 *   <li>{@link database.BookingDatabase}</li>
 *   <li>{@link database.EnquiryDatabase}</li>
 *   <li>{@link database.ReceiptDatabase}</li>
 * </ul>
 * 
 * <h2>Output Format:</h2>
 * <p>Implementations should produce formatted table output with:</p>
 * <ul>
 *   <li>Header row with column names</li>
 *   <li>Separator lines for visual clarity</li>
 *   <li>Properly aligned data columns</li>
 *   <li>Message when database is empty</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * IDatabasePrintable database = new ApplicantDatabase();
 * // ... populate database ...
 * database.printData(); // Outputs formatted table to console
 * }</pre>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 */
public interface IDatabasePrintable {
    
    /**
     * Prints the contents of the database to the console in a formatted table.
     * 
     * <p>Implementing classes should format their data appropriately for
     * clear and readable output, including headers, separators, and
     * properly aligned columns.</p>
     * 
     * <p>If the database is empty, a message indicating this should be
     * displayed instead of an empty table.</p>
     */
    void printData();
}