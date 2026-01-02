package boundary.filehandlerview;

import interfaces.IDisplayable;
import java.util.Scanner;
import utils.FileHandler;
import utils.ApplicationConstants;

public class FileHandlerReadView implements IDisplayable {

	private String filepath;
	private Scanner scanner;
	private FileHandler fileHandler;

	/**
	 * Constructors
	 */
	public FileHandlerReadView() {
		this.scanner = new Scanner(System.in);
		this.fileHandler = new FileHandler();
		this.filepath = ApplicationConstants.DEFAULT_DATA_PATH;
	}

	/**
	 * Methods
	 */
	@Override
	public void display() {
		boolean exit = false;
		
		while (!exit) {
			System.out.println("===== READ DATA FILES =====");
			System.out.println("1. Read Applicant Data");
			System.out.println("2. Read Officer Data");
			System.out.println("3. Read Manager Data");
			System.out.println("4. Read Project Data");
			System.out.println("5. Read BTO Application Data");
			System.out.println("6. Read Officer Registration Data");
			System.out.println("7. Read Booking Data");
			System.out.println("8. Read Receipt Data");
			System.out.println("9. Read Enquiry Data");
			System.out.println("10. Read All Files");
			System.out.println("11. Set Import Folder");
			System.out.println("0. Return to Data Files Menu");
			System.out.print("Enter your choice: ");
			
			String input = scanner.nextLine();
			exit = getUserChoice(input);
		}
	}

	private boolean getUserChoice(String input) {
		switch (input) {
			case "1":
				readApplicantData();
				return false;
			case "2":
				readOfficerData();
				return false;
			case "3":
				readManagerData();
				return false;
			case "4":
				readProjectData();
				return false;
			case "5":
				readBTOApplicationData();
				return false;
			case "6":
				readOfficerApplicationData();
				return false;
			case "7":
				readBookingData();
				return false;
			case "8":
				readReceiptData();
				return false;
			case "9":
				readEnquiryData();
				return false;
			case "10":
				readAllData();
				return false;
			case "11":
				this.filepath = selectImportFolder();
				System.out.println("Import folder set to: " + this.filepath);
				return false;
			case "0":
				return true;
			default:
				System.out.println("Invalid choice. Please try again.");
				return false;
		}
	}

	private void readApplicantData() {
		System.out.println("Reading Applicant Data...");
		String folderPath = (filepath != null && !filepath.isEmpty()) ? filepath : ApplicationConstants.DEFAULT_DATA_PATH;
		String filePath = folderPath + "ApplicantList.csv";
		try {
			if (FileHandler.readApplicantData(filePath)) {
				System.out.println("Applicant data read successfully.");
			} else {
				System.out.println("Failed to read applicant data.");
			}
		} catch (Exception e) {
			System.out.println("Error reading applicant data: " + e.getMessage());
		}
	}

	private void readOfficerData() {
		System.out.println("Reading Officer Data...");
		String folderPath = (filepath != null && !filepath.isEmpty()) ? filepath : ApplicationConstants.DEFAULT_DATA_PATH;
		String filePath = folderPath + "OfficerList.csv";
		try {
			if (FileHandler.readOfficerData(filePath)) {
				System.out.println("Officer data read successfully.");
			} else {
				System.out.println("Failed to read officer data.");
			}
		} catch (Exception e) {
			System.out.println("Error reading officer data: " + e.getMessage());
		}
	}

	private void readManagerData() {
		System.out.println("Reading Manager Data...");
		String folderPath = (filepath != null && !filepath.isEmpty()) ? filepath : ApplicationConstants.DEFAULT_DATA_PATH;
		String filePath = folderPath + "ManagerList.csv";
		try {
			if (FileHandler.readManagerData(filePath)) {
				System.out.println("Manager data read successfully.");
			} else {
				System.out.println("Failed to read manager data.");
			}
		} catch (Exception e) {
			System.out.println("Error reading manager data: " + e.getMessage());
		}
	}

	private void readProjectData() {
		System.out.println("Reading Project Data...");
		String folderPath = (filepath != null && !filepath.isEmpty()) ? filepath : ApplicationConstants.DEFAULT_DATA_PATH;
		String filePath = folderPath + "ProjectList.csv";
		try {
			if (FileHandler.readProjectData(filePath)) {
				System.out.println("Project data read successfully.");
			} else {
				System.out.println("Failed to read project data.");
			}
		} catch (Exception e) {
			System.out.println("Error reading project data: " + e.getMessage());
		}
	}

