package interfaces;

/**
 * Interface for UI components that can display interactive menus to users.
 * 
 * <p>This interface establishes the contract for all boundary/view classes
 * in the BTO Management System. Classes implementing this interface provide
 * user interaction through console-based menus and handle user input.</p>
 * 
 * <h2>Implementation Classes:</h2>
 * <ul>
 *   <li>{@link boundary.LoginView} - User authentication interface</li>
 *   <li>{@link boundary.MainMenuView} - Main navigation menu</li>
 *   <li>{@link boundary.applicantview.ApplicantView} - Applicant menu system</li>
 *   <li>{@link boundary.officerview.OfficerView} - Officer menu system</li>
 *   <li>{@link boundary.managerview.ManagerView} - Manager menu system</li>
 *   <li>And various sub-views for specific functionality</li>
 * </ul>
 * 
 * <h2>Design Pattern:</h2>
 * <p>This interface supports the Strategy pattern, allowing different
 * view implementations to be used interchangeably through the
 * {@link utils.DisplayMenu#displayMenu(IDisplayable)} method.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * IDisplayable view = new ApplicantView(currentApplicant);
 * DisplayMenu.displayMenu(view); // Calls view.display()
 * }</pre>
 * 
 * <h2>Implementation Guidelines:</h2>
 * <ul>
 *   <li>Display method should contain the main menu loop</li>
 *   <li>Handle user input validation within the display method</li>
 *   <li>Exit cleanly when user chooses to logout or go back</li>
 *   <li>Catch and handle input exceptions gracefully</li>
 * </ul>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see utils.DisplayMenu
 */
public interface IDisplayable {
    
    /**
     * Displays the menu or user interface for this component.
     * 
     * <p>This method should contain the main interaction loop for the view,
     * including menu display, user input handling, and navigation logic.
     * The method returns when the user exits or logs out.</p>
     * 
     * <p>Implementations should:</p>
     * <ul>
     *   <li>Display menu options clearly</li>
     *   <li>Validate user input</li>
     *   <li>Provide feedback for invalid choices</li>
     *   <li>Handle exceptions gracefully</li>
     * </ul>
     */
    void display();
} 