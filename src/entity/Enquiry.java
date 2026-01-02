package entity;

import database.*;
import enums.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Entity class representing an enquiry in the BTO Management System.
 * Enquiries are questions or requests for information submitted by applicants
 * regarding specific BTO projects. Enquiries can be responded to by officers
 * or managers, and their status is tracked through the system.
 * 
 * This class provides methods for creating, managing, and searching
 * enquiries in the system database.
 */
public class Enquiry {

	/**
	 * Unique identifier for the enquiry
	 */
	private String enquiryID;
	
	/**
	 * Date and time when the enquiry was submitted
	 */
	private LocalDate dateTime;
	
	/**
	 * Content or message of the enquiry
	 */
	private String content;
	
	/**
	 * Response provided to the enquiry, null if not yet replied
	 */
	private String reply;
	
	/**
	 * Date when the enquiry was responded to, null if not yet replied
	 */
	private LocalDate replyDate;
	
	/**
	 * Reference to the applicant who submitted the enquiry
	 */
	private Applicant submittedBy;
	
	/**
	 * Reference to the project the enquiry is about
	 */
	private Project project;
	
	/**
	 * Current status of the enquiry (PENDING, REPLIED, etc.)
	 */
	private EnquiryStatusEnum status;
	
	/**
	 * Reference to the user who responded to the enquiry, null if not yet replied
	 */
	private User respondent;  // Changed from Officer to User
	
	/**
	 * Static reference to the database of all enquiries in the system
	 */
	private static EnquiryDatabase database;

	/**
	 * Default constructor that initializes a new enquiry with current date,
	 * empty content, PENDING status, and generates a unique enquiry ID.
	 */
	public Enquiry() {
		this.dateTime = LocalDate.now();
		this.content = "";
		this.reply = null;
		this.replyDate = null;
		this.submittedBy = null;
		this.project = null;
		this.status = EnquiryStatusEnum.PENDING;
		this.respondent = null;
		generateEnquiryID();
	}

	/**
	 * Full constructor that initializes an enquiry with all properties.
	 * If some parameters are null, default values are applied.
	 * The constructor also adds the enquiry to the applicant's record if provided.
	 * 
	 * @param enquiryID Unique identifier for the enquiry, generated if null
	 * @param dateTime Date and time when the enquiry was submitted
	 * @param content Content or message of the enquiry
	 * @param reply Response to the enquiry, null if not replied
	 * @param replyDate Date when the enquiry was responded to
	 * @param submittedBy Reference to the applicant who submitted the enquiry
	 * @param project Reference to the project the enquiry is about
	 * @param status Current status of the enquiry
	 * @param respondent Reference to the user who responded to the enquiry
	 */
	public Enquiry(String enquiryID, LocalDate dateTime, String content, String reply, LocalDate replyDate, Applicant submittedBy, Project project, EnquiryStatusEnum status, User respondent) {
		this.enquiryID = enquiryID;
		this.dateTime = dateTime != null ? dateTime : LocalDate.now();
		this.content = content != null ? content : "";
		this.reply = reply;
		this.replyDate = replyDate;
		this.submittedBy = submittedBy;
		this.project = project;
		this.status = status != null ? status : EnquiryStatusEnum.PENDING;
		this.respondent = respondent;
		
		if (this.enquiryID == null || this.enquiryID.isEmpty()) {
			generateEnquiryID();
		}
		
		// Add this enquiry to applicant's enquiries if applicant is not null
		if (submittedBy != null) {
			submittedBy.addEnquiry(this);
		}
	}

	/**
	 * Sets the enquiry ID.
	 * 
	 * @param enquiryID The unique identifier to set for this enquiry
	 */
	public void setEnquiryID(String enquiryID) {
		this.enquiryID = enquiryID;
	}

