package entity;

import database.*;
import enums.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Entity class representing an officer's application to be assigned to a BTO project.
 * This class tracks the application process for officers requesting to work on specific
 * projects, including the application date, officer details, project information,
 * and current status.
 *
 * Officer applications go through a workflow that may include approval or rejection
 * by project managers. The application status is tracked through the system.
 */
public class OfficerApplication {

	/**
	 * The officer application ID associated with this officer application
	 */
	private String officerApplicationID;
	/**
	 * Date when the application was submitted
	 */
	private LocalDate applicationDate;

	/**
	 * Reference to the officer who submitted the application
	 */
	private Officer officer;

	/**
	 * Reference to the project the officer is applying for
	 */
	private Project project;

	/**
	 * Current status of the application (PENDING, APPROVED, REJECTED)
	 */
	private OfficerApplicationStatusEnum status;

	/**
	 * Static reference to the database of all officer applications in the system
	 */
	private static OfficerApplicationDatabase database;

	/**
	 * Default constructor that initializes a new officer application with current date,
	 * PENDING status, and null officer and project references.
	 */
	public OfficerApplication() {
		this.applicationDate = LocalDate.now();
		this.officer = null;
		this.project = null;
		this.status = OfficerApplicationStatusEnum.PENDING;
		generateOfficerApplicationID();
	}

	/**
	 * Full constructor that initializes an officer application with all properties.
	 * If some parameters are null, default values are applied.
	 *
	 * @param applicationDate Date when the application was submitted
	 * @param officer Reference to the officer who submitted the application
	 * @param project Reference to the project the officer is applying for
	 * @param status Current status of the application
	 */
	public OfficerApplication(LocalDate applicationDate, Officer officer, Project project, OfficerApplicationStatusEnum status) {
		this.applicationDate = applicationDate != null ? applicationDate : LocalDate.now();
		this.officer = officer;
		this.project = project;
		this.status = status != null ? status : OfficerApplicationStatusEnum.PENDING;
		generateOfficerApplicationID();
	}

	/**
	 * Full constructor that initializes an officer application with all properties.
	 * If some parameters are null, default values are applied.
	 *
	 * @param officerApplicationID Officer application ID associated with this officer application
	 * @param applicationDate Date when the application was submitted
	 * @param officer Reference to the officer who submitted the application
	 * @param project Reference to the project the officer is applying for
	 * @param status Current status of the application
	 */
	public OfficerApplication(String officerApplicationID, LocalDate applicationDate, Officer officer, Project project, OfficerApplicationStatusEnum status) {
		this.officerApplicationID = officerApplicationID;
		this.applicationDate = applicationDate != null ? applicationDate : LocalDate.now();
		this.officer = officer;
		this.project = project;
		this.status = status != null ? status : OfficerApplicationStatusEnum.PENDING;
	}

	/**
	 * Private helper method to generate a unique officer application ID.
	 * Uses system time and a random number to ensure uniqueness.
	 *
	 */
	private void generateOfficerApplicationID() {
		this.officerApplicationID = "OFF-APP-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
	}

	/**
	 * Sets the officer application ID associated with this officer application.
	 *
	 * @param officerApplicationID The officer application ID associated with this officer application
	 */
	public void setOfficerApplicationID(String officerApplicationID) {
		this.officerApplicationID = officerApplicationID;
	}

	/**
	 * Sets the date when the application was submitted.
	 *
	 * @param applicationDate The LocalDate representing the submission date
	 */
	public void setApplicationDate(LocalDate applicationDate) {
		this.applicationDate = applicationDate;
	}

	/**
	 * Sets the officer who submitted this application.
	 *
	 * @param officer The Officer entity who created the application
	 */
	public void setOfficer(Officer officer) {
		this.officer = officer;
	}

	/**
	 * Sets the project this application is for.
	 *
	 * @param project The Project entity the officer is applying to work on
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * Sets the status of this application.
	 *
	 * @param status The OfficerApplicationStatusEnum value representing the current status
	 */
	public void setStatus(OfficerApplicationStatusEnum status) {
		this.status = status;
	}

	/**
	 * Sets the static database reference for all OfficerApplication objects.
	 *
	 * @param database The OfficerApplicationDatabase to use for storing applications
	 */
	public static void setDatabase(OfficerApplicationDatabase database) {
		OfficerApplication.database = database;
	}

	/**
	 * Gets the officer application ID.
	 *
	 * @return The officer application ID associated with this officer application
	 */
	public String getOfficerApplicationID() {
		return this.officerApplicationID;
	}

	/**
	 * Gets the submission date of this application.
	 *
	 * @return The LocalDate representing when the application was submitted
	 */
	public LocalDate getApplicationDate() {
		return this.applicationDate;
	}

