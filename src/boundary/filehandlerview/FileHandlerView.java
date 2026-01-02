package boundary.filehandlerview;

import interfaces.*;
import utils.DisplayMenu;
import java.util.Scanner;

/**
 * Main view for data file handling operations in the BTO Management System.
 * Serves as a container for the read and write file handler views, allowing
 * users to choose between importing and exporting system data.
 */
public class FileHandlerView implements IDisplayable {

	/**
	 * Displays the file handler menu and processes user selections.
	 * Presents options for reading data files, writing data files,
	 * or returning to the main menu.
	 */
	@Override
	public void display() {
		Scanner scanner = new Scanner(System.in);
		boolean exit = false;
		
		while (!exit) {
			System.out.println("===== DATA FILES MENU =====");
			System.out.println("1. Read Data Files");
			System.out.println("2. Write Data Files");
			System.out.println("0. Return to Main Menu");
			System.out.print("Enter your choice: ");
			
			String input = scanner.nextLine();
			exit = getMainMenuChoice(input);
		}
	}

	/**
	 * Processes the user's menu selection.
	 * Routes to the appropriate sub-view based on user choice.
	 * 
	 * @param input The menu option selected by the user
	 * @return true if the user chooses to exit, false otherwise
	 */
	private boolean getMainMenuChoice(String input) {
		switch (input) {
			case "1":
				DisplayMenu.displayMenu(new FileHandlerReadView());
				return false;
			case "2":
				DisplayMenu.displayMenu(new FileHandlerWriteView());
				return false;
			case "0":
				return true;
			default:
				System.out.println("Invalid choice. Please try again.");
				return false;
		}
	}
}