	/**
	 * Read BTO application data files
	 */
	private void readBTOApplicationData() {
		System.out.println("Reading BTO Application Data...");
		String folderPath = (filepath != null && !filepath.isEmpty()) ? filepath : ApplicationConstants.DEFAULT_DATA_PATH;
		String filePath = folderPath + "ApplicationList.csv";

		boolean success = FileHandler.readBTOApplicationData(filePath);

		if (success) {
			System.out.println("BTO application data loaded successfully.");
		} else {
			System.out.println("Failed to load BTO application data. Please check file path and try again.");
		}
	}

	/**
	 * Read officer application data files
	 */
	private void readOfficerApplicationData() {
		System.out.println("Reading Officer Application Data...");
		String folderPath = (filepath != null && !filepath.isEmpty()) ? filepath : ApplicationConstants.DEFAULT_DATA_PATH;
		String filePath = folderPath + "OfficerApplicationList.csv";

		boolean success = FileHandler.readOfficerApplicationData(filePath);

		if (success) {
			System.out.println("Officer application data loaded successfully.");
		} else {
			System.out.println("Failed to load officer application data. Please check file path and try again.");
		}
	}

	/**
	 * Read booking data files
	 */
	private void readBookingData() {
		System.out.println("Reading Project Data...");
		String folderPath = (filepath != null && !filepath.isEmpty()) ? filepath : ApplicationConstants.DEFAULT_DATA_PATH;
		String filePath = folderPath + "BookingList.csv";

		boolean success = FileHandler.readBookingData(filePath);

		if (success) {
			System.out.println("Booking data loaded successfully.");
		} else {
			System.out.println("Failed to load booking data. Please check file path and try again.");
		}
	}

	/**
	 * Read receipt data files
	 */
	private void readReceiptData() {
		System.out.println("Reading Receipt Data...");
		String folderPath = (filepath != null && !filepath.isEmpty()) ? filepath : ApplicationConstants.DEFAULT_DATA_PATH;
		String filePath = folderPath + "ReceiptList.csv";

		boolean success = FileHandler.readReceiptData(filePath);

		if (success) {
			System.out.println("Receipt data loaded successfully.");
		} else {
			System.out.println("Failed to load receipt data. Please check file path and try again.");
		}
	}

	/**
	 * Read enquiry data files
	 */
	private void readEnquiryData() {
		System.out.println("Reading Enquiry Data...");
		String folderPath = (filepath != null && !filepath.isEmpty()) ? filepath : ApplicationConstants.DEFAULT_DATA_PATH;
		String filePath = folderPath + "EnquiryList.csv";

		boolean success = FileHandler.readEnquiryData(filePath);

		if (success) {
			System.out.println("Enquiry data loaded successfully.");
		} else {
			System.out.println("Failed to load enquiry data. Please check file path and try again.");
		}
	}

	private void readAllData() {
		System.out.println("Reading All Data...");
		String folderPath = (filepath != null && !filepath.isEmpty()) ? filepath : ApplicationConstants.DEFAULT_DATA_PATH;
		try {
			// Call all individual read methods since there's no readAllData method
			boolean applicantSuccess = FileHandler.readApplicantData(folderPath + "ApplicantList.csv");
			boolean officerSuccess = FileHandler.readOfficerData(folderPath + "OfficerList.csv");
			boolean managerSuccess = FileHandler.readManagerData(folderPath + "ManagerList.csv");
			boolean projectSuccess = FileHandler.readProjectData(folderPath + "ProjectList.csv");
			boolean btoSuccess = FileHandler.readBTOApplicationData(folderPath + "ApplicationList.csv");
			boolean officerAppSuccess = FileHandler.readOfficerApplicationData(folderPath + "OfficerApplicationList.csv");
			boolean bookingSuccess = FileHandler.readBookingData(folderPath + "BookingList.csv");
			boolean receiptSuccess = FileHandler.readReceiptData(folderPath + "ReceiptList.csv");
			boolean enquirySuccess = FileHandler.readEnquiryData(folderPath + "EnquiryList.csv");
			
			if (applicantSuccess && officerSuccess && managerSuccess && projectSuccess &&
					btoSuccess && officerAppSuccess && bookingSuccess && receiptSuccess && enquirySuccess) {
				System.out.println("All data read successfully.");
			} else {
				System.out.println("Failed to read all data.");
			}
		} catch (Exception e) {
			System.out.println("Error reading all data: " + e.getMessage());
		}
	}

	private String selectImportFolder() {
		System.out.println("Enter path to import folder (leave blank for default): ");
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

	public void setFilePath(String filePath) {
		this.filepath = filePath;
	}

	public String getFilePath() {
		return this.filepath;
	}
}