	/**
	 * Gets the officer who submitted this application.
	 *
	 * @return The Officer entity who created the application
	 */
	public Officer getOfficer() {
		return this.officer;
	}

	/**
	 * Gets the project this application is for.
	 *
	 * @return The Project entity the officer is applying to work on
	 */
	public Project getProject() {
		return this.project;
	}

	/**
	 * Gets the current status of this application.
	 *
	 * @return The OfficerApplicationStatusEnum value representing the status
	 */
	public OfficerApplicationStatusEnum getStatus() {
		return this.status;
	}

	/**
	 * Gets the static database reference for all OfficerApplication objects.
	 *
	 * @return The OfficerApplicationDatabase being used to store applications
	 */
	public static OfficerApplicationDatabase getDatabase() {
		return OfficerApplication.database;
	}

	/**
	 * Finds all applications submitted on a specific date.
	 *
	 * @param date The LocalDate to search for
	 * @return ArrayList of OfficerApplication objects submitted on the specified date
	 */
	public static ArrayList<OfficerApplication> findApplicationsByDate(LocalDate date) {
		ArrayList<OfficerApplication> result = new ArrayList<>();
		if (database == null || date == null) {
			return result;
		}

		for (OfficerApplication app : database.getApplications()) {
			if (date.equals(app.getApplicationDate())) {
				result.add(app);
			}
		}
		return result;
	}

	/**
	 * Finds all applications submitted by a specific officer.
	 *
	 * @param officer The Officer entity to search for
	 * @return ArrayList of OfficerApplication objects submitted by the specified officer
	 */
	public static ArrayList<OfficerApplication> findApplicationsByOfficer(Officer officer) {
		ArrayList<OfficerApplication> result = new ArrayList<>();
		if (database == null || officer == null) {
			return result;
		}

		for (OfficerApplication app : database.getApplications()) {
			if (officer.equals(app.getOfficer())) {
				result.add(app);
			}
		}
		return result;
	}

	/**
	 * Finds all applications for a specific project.
	 *
	 * @param project The Project entity to search for
	 * @return ArrayList of OfficerApplication objects for the specified project
	 */
	public static ArrayList<OfficerApplication> findApplicationsByProject(Project project) {
		ArrayList<OfficerApplication> result = new ArrayList<>();
		if (database == null || project == null) {
			return result;
		}

		for (OfficerApplication app : database.getApplications()) {
			if (project.equals(app.getProject())) {
				result.add(app);
			}
		}
		return result;
	}

	/**
	 * Finds all applications with a specific status.
	 *
	 * @param status The OfficerApplicationStatusEnum value to search for
	 * @return ArrayList of OfficerApplication objects with the specified status
	 */
	public static ArrayList<OfficerApplication> findApplicationsByStatus(enums.OfficerApplicationStatusEnum status) {
		ArrayList<OfficerApplication> result = new ArrayList<>();
		if (database == null || status == null) {
			return result;
		}

		for (OfficerApplication app : database.getApplications()) {
			if (status.equals(app.getStatus())) {
				result.add(app);
			}
		}
		return result;
	}

	/**
	 * Adds an application to the database if it doesn't already exist.
	 *
	 * @param application The OfficerApplication object to add to the database
	 * @return boolean True if the application was successfully added, false otherwise
	 */
	public static boolean addToDatabase(OfficerApplication application) {
		if (database == null || application == null) {
			return false;
		}

		ArrayList<OfficerApplication> applications = database.getApplications();
		if (applications.contains(application)) {
			return false; // Already in database
		}

		applications.add(application);
		return true;
	}

	/**
	 * Removes an application from the database.
	 *
	 * @param application The OfficerApplication object to remove from the database
	 * @return boolean True if the application was successfully removed, false otherwise
	 */
	public static boolean removeFromDatabase(OfficerApplication application) {
		if (database == null || application == null) {
			return false;
		}

		ArrayList<OfficerApplication> applications = database.getApplications();
		return applications.remove(application);
	}

	/**
	 * Retrieves all officer applications stored in the database.
	 *
	 * @return ArrayList containing all OfficerApplication objects in the database
	 */
	public static ArrayList<OfficerApplication> getAllApplications() {
		if (database == null) {
			return new ArrayList<>();
		}
		return database.getApplications();
	}

	/**
	 * Returns a string representation of this officer application.
	 * Includes the date, officer, project, and status information.
	 *
	 * @return String representation of the officer application
	 */
	@Override()
	public String toString() {
		return "OfficerApplication [officerApplicationID=" + officerApplicationID + " + date=" + applicationDate + ", officer=" + (officer != null ? officer.getName() : "None") + ", project=" + (project != null ? project.getProjectName() : "None") + ", status=" + status + "]";
	}
}