	/**
	 * Sets the date and time when the enquiry was submitted.
	 * 
	 * @param dateTime The LocalDate representing the submission date
	 */
	public void setDateTime(LocalDate dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * Sets the content of the enquiry.
	 * 
	 * @param content The message or question text of the enquiry
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Sets the reply to the enquiry.
	 * 
	 * @param reply The response text to the enquiry
	 */
	public void setReply(String reply) {
		this.reply = reply;
	}

	/**
	 * Sets the date when the reply was provided.
	 * 
	 * @param replyDate The LocalDate representing when the enquiry was answered
	 */
	public void setReplyDate(LocalDate replyDate) {
		this.replyDate = replyDate;
	}

	/**
	 * Sets the applicant who submitted this enquiry.
	 * 
	 * @param submittedBy The Applicant entity who created the enquiry
	 */
	public void setSubmittedBy(Applicant submittedBy) {
		this.submittedBy = submittedBy;
	}

	/**
	 * Sets the project this enquiry is about.
	 * 
	 * @param project The Project entity the enquiry refers to
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * Sets the status of this enquiry.
	 * 
	 * @param status The EnquiryStatusEnum value representing the current status
	 */
	public void setStatus(EnquiryStatusEnum status) {
		this.status = status;
	}

	/**
	 * Sets the user who responded to this enquiry.
	 * 
	 * @param respondent The User entity who provided a response
	 */
	public void setRespondent(User respondent) {
		this.respondent = respondent;
	}

	/**
	 * Sets the static database reference for all Enquiry objects.
	 * 
	 * @param database The EnquiryDatabase to use for storing enquiries
	 */
	public static void setDatabase(EnquiryDatabase database) {
		Enquiry.database = database;
	}

	/**
	 * Gets the unique ID of this enquiry.
	 * 
	 * @return The enquiry ID string
	 */
	public String getEnquiryID() {
		return this.enquiryID;
	}

	/**
	 * Gets the submission date and time of this enquiry.
	 * 
	 * @return The LocalDate representing when the enquiry was submitted
	 */
	public LocalDate getDateTime() {
		return this.dateTime;
	}

	/**
	 * Gets the content or message of this enquiry.
	 * 
	 * @return The enquiry text
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Gets the reply to this enquiry if one exists.
	 * 
	 * @return The reply text, or null if not yet answered
	 */
	public String getReply() {
		return this.reply;
	}

	/**
	 * Gets the date when this enquiry was responded to.
	 * 
	 * @return The LocalDate when the reply was submitted, or null if not yet answered
	 */
	public LocalDate getReplyDate() {
		return this.replyDate;
	}

	/**
	 * Gets the applicant who submitted this enquiry.
	 * 
	 * @return The Applicant entity who created the enquiry
	 */
	public Applicant getSubmittedBy() {
		return this.submittedBy;
	}

	/**
	 * Gets the project this enquiry is about.
	 * 
	 * @return The Project entity the enquiry refers to
	 */
	public Project getProject() {
		return this.project;
	}

	/**
	 * Gets the current status of this enquiry.
	 * 
	 * @return The EnquiryStatusEnum value representing the status
	 */
	public EnquiryStatusEnum getStatus() {
		return this.status;
	}

	/**
	 * Gets the user who responded to this enquiry.
	 * 
	 * @return The User entity who provided a response, or null if not yet answered
	 */
	public User getRespondent() {
		return this.respondent;
	}

	/**
	 * Gets the static database reference for all Enquiry objects.
	 * 
	 * @return The EnquiryDatabase being used to store enquiries
	 */
	public static EnquiryDatabase getDatabase() {
		return Enquiry.database;
	}

	/**
	 * Records a response to this enquiry.
	 * Updates the reply, respondent, reply date, and status of the enquiry.
	 * 
	 * @param user The User who is providing the response
	 * @param response The text of the response
	 * @return boolean True if the response was successfully recorded, false otherwise
	 */
	public boolean respond(User user, String response) {
		if (user == null || response == null || response.isEmpty() || 
			this.status == EnquiryStatusEnum.REPLIED) {
			return false;
		}
		
		this.respondent = user;
		this.reply = response;
		this.replyDate = LocalDate.now();
		this.status = EnquiryStatusEnum.REPLIED;
		return true;
	}

	/**
	 * Private helper method to generate a unique enquiry ID.
	 * Uses system time and a random number to ensure uniqueness.
	 */
	private void generateEnquiryID() {
		// Generate a unique enquiry ID based on current time
		this.enquiryID = "ENQ-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
	}

	/**
	 * Finds an enquiry by its unique ID.
	 * 
	 * @param enquiryID The ID to search for
	 * @return The matching Enquiry object, or null if not found
	 */
	public static Enquiry findEnquiriesByEnquiryID(String enquiryID) {
		if (database == null || enquiryID == null) {
			return null;
		}
		
		for (Enquiry enquiry : database.getEnquiries()) {
			if (enquiryID.equals(enquiry.getEnquiryID())) {
				return enquiry;
			}
		}
		return null;
	}

	/**
	 * Finds all enquiries submitted on a specific date.
	 * 
	 * @param date The LocalDate to search for
	 * @return ArrayList of Enquiry objects submitted on the specified date
	 */
	public static ArrayList<Enquiry> findEnquiriesBySubmittedDate(LocalDate date) {
		ArrayList<Enquiry> result = new ArrayList<>();
		if (database == null || date == null) {
			return result;
		}
		
		for (Enquiry enquiry : database.getEnquiries()) {
			if (date.equals(enquiry.getDateTime())) {
				result.add(enquiry);
			}
		}
		return result;
	}

	/**
	 * Finds all enquiries submitted by a specific applicant.
	 * 
	 * @param submitter The Applicant entity to search for
	 * @return ArrayList of Enquiry objects submitted by the specified applicant
	 */
	public static ArrayList<Enquiry> findEnquiriesBySubmitter(Applicant submitter) {
		ArrayList<Enquiry> result = new ArrayList<>();
		if (database == null || submitter == null) {
			return result;
		}
		
		for (Enquiry enquiry : database.getEnquiries()) {
			if (submitter.equals(enquiry.getSubmittedBy())) {
				result.add(enquiry);
			}
		}
		return result;
	}

	/**
	 * Finds all enquiries responded to by a specific user.
	 * 
	 * @param respondent The User entity to search for
	 * @return ArrayList of Enquiry objects responded to by the specified user
	 */
	public static ArrayList<Enquiry> findEnquiriesByRespondent(User respondent) {
		ArrayList<Enquiry> result = new ArrayList<>();
		if (database == null || respondent == null) {
			return result;
		}
		
		for (Enquiry enquiry : database.getEnquiries()) {
			if (respondent.equals(enquiry.getRespondent())) {
				result.add(enquiry);
			}
		}
		return result;
	}

	/**
	 * Finds all enquiries replied to on a specific date.
	 * 
	 * @param date The LocalDate to search for
	 * @return ArrayList of Enquiry objects replied to on the specified date
	 */
	public static ArrayList<Enquiry> findEnquiriesByReplyDate(LocalDate date) {
		ArrayList<Enquiry> result = new ArrayList<>();
		if (database == null || date == null) {
			return result;
		}
		
		for (Enquiry enquiry : database.getEnquiries()) {
			if (date.equals(enquiry.getReplyDate())) {
				result.add(enquiry);
			}
		}
		return result;
	}

	/**
	 * Finds all enquiries related to a specific project.
	 * 
	 * @param project The Project entity to search for
	 * @return ArrayList of Enquiry objects related to the specified project
	 */
	public static ArrayList<Enquiry> findEnquiriesByProject(Project project) {
		ArrayList<Enquiry> result = new ArrayList<>();
		if (database == null || project == null) {
			return result;
		}
		
		for (Enquiry enquiry : database.getEnquiries()) {
			if (project.equals(enquiry.getProject())) {
				result.add(enquiry);
			}
		}
		return result;
	}

	/**
	 * Retrieves all enquiries stored in the database.
	 * 
	 * @return ArrayList containing all Enquiry objects in the database
	 */
	public static ArrayList<Enquiry> getAllEnquiries() {
		if (database == null) {
			return new ArrayList<>();
		}
		return database.getEnquiries();
	}

	/**
	 * Adds an enquiry to the database if it doesn't already exist.
	 * 
	 * @param enquiry The Enquiry object to add to the database
	 * @return boolean True if the enquiry was successfully added, false otherwise
	 */
	public static boolean addToDatabase(Enquiry enquiry) {
		if (database == null || enquiry == null) {
			return false;
		}
		
		ArrayList<Enquiry> enquiries = database.getEnquiries();
		if (enquiries.contains(enquiry)) {
			return false; // Already in database
		}
		
		enquiries.add(enquiry);
		return true;
	}

	/**
	 * Finds all enquiries with a specific status.
	 * 
	 * @param status The EnquiryStatusEnum value to search for
	 * @return ArrayList of Enquiry objects with the specified status
	 */
	public static ArrayList<Enquiry> findEnquiriesByStatus(EnquiryStatusEnum status) {
		ArrayList<Enquiry> result = new ArrayList<>();
		if (database == null || status == null) {
			return result;
		}
		
		for (Enquiry enquiry : database.getEnquiries()) {
			if (status.equals(enquiry.getStatus())) {
				result.add(enquiry);
			}
		}
		return result;
	}

	/**
	 * Removes an enquiry from the database.
	 * 
	 * @param enquiry The Enquiry object to remove from the database
	 * @return boolean True if the enquiry was successfully removed, false otherwise
	 */
	public static boolean removeFromDatabase(Enquiry enquiry) {
		if (database == null || enquiry == null) {
			return false;
		}
		
		ArrayList<Enquiry> enquiries = database.getEnquiries();
		return enquiries.remove(enquiry);
	}

	/**
	 * Returns a string representation of this enquiry.
	 * Includes the ID, date, submitter, project, status, and respondent information.
	 * 
	 * @return String representation of the enquiry
	 */
	@Override()
	public String toString() {
		return "Enquiry [ID=" + enquiryID + ", date=" + dateTime + ", submitter=" + (submittedBy != null ? submittedBy.getName() : "None") + ", project=" + (project != null ? project.getProjectName() : "None") + ", status=" + status + ", respondent=" + (respondent != null ? respondent.getName() : "None") + "]";
	}
}