package entity;

import database.*;
import enums.*;
import java.util.ArrayList;

/**
 * Entity class representing an Officer in the BTO Management System.
 * 
 * <p>The Officer class represents a staff member who can be assigned to manage BTO projects.
 * Officers extend the {@link Applicant} class, allowing them to both manage projects
 * and also apply for housing as regular applicants. This dual role allows officers
 * to have all the capabilities of applicants while also managing assigned projects
 * and handling enquiries.</p>
 * 
 * <h2>Dual Role Design:</h2>
 * <p>Officers inherit from Applicant, meaning they can:</p>
 * <ul>
 *   <li>Apply for BTO flats as applicants (but not for projects they're assigned to)</li>
 *   <li>Be assigned to manage one or more BTO projects</li>
 *   <li>Process bookings for their assigned projects</li>
 *   <li>Respond to enquiries about their assigned projects</li>
 *   <li>Submit their own enquiries for other projects</li>
 * </ul>
 * 
 * <h2>Inheritance Hierarchy:</h2>
 * <pre>
 * {@link User}
 *   └── {@link Applicant}
 *         └── Officer
 * </pre>
 * 
 * <h2>Conflict of Interest:</h2>
 * <p>Officers cannot apply for BTO flats in projects they are assigned to manage.
 * This is enforced in the application logic to prevent conflicts of interest.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Create a new officer
 * Officer officer = new Officer();
 * officer.setName("Jane Officer");
 * officer.setNric("S9876543B");
 * 
 * // Assign to a project
 * officer.assignToProject(project);
 * 
 * // Check assignment
 * boolean isAssigned = officer.isAssignedToProject(project);
 * }</pre>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see Applicant
 * @see User
 * @see Project
 * @see OfficerApplication
 */
public class Officer extends Applicant {

	// ============================================================================
	// INSTANCE VARIABLES
	// ============================================================================

	/**
	 * List of projects this officer is currently assigned to manage.
	 * 
	 * <p>An officer can be assigned to multiple projects simultaneously,
	 * subject to system limits and manager approval.</p>
	 * 
	 * @see Project
	 */
	private ArrayList<Project> assignedProjects;

	/**
	 * List of applications submitted by this officer to be assigned to projects.
	 * 
	 * <p>Officer applications go through an approval process before
	 * the officer is assigned to the project.</p>
	 * 
	 * @see OfficerApplication
	 */
	private ArrayList<OfficerApplication> officerApplications;

	// ============================================================================
	// STATIC VARIABLES
	// ============================================================================

	/**
	 * Static database reference for Officer persistence.
	 * 
	 * <p>This is separate from the ApplicantDatabase to maintain
	 * distinct collections for officers and regular applicants.</p>
	 */
	private static OfficerDatabase officerDatabase;

	/**
	 * Default constructor that initializes an Officer with empty values.
	 * Creates an empty list of assigned projects.
	 */
	public Officer() {
		super();
		this.assignedProjects = new ArrayList<>();
		this.officerApplications = new ArrayList<>();
	}

	/**
	 * Full constructor that initializes an Officer with all specified values.
	 * Includes parent Applicant fields and officer-specific fields.
	 * 
	 * @param name The name of the officer
	 * @param nric The NRIC (National Registration Identity Card) number
	 * @param age The age of the officer
	 * @param password The account password for authentication
	 * @param maritalStatus The current marital status
	 * @param filter The search preferences for filtering projects
	 * @param applications The BTO applications submitted by this officer as an applicant
	 * @param enquiries The enquiries submitted by this officer
	 * @param assignedProjects The projects this officer is assigned to manage
	 * @param bookings The flat bookings made by this officer as an applicant
	 */
	public Officer(String name, String nric, int age, String password, MarriageStatusEnum maritalStatus, Filter filter, ArrayList<BTOApplication> applications, ArrayList<Enquiry> enquiries, ArrayList<Project> assignedProjects, ArrayList<Booking> bookings) {
		super(name, nric, age, maritalStatus, password, filter, applications, enquiries, bookings);
		this.assignedProjects = assignedProjects != null ? assignedProjects : new ArrayList<>();
	}

	/**
	 * Sets the list of projects this officer is assigned to manage.
	 * 
	 * @param assignedProjects The new list of assigned projects
	 */
	public void setAssignedProjects(ArrayList<Project> assignedProjects) {
		this.assignedProjects = assignedProjects;
	}
	
