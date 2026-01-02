package boundary.filehandlerview;

import interfaces.IDisplayable;
import java.util.Scanner;
import utils.FileHandler;
import utils.ApplicationConstants;

public class FileHandlerWriteView implements IDisplayable {

	private Scanner scanner;
	private FileHandler fileHandler;
	private String filePath;

	/**
	 * Constructors
	 */
	public FileHandlerWriteView() {
		this.scanner = new Scanner(System.in);
		this.fileHandler = new FileHandler();
		this.filePath = ApplicationConstants.DEFAULT_DATA_PATH;
	}

	/**
	 * 
	 * @param filePath
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return this.filePath;
	}

	/**
	 * Methods
	 */
	@Override
	public void display() {
		boolean exit = false;
		
		while (!exit) {
			System.out.println("===== WRITE DATA FILES =====");
			System.out.println("1. Write Applicant Data");
			System.out.println("2. Write Officer Data");
			System.out.println("3. Write Manager Data");
			System.out.println("4. Write Project Data");
			System.out.println("5. Write BTO Application Data");
			System.out.println("6. Write Officer Registration Data");
			System.out.println("7. Write Booking Data");
			System.out.println("8. Write Receipt Data");
			System.out.println("9. Write Enquiry Data");
			System.out.println("10. Write All Files");
			System.out.println("0. Return to Data Files Menu");
			System.out.print("Enter your choice: ");
			
			String input = scanner.nextLine();
			exit = getUserChoice(input);
		}
	}

	private boolean getUserChoice(String input) {
		switch (input) {
			case "1":
				writeAllData();
				return false;
			case "2":
				writeApplicantData();
				return false;
			case "3":
				writeOfficerData();
				return false;
			case "4":
				writeManagerData();
				return false;
			case "5":
				writeBTOApplicationData();
				return false;
			case "6":
				writeOfficerApplicationData();
				return false;
			case "7":
				writeBookingData();
				return false;
			case "8":
				writeReceiptData();
				return false;
			case "9":
				writeEnquiryData();
				return false;
			case "10":
				writeAllData();
				return false;
			case "0":
				return true;
			default:
				System.out.println("Invalid choice. Please try again.");
				return false;
		}
	}

	private void writeApplicantData() {
		System.out.println("Writing Applicant Data...");
		String exportPath = selectExportFolder();
		String filePath = exportPath + "ApplicantList.csv";
		
		try {
			if (FileHandler.writeApplicantData(filePath)) {
				System.out.println("Applicant data written successfully to " + filePath);
			} else {
				System.out.println("Failed to write applicant data.");
			}
		} catch (Exception e) {
			System.out.println("Error writing applicant data: " + e.getMessage());
		}
	}

	private void writeOfficerData() {
		System.out.println("Writing Officer Data...");
		String exportPath = selectExportFolder();
		String filePath = exportPath + "OfficerList.csv";
		
		try {
			if (FileHandler.writeOfficerData(filePath)) {
				System.out.println("Officer data written successfully to " + filePath);
			} else {
				System.out.println("Failed to write officer data.");
			}
		} catch (Exception e) {
			System.out.println("Error writing officer data: " + e.getMessage());
		}
	}

	private void writeManagerData() {
		System.out.println("Writing Manager Data...");
		String exportPath = selectExportFolder();
		String filePath = exportPath + "ManagerList.csv";
		
		try {
			if (FileHandler.writeManagerData(filePath)) {
				System.out.println("Manager data written successfully to " + filePath);
			} else {
				System.out.println("Failed to write manager data.");
			}
		} catch (Exception e) {
			System.out.println("Error writing manager data: " + e.getMessage());
		}
	}

	private void writeProjectData() {
		System.out.println("Writing Project Data...");
		String exportPath = selectExportFolder();
		String filePath = exportPath + "ProjectList.csv";
		
		try {
			if (FileHandler.writeProjectData(filePath)) {
				System.out.println("Project data written successfully to " + filePath);
			} else {
				System.out.println("Failed to write project data.");
			}
		} catch (Exception e) {
			System.out.println("Error writing project data: " + e.getMessage());
		}
	}

