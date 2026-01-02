package entity;

import enums.*;
import database.*;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 * The BTOApplication class represents an application for a BTO (Build-To-Order) flat.
 * Applications are submitted by applicants for specific projects and flat types.
 * Each application has a lifecycle with various statuses and can be withdrawn.
 * This class manages application data and provides functionality for creating,
 * tracking, and managing housing applications.
 */
public class BTOApplication {

	/**
	 * The application ID associated with this BTO application
	 */
	private String applicationID;
	/**
	 * The date when the application was submitted
	 */
	private LocalDate applicationDate;
	/**
	 * The applicant who submitted this application
	 */
	private Applicant applicant;
	/**
	 * The housing project being applied for
	 */
	private Project project;
	/**
	 * The type of flat being applied for
	 */
	private FlatTypeEnum flatType;
	/**
	 * The current status of the application (PENDING, SUCCESSFUL, etc.)
	 */
	private BTOApplicationStatusEnum status;
	/**
	 * The withdrawal status of the application (NA, PENDING, APPROVED, etc.)
	 */
	private WithdrawalStatusEnum withdrawalStatus;
	/**
	 * Static database reference for BTOApplication persistence
	 */
	private static BTOApplicationDatabase database;

	/**
	 * Default constructor that initializes a BTOApplication with default values.
	 * Creates an application with current date and PENDING status.
	 */
	public BTOApplication() {
		this.applicationDate = LocalDate.now();
		this.applicant = null;
		this.project = null;
		this.flatType = null;
		this.status = BTOApplicationStatusEnum.PENDING;
		this.withdrawalStatus = WithdrawalStatusEnum.NA;
		generateApplicationID();
	}

	/**
	 * Constructor that initializes a BTOApplication with the specified applicant, project, and flat type.
	 * The application date is set to the current date, status to PENDING,
	 * and withdrawal status to NA (not applicable).
	 * Also adds this application to the applicant's list of applications.
	 *
	 * @param applicant The applicant submitting the application
	 * @param project The project being applied for
	 * @param flatType The type of flat applied for
	 */
	public BTOApplication(Applicant applicant, Project project, FlatTypeEnum flatType) {
		this.applicationDate = LocalDate.now();
		this.applicant = applicant;
		this.project = project;
		this.flatType = flatType;
		this.status = BTOApplicationStatusEnum.PENDING;
		this.withdrawalStatus = WithdrawalStatusEnum.NA;

		// Add this application to applicant's applications if applicant is not null
		if (applicant != null) {
			applicant.addApplication(this);
		}
		generateApplicationID();
	}

	/**
	 * Constructor that initializes a BTOApplication with the specified applicant, project, and flat type.
	 * The application date is set to the current date, status to PENDING,
	 * and withdrawal status to NA (not applicable).
	 * Also adds this application to the applicant's list of applications.
	 *
	 * @param applicationID The BTO Application ID associated for this BTO application
	 * @param applicant The applicant submitting the application
	 * @param project The project being applied for
	 * @param flatType The type of flat applied for
	 */
	public BTOApplication(String applicationID, Applicant applicant, Project project, FlatTypeEnum flatType) {
		this.applicationID = applicationID;
		this.applicationDate = LocalDate.now();
		this.applicant = applicant;
		this.project = project;
		this.flatType = flatType;
		this.status = BTOApplicationStatusEnum.PENDING;
		this.withdrawalStatus = WithdrawalStatusEnum.NA;

		// Add this application to applicant's applications if applicant is not null
		if (applicant != null) {
			applicant.addApplication(this);
		}
	}

	/**
	 * Private helper method to generate a unique BTO application ID.
	 * Uses system time and a random number to ensure uniqueness.
	 */
	private void generateApplicationID() {
		// Generate a unique BTO Application ID based on current time
		this.applicationID = "BTO-APP-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
	}

