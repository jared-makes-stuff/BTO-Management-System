package utils;

import interfaces.IDisplayable;

/**
 * Utility class for displaying menus in the BTO Management System.
 * 
 * <p>This class provides a centralized method for invoking the display
 * functionality of any IDisplayable component. It acts as a facade that
 * simplifies menu navigation throughout the application.</p>
 * 
 * <h2>Purpose:</h2>
 * <p>Provides a single point of entry for displaying any view in the system,
 * enabling consistent menu handling and potential future enhancements like
 * logging, error handling, or menu transition animations.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Display the main menu
 * DisplayMenu.displayMenu(new MainMenuView());
 * 
 * // Display applicant menu after login
 * DisplayMenu.displayMenu(new ApplicantView(authenticatedApplicant));
 * }</pre>
 * 
 * <h2>Design Pattern:</h2>
 * <p>This class implements the Facade pattern, providing a simple interface
 * for the complex menu navigation system. It also supports the Strategy
 * pattern by accepting any IDisplayable implementation.</p>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see interfaces.IDisplayable
 */
public class DisplayMenu {
    
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DisplayMenu() {
        throw new UnsupportedOperationException("DisplayMenu is a utility class and cannot be instantiated");
    }
    
    /**
     * Displays the menu for the given IDisplayable component.
     * 
     * <p>This method invokes the {@link IDisplayable#display()} method on the
     * provided menu object. The menu will handle its own input loop and return
     * when the user chooses to exit or navigate away.</p>
     * 
     * @param menu the IDisplayable component to display, must not be null
     * @throws NullPointerException if menu is null
     * 
     * @see IDisplayable#display()
     */
    public static void displayMenu(IDisplayable menu) {
        if (menu == null) {
            throw new NullPointerException("Menu cannot be null");
        }
        menu.display();
    }
}