	/**
	 * Sets the list of officer applications for this officer.
	 * 
	 * <p>Officer applications represent requests by this officer to be
	 * assigned to specific projects. These go through an approval workflow.</p>
	 * 
	 * @param officerApplications the list of officer applications to set
	 * @see #getOfficerApplications()
	 * @see #addOfficerApplication(OfficerApplication)
	 */
	public void setOfficerApplications(ArrayList<OfficerApplication> officerApplications) {
		this.officerApplications = officerApplications != null ? officerApplications : new ArrayList<>();
	}

	/**
	 * Sets the static database reference for Officer persistence.
	 * 
	 * @param database The database instance to use for Officer storage
	 */
	public static void setDatabase(OfficerDatabase database) {
		Officer.officerDatabase = database;
	}

	/**
	 * Gets the list of projects this officer is assigned to manage.
	 * 
	 * @return ArrayList of projects assigned to this officer
	 */
	public ArrayList<Project> getAssignedProjects() {
		return this.assignedProjects;
	}
	/**
	 * Gets the list of officer Applications this officer has
	 * 
	 * @return ArrayList of officer Applications assigned to this officer
	 */
	public ArrayList<OfficerApplication> getOfficerApplications() {
		return this.officerApplications;
	}


	/**
	 * Gets the database used for Officer storage.
	 * This is a static method rather than an override since static methods cannot be overridden.
	 * 
	 * @return The Officer database instance
	 */
	public static OfficerDatabase getOfficerDatabase() {
		return officerDatabase;
	}

	/**
	 * @param application The officer application to add
	 * @return true if the application was added successfully, false if null or already exists
	 */
	public boolean addOfficerApplication(OfficerApplication application) {
		if (application == null) {
			return false;
		}
		this.officerApplications.add(application);
		return true;
	}


	/**
	 * Assigns this officer to manage a specified project.
	 * 
	 * @param project The project to assign to this officer
	 * @return true if the assignment was successful, false if already assigned or project is null
	 */
	public boolean assignToProject(Project project) {
		if (project == null) {
			return false;
		}
		
		if (isAssignedToProject(project)) {
			// Officer is already assigned to this project
			return false;
		}
		this.assignedProjects.add(project);
		
		return true;
	}

	/**
	 * Removes this officer from managing a specified project.
	 * 
	 * @param project The project to unassign from this officer
	 * @return true if the unassignment was successful, false if not assigned or project is null
	 */
	public boolean unassignFromProject(Project project) {
		if (project == null || !isAssignedToProject(project)) {
			return false;
		}
		
		if (project.unassignOfficer(this)) {
			this.assignedProjects.remove(project);
			return true;
		}
		
		return false;
	}

	/**
	 * Checks if this officer is assigned to a specific project.
	 * 
	 * @param project The project to check for assignment
	 * @return true if the officer is assigned to the project, false otherwise
	 */
	public boolean isAssignedToProject(Project project) {
		if (project == null || this.assignedProjects == null) {
			return false;
		}
		
		return this.assignedProjects.contains(project);
	}

	/**
	 * Finds an officer by their name in the database.
	 * 
	 * @param name The name to search for
	 * @return The matching Officer if found, null otherwise
	 */
	public static Officer findOfficerByName(String name) {
		if (officerDatabase == null || name == null) {
			return null;
		}
		
		for (Officer officer : officerDatabase.getOfficers()) {
			if (name.equals(officer.getName())) {
				return officer;
			}
		}
		return null;
	}

	/**
	 * Finds an officer by their NRIC number in the database.
	 * 
	 * @param nric The NRIC number to search for
	 * @return The matching Officer if found, null otherwise
	 */
	public static Officer findOfficerByNRIC(String nric) {
		if (officerDatabase == null || nric == null) {
			return null;
		}
		
		for (Officer officer : officerDatabase.getOfficers()) {
			if (nric.equals(officer.getNric())) {
				return officer;
			}
		}
		return null;
	}

	/**
	 * Finds all officers with a specific age in the database.
	 * 
	 * @param age The age to search for
	 * @return ArrayList of matching Officers, empty list if none found
	 */
	public static ArrayList<Officer> findOfficersByAge(int age) {
		ArrayList<Officer> result = new ArrayList<>();
		if (officerDatabase == null) {
			return result;
		}
		
		for (Officer officer : officerDatabase.getOfficers()) {
			if (officer.getAge() == age) {
				result.add(officer);
			}
		}
		return result;
	}