	/**
	 * Sets the BTO application ID for this BTO application.
	 *
	 * @param applicationID The BTO application ID associated with this BTO application
	 */
	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}

	/**
	 * Sets the date when the application was submitted.
	 *
	 * @param applicationDate The application submission date
	 */
	public void setApplicationDate(LocalDate applicationDate) {
		this.applicationDate = applicationDate;
	}

	/**
	 * Sets the applicant who submitted this application.
	 *
	 * @param applicant The applicant submitting the application
	 */
	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	/**
	 * Sets the project being applied for.
	 *
	 * @param project The housing project
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * Sets the type of flat being applied for.
	 *
	 * @param flatType The flat type
	 */
	public void setFlatType(FlatTypeEnum flatType) {
		this.flatType = flatType;
	}

	/**
	 * Sets the current status of the application.
	 *
	 * @param status The application status
	 */
	public void setStatus(BTOApplicationStatusEnum status) {
		this.status = status;
	}

	/**
	 * Sets the withdrawal status of the application.
	 *
	 * @param withdrawalStatus The withdrawal status
	 */
	public void setWithdrawalStatus(WithdrawalStatusEnum withdrawalStatus) {
		this.withdrawalStatus = withdrawalStatus;
	}

	/**
	 * Sets the static database reference for BTOApplication persistence.
	 *
	 * @param database The database instance to use for BTOApplication storage
	 */
	public static void setDatabase(BTOApplicationDatabase database) {
		BTOApplication.database = database;
	}

	/**
	 * Gets the unique identifier for this application.
	 *
	 * @return The application ID
	 */
	public String getApplicationID() {
		return applicationID;
	}

	/**
	 * Gets the date when the application was submitted.
	 *
	 * @return The application submission date
	 */
	public LocalDate getApplicationDate() {
		return this.applicationDate;
	}

	/**
	 * Gets the applicant who submitted this application.
	 *
	 * @return The applicant
	 */
	public Applicant getApplicant() {
		return this.applicant;
	}

	/**
	 * Gets the project being applied for.
	 *
	 * @return The housing project
	 */
	public Project getProject() {
		return this.project;
	}

	/**
	 * Gets the type of flat being applied for.
	 *
	 * @return The flat type
	 */
	public FlatTypeEnum getFlatType() {
		return this.flatType;
	}

	/**
	 * Gets the current status of the application.
	 *
	 * @return The application status
	 */
	public BTOApplicationStatusEnum getStatus() {
		return this.status;
	}

	/**
	 * Gets the withdrawal status of the application.
	 *
	 * @return The withdrawal status
	 */
	public WithdrawalStatusEnum getWithdrawalStatus() {
		return this.withdrawalStatus;
	}

	/**
	 * Gets the database used for BTOApplication storage.
	 *
	 * @return The BTOApplication database instance
	 */
	public static BTOApplicationDatabase getDatabase() {
		return BTOApplication.database;
	}

	/**
	 * Requests withdrawal of this application.
	 * Only pending applications with no existing withdrawal request can be withdrawn.
	 *
	 * @return true if the withdrawal request was successful, false otherwise
	 */
	public boolean requestWithdrawal() {
		// Can only request withdrawal if the application is currently pending
		if (this.status == BTOApplicationStatusEnum.PENDING &&
				this.withdrawalStatus == WithdrawalStatusEnum.NA) {
			this.withdrawalStatus = WithdrawalStatusEnum.PENDING;
			return true;
		}
		return false;
	}

	/**
	 * Finds all applications submitted on a specific date.
	 *
	 * @param date The date to search for
	 * @return ArrayList of applications submitted on this date
	 */
	public static ArrayList<BTOApplication> findApplicationsByDate(LocalDate date) {
		ArrayList<BTOApplication> result = new ArrayList<>();
		if (database == null || date == null) {
			return result;
		}

		for (BTOApplication app : database.getApplications()) {
			if (date.equals(app.getApplicationDate())) {
				result.add(app);
			}
		}
		return result;
	}

	/**
	 * Finds all applications submitted by a specific applicant.
	 *
	 * @param applicant The applicant to search for
	 * @return ArrayList of applications submitted by this applicant
	 */
	public static ArrayList<BTOApplication> findApplicationsByApplicant(Applicant applicant) {
		ArrayList<BTOApplication> result = new ArrayList<>();
		if (database == null || applicant == null) {
			return result;
		}

		for (BTOApplication app : database.getApplications()) {
			if (applicant.equals(app.getApplicant())) {
				result.add(app);
			}
		}
		return result;
	}

	/**
	 * Finds all applications with a specific application ID.
	 *
	 * @param applicationID The applicant to search for
	 * @return application with the matching ID of the application
	 */
	public static BTOApplication findApplicationByApplicationID(String applicationID) {
		if (database == null || applicationID == null) {
			return null;
		}

		for (BTOApplication app : database.getApplications()) {
			if (applicationID.equals(app.getApplicationID())) {
				return app;
			}
		}

		return null;
	}


	/**
	 * Finds all applications for a specific project.
	 *
	 * @param project The project to search for
	 * @return ArrayList of applications for this project
	 */
	public static ArrayList<BTOApplication> findApplicationsByProject(Project project) {
		ArrayList<BTOApplication> result = new ArrayList<>();
		if (database == null || project == null) {
			return result;
		}

		for (BTOApplication app : database.getApplications()) {
			if (project.equals(app.getProject())) {
				result.add(app);
			}
		}
		return result;
	}

	/**
	 * Retrieves all applications from the database.
	 *
	 * @param applicationID The BTO application ID associated with the BTO application
	 * @return BTO Application associated with the BTO application ID
	 */
	public static BTOApplication findApplicationByID(String applicationID) {
		if (database == null || applicationID == null) {
			return null;
		}

		for (BTOApplication app : database.getApplications()) {
			if (applicationID.equals(app.getApplicationID())) {
				return app;
			}
		}
		return null;
	}

	/**
	 * Retrieves all applications from the database.
	 *
	 * @return ArrayList of all applications in the database, empty list if none
	 */
	public static ArrayList<BTOApplication> getAllApplications() {
		if (database == null) {
			return new ArrayList<>();
		}
		return database.getApplications();
	}

	/**
	 * Adds an application to the database.
	 * Automatically assigns a unique ID to the application.
	 *
	 * @param application The application to add
	 * @return true if the application was added successfully, false otherwise
	 */
	public static boolean addToDatabase(BTOApplication application) {
		if (database == null || application == null) {
			return false;
		}

		ArrayList<BTOApplication> applications = database.getApplications();
		if (applications.contains(application)) {
			return false; // Already in database
		}

		applications.add(application);
		return true;
	}

	/**
	 * Removes an application from the database.
	 *
	 * @param application The application to remove
	 * @return true if the application was removed successfully, false otherwise
	 */
	public static boolean removeFromDatabase(BTOApplication application) {
		if (database == null || application == null) {
			return false;
		}

		ArrayList<BTOApplication> applications = database.getApplications();
		return applications.remove(application);
	}

	/**
	 * Creates a string representation of this application.
	 *
	 * @return A string containing application details
	 */
	@Override()
	public String toString() {
		return "BTOApplication [id=" + applicationID + ", date=" + applicationDate +
				", applicant=" + (applicant != null ? applicant.getName() : "None") +
				", project=" + (project != null ? project.getProjectName() : "None") +
				", flatType=" + flatType + ", status=" + status +
				", withdrawalStatus=" + withdrawalStatus + "]";
	}
}