	/**
	 * Write BTO application data files
	 */
	private void writeBTOApplicationData() {
		System.out.println("Writing BTO Application Data...");
		String exportPath = selectExportFolder();
		String filePath = exportPath + "ApplicationList.csv";

		boolean success = FileHandler.writeBTOApplicationData(filePath);

		if (success) {
			System.out.println("BTO application data written successfully.");
		} else {
			System.out.println("Failed to write BTO application data. Please check file path and try again.");
		}
	}

	/**
	 * Write officer application data files
	 */
	private void writeOfficerApplicationData() {
		System.out.println("Writing Officer Application Data...");
		String exportPath = selectExportFolder();
		String filePath = exportPath + "OfficerApplicationList.csv";

		boolean success = FileHandler.writeOfficerApplicationData(filePath);

		if (success) {
			System.out.println("Officer application data written successfully.");
		} else {
			System.out.println("Failed to write officer application data. Please check file path and try again.");
		}
	}

	/**
	 * Write booking data files
	 */
	private void writeBookingData() {
		System.out.println("Writing Booking Data...");
		String exportPath = selectExportFolder();
		String filePath = exportPath + "BookingList.csv";

		boolean success = FileHandler.writeBookingData(filePath);

		if (success) {
			System.out.println("Booking data written successfully.");
		} else {
			System.out.println("Failed to write booking data. Please check file path and try again.");
		}
	}

	/**
	 * Write receipt data files
	 */
	private void writeReceiptData() {
		System.out.println("Writing Receipt Data...");
		String exportPath = selectExportFolder();
		String filePath = exportPath + "ReceiptList.csv";

		boolean success = FileHandler.writeReceiptData(filePath);

		if (success) {
			System.out.println("Receipt data written successfully.");
		} else {
			System.out.println("Failed to write receipt data. Please check file path and try again.");
		}
	}

	/**
	 * Write enquiry data files
	 */
	private void writeEnquiryData() {
		System.out.println("Writing Enquiry Data...");
		String exportPath = selectExportFolder();
		String filePath = exportPath + "EnquiryList.csv";

		boolean success = FileHandler.writeEnquiryData(filePath);

		if (success) {
			System.out.println("Enquiry data written successfully.");
		} else {
			System.out.println("Failed to write enquiry data. Please check file path and try again.");
		}
	}

	private void writeAllData() {
		System.out.println("Writing All Data...");
		String exportPath = selectExportFolder();
		
		try {
			// Call all individual write methods since there's no writeAllData method
			boolean applicantSuccess = FileHandler.writeApplicantData(exportPath + "ApplicantList.csv");
			boolean officerSuccess = FileHandler.writeOfficerData(exportPath + "OfficerList.csv");
			boolean managerSuccess = FileHandler.writeManagerData(exportPath + "ManagerList.csv");
			boolean projectSuccess = FileHandler.writeProjectData(exportPath + "ProjectList.csv");
			boolean btoSuccess = FileHandler.writeBTOApplicationData(exportPath + "ApplicationList.csv");
			boolean officerAppSuccess = FileHandler.writeOfficerApplicationData(exportPath + "OfficerApplicationList.csv");
			boolean bookingSuccess = FileHandler.writeBookingData(exportPath + "BookingList.csv");
			boolean receiptSuccess = FileHandler.writeReceiptData(exportPath + "ReceiptList.csv");
			boolean enquirySuccess = FileHandler.writeEnquiryData(exportPath + "EnquiryList.csv");

			if (applicantSuccess && officerSuccess && managerSuccess && projectSuccess &&
					btoSuccess && officerAppSuccess && bookingSuccess && receiptSuccess && enquirySuccess) {
				System.out.println("All data written successfully to " + exportPath);
			} else {
				System.out.println("Failed to write all data.");
			}
		} catch (Exception e) {
			System.out.println("Error writing all data: " + e.getMessage());
		}
	}

	private String selectExportFolder() {
		System.out.println("Enter path to export folder (leave blank for default): ");
		String path = scanner.nextLine();
		
		if (path == null || path.isEmpty()) {
			return ApplicationConstants.DEFAULT_DATA_PATH;
		}
		
		// Ensure path ends with a separator
		if (!path.endsWith("/") && !path.endsWith("\\")) {
			path += "/";
		}
		
		return path;
	}

	/**
	 * 
	 * @param fileHandler
	 */
	public void setFileHandler(FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}

	public FileHandler getFileHandler() {
		return this.fileHandler;
	}
}