	/**
	 * Finds the officer who submitted a specific application.
	 * 
	 * @param application The application to find the officer for
	 * @return The matching Officer if found, null otherwise
	 */
	public static Officer findOfficerByApplication(BTOApplication application) {
		if (officerDatabase == null || application == null) {
			return null;
		}
		
		for (Officer officer : officerDatabase.getOfficers()) {
			if (officer.getApplications() instanceof ArrayList) {
				ArrayList<BTOApplication> apps = (ArrayList<BTOApplication>) officer.getApplications();
				if (apps.contains(application)) {
					return officer;
				}
			}
		}
		return null;
	}

	/**
	 * Finds the officer who submitted a specific enquiry.
	 * 
	 * @param enquiry The enquiry to find the officer for
	 * @return The matching Officer if found, null otherwise
	 */
	public static Officer findOfficerByEnquiry(Enquiry enquiry) {
		if (officerDatabase == null || enquiry == null) {
			return null;
		}
		
		for (Officer officer : officerDatabase.getOfficers()) {
			if (officer.getEnquiries() instanceof ArrayList) {
				ArrayList<Enquiry> enqs = (ArrayList<Enquiry>) officer.getEnquiries();
				if (enqs.contains(enquiry)) {
					return officer;
				}
			}
		}
		return null;
	}

	/**
	 * Finds all officers assigned to a specific project.
	 * 
	 * @param project The project to find officers for
	 * @return ArrayList of matching Officers, empty list if none found
	 */
	public static ArrayList<Officer> findOfficersByProject(Project project) {
		ArrayList<Officer> result = new ArrayList<>();
		if (officerDatabase == null || project == null) {
			return result;
		}
		
		for (Officer officer : officerDatabase.getOfficers()) {
			if (officer.isAssignedToProject(project)) {
				result.add(officer);
			}
		}
		return result;
	}

	/**
	 * Finds all officers with a specific marital status.
	 * 
	 * @param status The marital status to search for
	 * @return ArrayList of matching Officers, empty list if none found
	 */
	public static ArrayList<Officer> findOfficerByMaritalStatus(MarriageStatusEnum status) {
		ArrayList<Officer> result = new ArrayList<>();
		if (officerDatabase == null || status == null) {
			return result;
		}
		
		for (Officer officer : officerDatabase.getOfficers()) {
			if (status.equals(officer.getMaritalStatus())) {
				result.add(officer);
			}
		}
		return result;
	}

	/**
	 * Adds an officer to the database.
	 * 
	 * @param officer The officer to add
	 * @return true if the officer was added successfully, false otherwise
	 */
	public static boolean addToDatabase(Officer officer) {
		if (officerDatabase == null || officer == null) {
			return false;
		}
		
		ArrayList<Officer> officers = officerDatabase.getOfficers();
		if (officers.contains(officer)) {
			return false; // Already in database
		}
		
		officers.add(officer);
		return true;
	}

	/**
	 * Removes an officer from the database.
	 * 
	 * @param officer The officer to remove
	 * @return true if the officer was removed successfully, false otherwise
	 */
	public static boolean removeFromDatabase(Officer officer) {
		if (officerDatabase == null || officer == null) {
			return false;
		}
		
		ArrayList<Officer> officers = officerDatabase.getOfficers();
		return officers.remove(officer);
	}

	/**
	 * Retrieves all officers from the database.
	 * 
	 * @return ArrayList of all Officers in the database, empty list if none
	 */
	public static ArrayList<Officer> getAllOfficers() {
		if (officerDatabase == null) {
			return new ArrayList<>();
		}
		return officerDatabase.getOfficers();
	}

	/**
	 * Creates a string representation of this officer.
	 * 
	 * @return A string containing officer details
	 */
	@Override()
	public String toString() {
		StringBuilder projectNames = new StringBuilder();
		if (assignedProjects != null && !assignedProjects.isEmpty()) {
			for (Project project : assignedProjects) {
				if (projectNames.length() > 0) {
					projectNames.append(", ");
				}
				projectNames.append(project.getProjectName());
			}
		} else {
			projectNames.append("None");
		}
		
		return "Officer [name=" + getName() + ", nric=" + getNric() + ", age=" + getAge() 
			+ ", maritalStatus=" + getMaritalStatus() + ", assignedProjects=" + projectNames + "]";
	}
}