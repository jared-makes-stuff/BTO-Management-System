package utils;

import database.*;
import entity.*;
import enums.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileHandler {

	private static final String APPLICANT_FILE = "ApplicantList.csv";
	private static final String OFFICER_FILE = "OfficerList.csv";
	private static final String MANAGER_FILE = "ManagerList.csv";
	private static final String PROJECT_FILE = "ProjectList.csv";
	private static final String BTO_APPLICATION_FILE = "ApplicationList.csv";
	private static final String OFFICER_APPLICATION_FILE = "OfficerApplicationList.csv";
	private static final String BOOKING_FILE = "BookingList.csv";
	private static final String RECEIPT_FILE = "ReceiptList.csv";
	private static final String ENQUIRY_FILE = "EnquiryList.csv";

	private static ApplicantDatabase applicantDatabase;
	private static OfficerDatabase officerDatabase;
	private static ManagerDatabase managerDatabase;
	private static ProjectDatabase projectDatabase;
	private static BTOApplicationDatabase btoApplicationDatabase;
	private static OfficerApplicationDatabase officerApplicationDatabase;
	private static BookingDatabase bookingDatabase;
	private static ReceiptDatabase receiptDatabase;
	private static EnquiryDatabase enquiryDatabase;

	/**
	 *
	 * @param applicantDatabase
	 */
	public static void setApplicantDatabase(ApplicantDatabase applicantDatabase) {
		FileHandler.applicantDatabase = applicantDatabase;
	}

	/**
	 *
	 * @param officerDatabase
	 */
	public static void setOfficerDatabase(OfficerDatabase officerDatabase) {
		FileHandler.officerDatabase = officerDatabase;
	}

	/**
	 *
	 * @param managerDatabase
	 */
	public static void setManagerDatabase(ManagerDatabase managerDatabase) {
		FileHandler.managerDatabase = managerDatabase;
	}

	/**
	 *
	 * @param projectDatabase
	 */
	public static void setProjectDatabase(ProjectDatabase projectDatabase) {
		FileHandler.projectDatabase = projectDatabase;
	}

	/**
	 *
	 * @param btoApplicationDatabase
	 */
	public static void setBtoApplicationDatabase(BTOApplicationDatabase btoApplicationDatabase) {
		FileHandler.btoApplicationDatabase = btoApplicationDatabase;
	}

	/**
	 *
	 * @param officerApplicationDatabase
	 */
	public static void setOfficerApplicationDatabase(OfficerApplicationDatabase officerApplicationDatabase) {
		FileHandler.officerApplicationDatabase = officerApplicationDatabase;
	}

	/**
	 *
	 * @param bookingDatabase
	 */
	public static void setBookingDatabase(BookingDatabase bookingDatabase) {
		FileHandler.bookingDatabase = bookingDatabase;
	}

	/**
	 *
	 * @param receiptDatabase
	 */
	public static void setReceiptDatabase(ReceiptDatabase receiptDatabase) {
		FileHandler.receiptDatabase = receiptDatabase;
	}

	/**
	 *
	 * @param enquiryDatabase
	 */
	public static void setEnquiryDatabase(EnquiryDatabase enquiryDatabase) {
		FileHandler.enquiryDatabase = enquiryDatabase;
	}

	public static ApplicantDatabase getApplicantDatabase() {
		return FileHandler.applicantDatabase;
	}

	public static OfficerDatabase getOfficerDatabase() {
		return FileHandler.officerDatabase;
	}

	public static ManagerDatabase getManagerDatabase() {
		return FileHandler.managerDatabase;
	}

	public static ProjectDatabase getProjectDatabase() {
		return FileHandler.projectDatabase;
	}

	public static BTOApplicationDatabase getBtoApplicationDatabase() {
		return FileHandler.btoApplicationDatabase;
	}

	public static OfficerApplicationDatabase getOfficerApplicationDatabase() {
		return FileHandler.officerApplicationDatabase;
	}

	public static BookingDatabase getBookingDatabase() {
		return FileHandler.bookingDatabase;
	}

	public static ReceiptDatabase getReceiptDatabase() {
		return FileHandler.receiptDatabase;
	}

	public static EnquiryDatabase getEnquiryDatabase() {
		return FileHandler.enquiryDatabase;
	}

	/**
	 * Individual read methods for each database type
	 * @param filePath
	 */
	public static boolean readApplicantData(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				System.out.println("Applicant data file not found: " + filePath);
				return false;
			}

			// Create database if not exists
			if (applicantDatabase == null) {
				applicantDatabase = new ApplicantDatabase();
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			// Skip header line
			reader.readLine();

			// Read each applicant record
			ArrayList<Applicant> applicants = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				if (data.length >= 5) {
					String name = data[0].trim();
					String nric = data[1].trim();
					int age = Integer.parseInt(data[2].trim());
					MarriageStatusEnum maritalStatus = parseMaritalStatus(data[3].trim());
					String password = data[4].trim();

					Applicant applicant = new Applicant();
					applicant.setName(name);
					applicant.setNric(nric);
					applicant.setAge(age);
					applicant.setMaritalStatus(maritalStatus);
					applicant.setPassword(password);

					applicants.add(applicant);
				}
			}

			applicantDatabase.setApplicants(applicants);
			reader.close();

			System.out.println("Read " + applicants.size() + " applicants from " + filePath);
			return true;
		} catch (Exception e) {
			System.out.println("Error reading applicant data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 * @param filePath
	 */
	public static boolean readOfficerData(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				System.out.println("Officer data file not found: " + filePath);
				return false;
			}

			// Create officer database if not exists
			if (officerDatabase == null) {
				officerDatabase = new OfficerDatabase();
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			// Skip header line
			reader.readLine();

			// Read each officer record
			ArrayList<Officer> officers = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				if (data.length >= 5) {
					String name = data[0].trim();
					String nric = data[1].trim();
					int age = Integer.parseInt(data[2].trim());
					MarriageStatusEnum maritalStatus = parseMaritalStatus(data[3].trim());
					String password = data[4].trim();

					Officer officer = new Officer();
					officer.setName(name);
					officer.setNric(nric);
					officer.setAge(age);
					officer.setMaritalStatus(maritalStatus);
					officer.setPassword(password);

					officers.add(officer);
				}
			}

			officerDatabase.setOfficers(officers);
			reader.close();

			System.out.println("Read " + officers.size() + " officers from " + filePath);
			return true;
		} catch (Exception e) {
			System.out.println("Error reading officer data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 * @param filePath
	 */
	public static boolean readManagerData(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				System.out.println("Manager data file not found: " + filePath);
				return false;
			}

			// Create manager database if not exists
			if (managerDatabase == null) {
				managerDatabase = new ManagerDatabase();
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			// Skip header line
			reader.readLine();

			// Read each manager record
			ArrayList<Manager> managers = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				if (data.length >= 5) {
					String name = data[0].trim();
					String nric = data[1].trim();
					int age = Integer.parseInt(data[2].trim());
					MarriageStatusEnum maritalStatus = parseMaritalStatus(data[3].trim());
					String password = data[4].trim();

					Manager manager = new Manager();
					manager.setName(name);
					manager.setNric(nric);
					manager.setAge(age);
					manager.setMaritalStatus(maritalStatus);
					manager.setPassword(password);

					managers.add(manager);
				}
			}

			managerDatabase.setManagers(managers);
			reader.close();

			System.out.println("Read " + managers.size() + " managers from " + filePath);
			return true;
		} catch (Exception e) {
			System.out.println("Error reading manager data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 * @param filePath
	 */
	public static boolean readProjectData(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				System.out.println("Project data file not found: " + filePath);
				return false;
			}

			// Create project database if not exists
			if (projectDatabase == null) {
				projectDatabase = new ProjectDatabase();
			}

			// Ensure manager and officer databases exist
			if (managerDatabase == null || officerDatabase == null) {
				System.out.println("Manager and Officer databases must be loaded before loading Project data");
				return false;
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			// Skip header line
			reader.readLine();

			// Read each project record
			ArrayList<Project> projects = new ArrayList<>();
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				if (data.length >= 13) {
					String projectName = data[0].trim();
					String neighborhood = data[1].trim();

					// Read flat types (could be multiple)
					ArrayList<FlatType> flatTypes = new ArrayList<>();

					// First flat type
					if (!data[2].trim().equals("") && !data[3].trim().equals("")){
						FlatTypeEnum type1 = parseFlatType(data[2].trim());
						int numUnits1 = Integer.parseInt(data[3].trim());
						double price1 = Double.parseDouble(data[4].trim());
						FlatType flatType1 = new FlatType();
						flatType1.setType(type1);
						flatType1.setNumUnits(numUnits1);
						flatType1.setAvailableUnits(numUnits1); // Initially all units are available
						flatType1.setSellingPrice(price1);
						flatTypes.add(flatType1);
					}

					// Second flat type
					if (!data[5].trim().equals("") && !data[6].trim().equals("")){
						FlatTypeEnum type2 = parseFlatType(data[5].trim());
						int numUnits2 = Integer.parseInt(data[6].trim());
						double price2 = Double.parseDouble(data[7].trim());
						FlatType flatType2 = new FlatType();
						flatType2.setType(type2);
						flatType2.setNumUnits(numUnits2);
						flatType2.setAvailableUnits(numUnits2); // Initially all units are available
						flatType2.setSellingPrice(price2);
						flatTypes.add(flatType2);
					}

					// Application dates
					LocalDate startDate = LocalDate.parse(data[8].trim(), dateFormatter);
					LocalDate endDate = LocalDate.parse(data[9].trim(), dateFormatter);

					// Manager
					String managerName = data[10].trim();
					Manager manager = null;
					for (Manager m : managerDatabase.getManagers()) {
						if (m.getName().equals(managerName)) {
							manager = m;
							break;
						}
					}

					// Officer slots
					int officerSlots = Integer.parseInt(data[11].trim());

					// Assigned officers
					ArrayList<Officer> assignedOfficers = new ArrayList<>();
					if (data.length > 12 && !data[12].isEmpty()) {
						String[] officerNames = data[12].replaceAll("^\"|\"$", "").split(",");
						for (String officerName : officerNames) {
							for (Officer o : officerDatabase.getOfficers()) {
								if (o.getName().equals(officerName.trim())) {
									assignedOfficers.add(o);
									break;
								}
							}
						}
					}

					// Create project
					Project project = new Project();
					project.setProjectName(projectName);
					project.setNeighborhood(neighborhood);
					project.setApplicationStartDate(startDate);
					project.setApplicationEndDate(endDate);
					project.setManager(manager);
					project.setOfficerSlots(officerSlots);
					project.setFlatTypes(flatTypes);
					project.setAssignedOfficers(assignedOfficers);
					project.setVisibility(VisibilityEnum.VISIBLE); // Default visibility

					projects.add(project);

					// Update Manager's managed projects list
					if (manager != null) {
						ArrayList<Project> managerProjects = manager.getManagedProjects();
						if (managerProjects == null) {
							managerProjects = new ArrayList<>();
							manager.setManagedProjects(managerProjects);
						}
						managerProjects.add(project);
					}

					// Update each Officer's attached projects list
					for (Officer officer : assignedOfficers) {
						ArrayList<Project> officerProjects = officer.getAssignedProjects();
						if (officerProjects == null || !officerProjects.contains(project)) {
							officer.assignToProject(project);
						}
					}
				}
			}

			projectDatabase.setProjects(projects);
			reader.close();

			System.out.println("Read " + projects.size() + " projects from " + filePath);
			return true;
		} catch (Exception e) {
			System.out.println("Error reading project data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Read BTO Application data from file
	 * @param filePath The path to the BTO Application data file
	 * @return True if successful, false otherwise
	 */
	public static boolean readBTOApplicationData(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				System.out.println("BTO Application data file not found: " + filePath);
				return false;
			}

			// Create BTO Application database if not exists
			if (btoApplicationDatabase == null) {
				btoApplicationDatabase = new BTOApplicationDatabase();
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			// Skip header line
			reader.readLine();

			// Read each bto application record
			ArrayList<BTOApplication> applications = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				if (data.length >= 7) {
					String applicationID = data[0].trim();
					LocalDate date = LocalDate.parse(data[1].trim());
					String applicantName = data[2].trim();
					String projectName = data[3].trim();
					FlatTypeEnum flatType = parseFlatType(data[4].trim());
					BTOApplicationStatusEnum status = BTOApplicationStatusEnum.valueOf(data[5].trim());
					WithdrawalStatusEnum withdrawalStatus = WithdrawalStatusEnum.valueOf(data[6].trim());

					Applicant applicant = Applicant.findApplicantByName(applicantName);
					Project project = Project.findProjectByName(projectName);

					if (applicant == null || project == null) return false;

					BTOApplication application = new BTOApplication(applicationID, applicant, project, flatType);
					application.setApplicationDate(date);
					application.setStatus(status);
					application.setWithdrawalStatus(withdrawalStatus);

					applications.add(application);
					
					// link application to the applicant
					applicant.addApplication(application);
				}
			}

			btoApplicationDatabase.setApplications(applications);
			reader.close();

			System.out.println("Read " + applications.size() + " BTO Applications from " + filePath);

			return true;

		} catch (Exception e) {
			System.out.println("Error reading BTO Application data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Read Officer Application data from file
	 * @param filePath The path to the Officer Application data file
	 * @return True if successful, false otherwise
	 */
	public static boolean readOfficerApplicationData(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				System.out.println("Officer Application data file not found: " + filePath);
				return false;
			}

			// Create officer application database if not exists
			if (officerApplicationDatabase == null) {
				officerApplicationDatabase = new OfficerApplicationDatabase();
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			// Skip header line
			reader.readLine();

			// read each officer applications record
			ArrayList<OfficerApplication> officerApplications = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				if (data.length >= 5) {
					String officerApplicationID = data[0].trim();
					LocalDate date = LocalDate.parse(data[1].trim());
					String officerName = data[2].trim();
					String projectName = data[3].trim();
					OfficerApplicationStatusEnum status = OfficerApplicationStatusEnum.valueOf(data[4].trim());

					Officer officer = Officer.findOfficerByName(officerName);
					Project project = Project.findProjectByName(projectName);

					if (officer == null || project == null) return false;

					OfficerApplication officerApplication = new OfficerApplication(officerApplicationID, date, officer, project, status);

					officerApplications.add(officerApplication);
					officer.addOfficerApplication(officerApplication); // link application to the officer
				}
			}

			officerApplicationDatabase.setApplications(officerApplications);
			reader.close();

			System.out.println("Read " + officerApplications.size() + " Officer Applications from " + filePath);
			return true;
		} catch (Exception e) {
			System.out.println("Error reading Officer Application data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Read Booking data from file
	 * @param filePath The path to the Booking data file
	 * @return True if successful, false otherwise
	 */
	public static boolean readBookingData(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				System.out.println("Booking data file not found: " + filePath);
				return false;
			}

			// Create database if not exists
			if (bookingDatabase == null) {
				bookingDatabase = new BookingDatabase();
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			// Skip header line
			reader.readLine();

			// Read each booking record
			ArrayList<Booking> bookings = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				if (data.length >= 6) {

					String bookingID = data[0].trim();
					LocalDate bookingDateTime = LocalDate.parse(data[1].trim());
					String applicationID = data[2].trim();
					String processingOfficer = data[3].trim();
					FlatTypeEnum flatType = parseFlatType(data[4].trim());
					BookingStatusEnum status = BookingStatusEnum.valueOf(data[5].trim());

					BTOApplication bookingApplication = BTOApplication.findApplicationByID(applicationID);

					Officer officer = Officer.findOfficerByName(processingOfficer);

					Booking booking = new Booking(bookingID, bookingDateTime, bookingApplication, officer, flatType, status);

					bookings.add(booking);
					bookingApplication.getApplicant().addBooking(booking); // link booking to the applicant
				}
			}

			bookingDatabase.setBookings(bookings);
			reader.close();

			System.out.println("Read " + bookings.size() + " bookings from " + filePath);

			return true;
		} catch (Exception e) {
			System.out.println("Error reading Booking data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Read Receipt data from file
	 * @param filePath The path to the Receipt data file
	 * @return True if successful, false otherwise
	 */
	public static boolean readReceiptData(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				System.out.println("Receipt data file not found: " + filePath);
				return false;
			}

			// Create database if not exists
			if (receiptDatabase == null) {
				receiptDatabase = new ReceiptDatabase();
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			// Skip header line
			reader.readLine();

			// Reach each receipt record
			ArrayList<Receipt> receipts = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				if (data.length >= 3) {
					String receiptNumber = data[0].trim();
					LocalDate date = LocalDate.parse(data[1].trim());
					String btoApplicationID = data[2].trim();
					BTOApplication btoApplication = BTOApplication.findApplicationByID(btoApplicationID);
					Booking theBooking = Booking.findBookingByApplication(btoApplication);

					Receipt receipt = new Receipt(receiptNumber, date, theBooking);
					receipts.add(receipt);
				}
			}

			receiptDatabase.setReceipts(receipts);
			reader.close();

			System.out.println("Read " + receipts.size() + " receipts from " + filePath);

			return true;
		} catch (Exception e) {
			System.out.println("Error reading Receipt data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Read Enquiry data from file
	 * @param filePath The path to the Enquiry data file
	 * @return True if successful, false otherwise
	 */
	public static boolean readEnquiryData(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				System.out.println("Enquiry data file not found: " + filePath);
				return false;
			}

			// Create database if not exists
			if (enquiryDatabase == null) {
				enquiryDatabase = new EnquiryDatabase();
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			// Skip header line
			reader.readLine();

			ArrayList<Enquiry> enquiries = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				if (data.length >= 9) {
					String enquiryID = data[0].trim();
					LocalDate date = LocalDate.parse(data[1].trim());
					String content = data[2].trim();
					String reply = data[3].trim();
					LocalDate replyDate = LocalDate.parse(data[4].trim());
					String submittedBy = data[5].trim();
					Applicant applicantSubmit = Applicant.findApplicantByName(submittedBy);

					String projectName = data[6].trim();
					Project project = Project.findProjectByName(projectName);

					EnquiryStatusEnum status = EnquiryStatusEnum.valueOf(data[7].trim());
					String respondent = data[8].trim();
					Manager isManager = Manager.findManagerByName(respondent);
					User userRespondent = null;
					if (isManager == null) {
						Officer isOfficer = Officer.findOfficerByName(respondent);
						userRespondent = (User) isOfficer;
					} else {
						userRespondent = (User) isManager;
					}

					Enquiry enquiry = new Enquiry(enquiryID, date, content, reply, replyDate, applicantSubmit, project, status, userRespondent);

					enquiries.add(enquiry);
					applicantSubmit.addEnquiry(enquiry); // link enquiry to the applicant
				}
			}

			enquiryDatabase.setEnquiries(enquiries);
			reader.close();

			System.out.println("Read " + enquiries.size() + " enquiries from " + filePath);
			return true;

		} catch (Exception e) {
			System.out.println("Error reading Enquiry data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Individual write methods for each database type
	 * @param filePath
	 */
	public static boolean writeApplicantData(String filePath) {
		try {
			// Check if applicant database exists
			if (applicantDatabase == null || applicantDatabase.getApplicants() == null) {
				System.out.println("Applicant database is empty, nothing to write.");
				return false;
			}

			int recordsWritten = 0;
			try (FileWriter writer = new FileWriter(filePath)) {
				// Write header
				writer.write("Name,NRIC,Age,Marital Status,Password\n");

				// Write each applicant
				for (Applicant applicant : applicantDatabase.getApplicants()) {
					writer.write(applicant.getName() + ",");
					writer.write(applicant.getNric() + ",");
					writer.write(applicant.getAge() + ",");
					writer.write(applicant.getMaritalStatus().name() + ",");
					writer.write(applicant.getPassword() + "\n");
					recordsWritten++;
				}
			}

			System.out.println("Successfully wrote " + recordsWritten + " applicant records to " + filePath);
			return true;
		} catch (Exception e) {
			System.out.println("Error writing applicant data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 * @param filePath
	 */
	public static boolean writeOfficerData(String filePath) {
		try {
			// Check if officer database exists
			if (officerDatabase == null || officerDatabase.getOfficers() == null) {
				System.out.println("Officer database is empty, nothing to write.");
				return false;
			}

			int recordsWritten = 0;
			try (FileWriter writer = new FileWriter(filePath)) {
				// Write header
				writer.write("Name,NRIC,Age,Marital Status,Password\n");

				// Write each officer
				for (Officer officer : officerDatabase.getOfficers()) {
					writer.write(officer.getName() + ",");
					writer.write(officer.getNric() + ",");
					writer.write(officer.getAge() + ",");
					writer.write(officer.getMaritalStatus().name() + ",");
					writer.write(officer.getPassword() + "\n");
					recordsWritten++;
				}
			}

			System.out.println("Successfully wrote " + recordsWritten + " officer records to " + filePath);
			return true;
		} catch (Exception e) {
			System.out.println("Error writing officer data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 * @param filePath
	 */
	public static boolean writeManagerData(String filePath) {
		try {
			// Check if manager database exists
			if (managerDatabase == null || managerDatabase.getManagers() == null) {
				System.out.println("Manager database is empty, nothing to write.");
				return false;
			}

			FileWriter writer = new FileWriter(filePath);

			// Write header
			writer.write("Name,NRIC,Age,Marital Status,Password\n");

			// Write each manager
			for (Manager manager : managerDatabase.getManagers()) {
				writer.write(manager.getName() + ",");
				writer.write(manager.getNric() + ",");
				writer.write(manager.getAge() + ",");
				writer.write(manager.getMaritalStatus().name() + ",");
				writer.write(manager.getPassword() + "\n");
			}

			writer.close();
			System.out.println("Successfully wrote " + managerDatabase.getManagers().size() + " managers to " + filePath);
			return true;
		} catch (Exception e) {
			System.out.println("Error writing manager data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 * @param filePath
	 */
	public static boolean writeProjectData(String filePath) {
		try {
			// Check if project database exists
			if (projectDatabase == null || projectDatabase.getProjects() == null) {
				System.out.println("Project database is empty, nothing to write.");
				return false;
			}

			FileWriter writer = new FileWriter(filePath);

			// Write header
			writer.write("Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer\n");

			// Write each project
			for (Project project : projectDatabase.getProjects()) {
				writer.write(project.getProjectName() + ",");
				writer.write(project.getNeighborhood() + ",");

				// Write flat types
				ArrayList<FlatType> flatTypes = project.getFlatTypes();
				if (flatTypes.size() >= 1) {
					FlatType type1 = flatTypes.get(0);
					writer.write(formatFlatType(type1.getType()) + ",");
					writer.write(type1.getNumUnits() + ",");
					writer.write(type1.getSellingPrice() + ",");
				} else {
					writer.write(",,,");
				}

				if (flatTypes.size() >= 2) {
					FlatType type2 = flatTypes.get(1);
					writer.write(formatFlatType(type2.getType()) + ",");
					writer.write(type2.getNumUnits() + ",");
					writer.write(type2.getSellingPrice() + ",");
				} else {
					writer.write(",,,");
				}

				// Write dates
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				writer.write(project.getApplicationStartDate().format(dateFormatter) + ",");
				writer.write(project.getApplicationEndDate().format(dateFormatter) + ",");

				// Write manager
				if (project.getManager() != null) {
					writer.write(project.getManager().getName() + ",");
				} else {
					writer.write(",");
				}

				// Write officer slots
				writer.write(project.getOfficerSlots() + ",");

				// Write assigned officers
				ArrayList<Officer> officers = project.getAssignedOfficers();
				if (officers != null && !officers.isEmpty()) {
					writer.write("\"");
					for (int i = 0; i < officers.size(); i++) {
						writer.write(officers.get(i).getName());
						if (i < officers.size() - 1) {
							writer.write(",");
						}
					}
					writer.write("\"");
				}

				writer.write("\n");
			}

			writer.close();
			System.out.println("Successfully wrote " + projectDatabase.getProjects().size() + " projects to " + filePath);
			return true;
		} catch (Exception e) {
			System.out.println("Error writing project data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public static boolean writeBTOApplicationData(String filePath) {
		try {
			if (btoApplicationDatabase == null || btoApplicationDatabase.getApplications() == null) {
				System.out.println("BTO Application database is empty, nothing to write.");
				return false;
			}

			FileWriter writer = new FileWriter(filePath);
			writer.write("Application ID,Application Date,Applicant Name,Project Name,Flat Type,Status,Withdrawal Status\n");

			for (BTOApplication app : btoApplicationDatabase.getApplications()) {
				writer.write(app.getApplicationID() + ",");
				writer.write(app.getApplicationDate() + ",");
				writer.write(app.getApplicant().getName() + ",");
				writer.write(app.getProject().getProjectName() + ",");
				writer.write(app.getFlatType().name() + ",");
				writer.write(app.getStatus().name() + ",");
				writer.write(app.getWithdrawalStatus().name() + "\n");
			}
			writer.close();

			System.out.println("Successfully wrote " + btoApplicationDatabase.getApplications().size() + " BTO applications to " + filePath);
			return true;
		} catch (Exception e) {
			System.out.println("Error writing BTO Application data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public static boolean writeOfficerApplicationData(String filePath) {
		try {
			if (officerApplicationDatabase == null || officerApplicationDatabase.getApplications() == null) {
				System.out.println("Officer Application database is empty, nothing to write.");
				return false;
			}

			int recordsWritten = 0;
			try (FileWriter writer = new FileWriter(filePath)) {
				writer.write("Officer Application ID,Application Date,Officer Name,Project Name,Application Status\n");

				for (OfficerApplication app : officerApplicationDatabase.getApplications()) {
					writer.write(app.getOfficerApplicationID() + "," +
							app.getApplicationDate() + "," +
							app.getOfficer().getName() + "," +
							app.getProject().getProjectName() + "," +
							app.getStatus().name() + "\n");
					recordsWritten++;
				}
			}

			System.out.println("Successfully wrote " + recordsWritten + " Officer applications to " + filePath);
			return true;
		} catch (Exception e) {
			System.out.println("Error writing Officer Application data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public static boolean writeBookingData(String filePath) {
		try {
			if (bookingDatabase == null || bookingDatabase.getBookings() == null) {
				System.out.println("Booking database is empty, nothing to write.");
				return false;
			}

			int recordsWritten = 0;
			try (FileWriter writer = new FileWriter(filePath)) {
				writer.write("Booking ID,Booking Date,BTO Application ID,Processing Officer,Flat Type,Booking Status\n");

				for (Booking booking : bookingDatabase.getBookings()) {
					writer.write(booking.getBookingID() + "," +
							booking.getBookingDateTime() + "," +
							booking.getApplication().getApplicationID() + "," +
							booking.getProcessingOfficer().getName() + "," +
							booking.getFlatType().name() + "," +
							booking.getStatus().name() + "\n");
					recordsWritten++;
				}
			}

			System.out.println("Successfully wrote " + recordsWritten + " bookings to " + filePath);
			return true;
		} catch (Exception e) {
			System.out.println("Error writing Booking data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public static boolean writeReceiptData(String filePath) {
		try {
			if (receiptDatabase == null || receiptDatabase.getReceipts() == null) {
				System.out.println("Receipt database is empty, nothing to write.");
				return false;
			}

			int recordsWritten = 0;
			try (FileWriter writer = new FileWriter(filePath)) {
				writer.write("Receipt Number,Date,Booking ID\n");

				for (Receipt receipt : receiptDatabase.getReceipts()) {
					writer.write(receipt.getReceiptNumber() + "," +
							receipt.getDate() + "," +
							receipt.getBooking().getApplication().getApplicationID() + "\n");
					recordsWritten++;
				}
			}

			System.out.println("Successfully wrote " + recordsWritten + " receipts to " + filePath);
			return true;
		} catch (Exception e) {
			System.out.println("Error writing Receipt data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public static boolean writeEnquiryData(String filePath) {
		try {
			if (enquiryDatabase == null || enquiryDatabase.getEnquiries() == null) {
				System.out.println("Enquiry database is empty, nothing to write.");
				return false;
			}

			int recordsWritten = 0;
			try (FileWriter writer = new FileWriter(filePath)) {
				writer.write("Enquiry ID,Date,Content,Reply,Reply Date,Submitted By,Project,Status,Respondent\n");

				for (Enquiry enquiry : enquiryDatabase.getEnquiries()) {
					writer.write(enquiry.getEnquiryID() + "," +
							enquiry.getDateTime() + "," +
							enquiry.getContent() + "," +
							enquiry.getReply() + "," +
							enquiry.getReplyDate() + "," +
							enquiry.getSubmittedBy().getName() + "," +
							enquiry.getProject().getProjectName() + "," +
							enquiry.getStatus().name() + "," +
							enquiry.getRespondent().getName() + "\n");
					recordsWritten++;
				}
			}

			System.out.println("Successfully wrote " + recordsWritten + " enquiries to " + filePath);
			return true;

		} catch (Exception e) {
			System.out.println("Error writing Enquiry data: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Helper method to parse marital status string to enum
	 */
	private static MarriageStatusEnum parseMaritalStatus(String statusStr) {
		if (statusStr.equalsIgnoreCase("Married")) {
			return MarriageStatusEnum.MARRIED;
		} else {
			return MarriageStatusEnum.SINGLE;
		}
	}

	/**
	 * Helper method to parse flat type string to enum
	 */
	private static FlatTypeEnum parseFlatType(String typeStr) {
		if (typeStr.equalsIgnoreCase("2-Room")) {
			return FlatTypeEnum.TWO_ROOM;
		} else if (typeStr.equalsIgnoreCase("3-Room")) {
			return FlatTypeEnum.THREE_ROOM;
		} else {
			// Default
			return FlatTypeEnum.TWO_ROOM;
		}
	}

	/**
	 * Helper method to format flat type enum to string
	 */
	private static String formatFlatType(FlatTypeEnum type) {
		if (type == FlatTypeEnum.TWO_ROOM) {
			return "2-Room";
		} else if (type == FlatTypeEnum.THREE_ROOM) {
			return "3-Room";
		} else {
			return "";
		}
	}

	/**
	 * Method to read all data files at once
	 * @param dataPath The path to the data files
	 * @return True if all files were read successfully, false otherwise
	 */
	public static boolean readAllData(String dataPath) {
		boolean success = true;

		// Ensure data path ends with a separator if needed
		if (!dataPath.endsWith(File.separator) && !dataPath.isEmpty()) {
			dataPath = dataPath + File.separator;
		}

		// Check if directory exists
		File dataDir = new File(dataPath);
		if (!dataDir.exists() || !dataDir.isDirectory()) {
			System.out.println("Data directory does not exist: " + dataPath);
			System.out.println("Using current directory instead.");
			dataPath = "";
		}

		// Manager data
		File managerFile = new File(dataPath + MANAGER_FILE);
		if (!managerFile.exists()) {
			System.out.println("Warning: Manager data file not found: " + managerFile.getPath());
		} else {
			if (!readManagerData(managerFile.getPath())) {
				System.out.println("Failed to read manager data. Some data dependencies may fail.");
				success = false;
			} else {
				int managerCount = Manager.getAllManagers().size();
				System.out.println(managerCount + " Manager(s) loaded successfully.");
			}
		}

		// Officer data
		File officerFile = new File(dataPath + OFFICER_FILE);
		if (!officerFile.exists()) {
			System.out.println("Warning: Officer data file not found: " + officerFile.getPath());
		} else {
			if (!readOfficerData(officerFile.getPath())) {
				System.out.println("Failed to read officer data. Some data dependencies may fail.");
				success = false;
			} else {
				int officerCount = Officer.getAllOfficers().size();
				System.out.println(officerCount + " Officer(s) loaded successfully.");
			}
		}

		// Project data
		File projectFile = new File(dataPath + PROJECT_FILE);
		if (!projectFile.exists()) {
			System.out.println("Warning: Project data file not found: " + projectFile.getPath());
		} else {
			if (!readProjectData(projectFile.getPath())) {
				System.out.println("Failed to read project data.");
				success = false;
			} else {
				int projectCount = Project.getAllProjects().size();
				System.out.println(projectCount + " Project(s) loaded successfully.");
			}
		}

		// Applicant data
		File applicantFile = new File(dataPath + APPLICANT_FILE);
		if (!applicantFile.exists()) {
			System.out.println("Warning: Applicant data file not found: " + applicantFile.getPath());
		} else {
			if (!readApplicantData(applicantFile.getPath())) {
				System.out.println("Failed to read applicant data.");
				success = false;
			} else {
				int applicantCount = Applicant.getAllApplicants().size();
				System.out.println(applicantCount + " Applicant(s) loaded successfully.");
			}
		}

		// BTO applications
		File btoAppFile = 	new File(dataPath + BTO_APPLICATION_FILE);
		if (!btoAppFile.exists()) {
			System.out.println("Warning: BTO Application data file not found: " + btoAppFile.getPath());
		} else {
			if (!readBTOApplicationData(btoAppFile.getPath())) {
				System.out.println("Failed to read BTO application data.");
				success = false;
			} else {
				int btoAppCount = BTOApplication.getAllApplications().size();
				System.out.println(btoAppCount + " BTO Application(s) loaded successfully.");
			}
		}

		// Officer applications
		File officerAppFile = new File(dataPath + OFFICER_APPLICATION_FILE);
		if (!officerAppFile.exists()) {
			System.out.println("Warning: Officer Application data file not found: " + officerAppFile.getPath());
		} else {
			if (!readOfficerApplicationData(officerAppFile.getPath())) {
				System.out.println("Failed to read officer application data.");
				success = false;
			} else {
				int officerAppCount = OfficerApplication.getAllApplications().size();
				System.out.println(officerAppCount + " Officer Application(s) loaded successfully.");
			}
		}

		// Bookings
		File bookingFile = new File(dataPath + BOOKING_FILE);
		if (!bookingFile.exists()) {
			System.out.println("Warning: Booking data file not found: " + bookingFile.getPath());
		} else {
			if (!readBookingData(bookingFile.getPath())) {
				System.out.println("Failed to read booking data.");
				success = false;
			} else {
				int bookingCount = Booking.getAllBookings().size();
				System.out.println(bookingCount + " Booking(s) loaded successfully.");
			}
		}

		// Receipts
		File receiptFile = new File(dataPath + RECEIPT_FILE);
		if (!receiptFile.exists()) {
			System.out.println("Warning: Receipt data file not found: " + receiptFile.getPath());
		} else {
			if (!readReceiptData(receiptFile.getPath())) {
				System.out.println("Failed to read receipt data.");
				success = false;
			} else {
				int receiptCount = Receipt.getAllReceipts().size();
				System.out.println(receiptCount + " Receipt(s) loaded successfully.");
			}
		}

		// Enquiries
		File enquiryFile = new File(dataPath + ENQUIRY_FILE);
		if (!enquiryFile.exists()) {
			System.out.println("Warning: Enquiry data file not found: " + enquiryFile.getPath());
		} else {
			if (!readEnquiryData(enquiryFile.getPath())) {
				System.out.println("Failed to read enquiry data.");
				success = false;
			} else {
				int enquiryCount = Enquiry.getAllEnquiries().size();
				System.out.println(enquiryCount + " Enquiry(s) loaded successfully.");
			}
		}

		return success;
	}
}