package controller;

import entity.*;
import enums.*;
import utils.ApplicationConstants;
import utils.ValidationUtils;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Controller class for managing all applicant-related business logic in the BTO Management System.
 * 
 * <p>This controller handles the core operations that applicants can perform, including:
 * BTO application creation and management, flat booking, enquiry submission, and
 * eligibility checking. It serves as the bridge between the applicant views and
 * the underlying entity/database layer.</p>
 * 
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li><strong>Application Management:</strong> Create, retrieve, and withdraw BTO applications</li>
 *   <li><strong>Eligibility Checking:</strong> Verify applicant eligibility for projects and flat types</li>
 *   <li><strong>Flat Booking:</strong> Process flat booking requests for successful applications</li>
 *   <li><strong>Enquiry Handling:</strong> Submit, edit, and delete enquiries about projects</li>
 *   <li><strong>Project Filtering:</strong> Retrieve and filter eligible projects for applicants</li>
 * </ul>
 * 
 * <h2>Eligibility Rules:</h2>
 * <ul>
 *   <li>Married applicants: Must be at least {@value utils.ApplicationConstants#MIN_AGE_MARRIED_APPLICANT} years old</li>
 *   <li>Single applicants: Must be at least {@value utils.ApplicationConstants#MIN_AGE_SINGLE_APPLICANT} years old, can only apply for 2-room flats</li>
 *   <li>Only one active application per applicant at a time</li>
 *   <li>Project must be visible and within application period</li>
 * </ul>
 * 
 * <h2>Design Pattern:</h2>
 * <p>This class follows the Controller pattern from MVC/EBC architecture,
 * encapsulating business logic and coordinating between views and entities.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * ApplicantController controller = new ApplicantController();
 * 
 * // Get eligible projects
 * ArrayList<Project> projects = controller.getEligibleProjects(applicant);
 * 
 * // Create application
 * BTOApplication app = controller.createApplication(applicant, project, FlatTypeEnum.THREE_ROOM);
 * 
 * // Submit enquiry
 * Enquiry enquiry = controller.submitEnquiry(applicant, project, "What is the expected completion date?");
 * }</pre>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see Applicant
 * @see BTOApplication
 * @see Enquiry
 * @see Booking
 */
public class ApplicantController {

	/**
	 * Default constructor for the ApplicantController.
	 */
	public ApplicantController() {
		// Default constructor
	}

	/**
	 * Retrieves all projects that an applicant is eligible to apply for.
	 * This includes visible projects that match eligibility criteria or 
	 * projects the applicant has already applied for.
	 * 
	 * @param applicant The applicant to check eligibility for
	 * @return List of projects the applicant is eligible to apply for
	 */
	public ArrayList<Project> getEligibleProjects(Applicant applicant) {
		ArrayList<Project> allProjects = Project.getAllProjects();
		ArrayList<Project> eligibleProjects = new ArrayList<>();
		
		for (Project project : allProjects) {
			// Check if project is visible or if applicant has already applied for it
			if ((project.getVisibility() == VisibilityEnum.VISIBLE && isEligibleForProject(applicant, project)) || hasAppliedForProject(applicant, project)) {
				eligibleProjects.add(project);
			}
		}
		
		return eligibleProjects;
	}

	/**
	 * Filters eligible projects based on specified criteria.
	 * 
	 * @param applicant The applicant whose eligible projects should be filtered
	 * @param filter The filter criteria to apply
	 * @return List of projects matching the filter criteria
	 */
	public ArrayList<Project> getFilteredProjects(Applicant applicant, Filter filter) {
		ArrayList<Project> allProjects = getEligibleProjects(applicant);
		ArrayList<Project> filteredProjects = new ArrayList<>();
		
		for (Project project : allProjects) {
			if (project.getVisibility() == VisibilityEnum.VISIBLE && filter.matchesProject(project)) {
				filteredProjects.add(project);
			}
		}
		
		return filteredProjects;
	}

	/**
	 * Retrieves all projects that an applicant has applied for.
	 * Includes projects with applications in various states including
	 * pending, successful, unsuccessful, and booked.
	 * 
	 * @param applicant The applicant whose applications to retrieve
	 * @return List of projects the applicant has applied for
	 */
	public ArrayList<Project> getAppliedProjects(Applicant applicant) {
		ArrayList<BTOApplication> applications = getApplications(applicant);
		ArrayList<Project> appliedProjects = new ArrayList<>();
		
			// Able to view the projects they have applied for after visibility is off for status of Pending, Successful, Unsuccessful, Booked
		for (BTOApplication app : applications) {
			if (app.getStatus() == BTOApplicationStatusEnum.PENDING || 
				app.getStatus() == BTOApplicationStatusEnum.SUCCESSFUL ||
				app.getStatus() == BTOApplicationStatusEnum.UNSUCCESSFUL ||
				app.getStatus() == BTOApplicationStatusEnum.BOOKED) {
				appliedProjects.add(app.getProject());
			}
		}
		
		return appliedProjects;
	}

	/**
	 * Creates a new BTO application for an applicant.
	 * Checks eligibility, ensures the applicant has no active applications,
	 * and creates a pending application for the specified project and flat type.
	 * 
	 * @param applicant The applicant applying for a BTO flat
	 * @param project The project being applied for
	 * @param flatType The type of flat being applied for
	 * @return The created application if successful, null otherwise
	 */
	public BTOApplication createApplication(Applicant applicant, Project project, FlatTypeEnum flatType) {
		// Check if applicant is eligible for the project and flat type
		if (!isEligibleForProject(applicant, project)) {
			return null;
		}
		
		// Check if applicant has already applied for any project
		if (hasActiveApplications(applicant)) {
			return null;
		}
		
		// Create a new application
		BTOApplication application = new BTOApplication(applicant, project, flatType);
		application.setApplicationDate(LocalDate.now());
		application.setStatus(BTOApplicationStatusEnum.PENDING);
		application.setWithdrawalStatus(WithdrawalStatusEnum.NA);
		
		// Add the application to the database and to the applicant's list
		boolean added = BTOApplication.addToDatabase(application);
		if (added) {
			applicant.addApplication(application);
			return application;
		}
		
		return null;
	}

	/**
	 * Retrieves all applications submitted by an applicant.
	 * 
	 * @param applicant The applicant whose applications to retrieve
	 * @return List of all applications submitted by the applicant
	 */
	public ArrayList<BTOApplication> getApplications(Applicant applicant) {
		ArrayList<BTOApplication> allApplications = BTOApplication.getAllApplications();
		ArrayList<BTOApplication> applicantApplications = new ArrayList<>();
		
		for (BTOApplication app : allApplications) {
			if (app.getApplicant().getNric().equals(applicant.getNric())) {
				applicantApplications.add(app);
			}
		}
		
		return applicantApplications;
	}

	/**
	 * Requests withdrawal of an application.
	 * Changes the application's withdrawal status to pending.
	 * 
	 * @param application The application to withdraw
	 * @return True if withdrawal request was successful, false otherwise
	 */
	public boolean requestWithdrawal(BTOApplication application) {
		// Check if application exists and has no existing withdrawal request
		if (application == null || application.getWithdrawalStatus() != WithdrawalStatusEnum.NA) {
			return false;
		}
		
		// Update withdrawal status to pending
		application.setWithdrawalStatus(WithdrawalStatusEnum.PENDING);
		return true;
	}

	/**
	 * Books a flat for a successful application.
	 * Verifies eligibility, creates a booking, and updates related entities.
	 * 
	 * @param application The application to book a flat for
	 * @param flatType The type of flat to book
	 * @return True if booking was successful, false otherwise
	 */
	public boolean bookFlat(BTOApplication application, FlatType flatType) {
		// Check if application is valid for booking
		if (application == null || flatType == null) {
			return false;
		}
		
		// Check application status
		if (application.getStatus() != BTOApplicationStatusEnum.SUCCESSFUL || 
				application.getWithdrawalStatus() != WithdrawalStatusEnum.NA) {
			return false;
		}
		
		// Check if applicant is eligible for this flat type
        Applicant applicant = application.getApplicant();
        if (!isEligibleForFlatType(applicant, flatType.getType())) {
            return false;
        }
		
		// Check if applicant already has an active booking
		ArrayList<Booking> applicantBookings = applicant.getBooking();
		if (applicantBookings != null) {
			for (Booking existingBooking : applicantBookings) {
				if (existingBooking.getStatus() == BookingStatusEnum.PENDING || 
					existingBooking.getStatus() == BookingStatusEnum.CONFIRMED) {
					return false; // Active booking exists
				}
			}
		}

		Project project = application.getProject();
		// Check if available flatType
		for (FlatType flatType2 : project.getFlatTypes()){
			if (flatType2.getType() == flatType.getType() && flatType2.getAvailableUnits() <=0) return false;
		}
		
		// Create a new booking with pending status
		Booking booking = new Booking();
		booking.setApplication(application);
		booking.setFlatType(flatType.getType());
		booking.setStatus(BookingStatusEnum.PENDING);
		booking.setBookingDateTime(java.time.LocalDate.now());
		
		// Add booking to database
		boolean success = Booking.addToDatabase(booking);
		
		if (success) {
			// Add booking to applicant's bookings list
			applicant.addBooking(booking);
		}
		
		return success;
	}

	/**
	 * Submits an enquiry about a project.
	 * Creates and stores a new enquiry with pending status.
	 * 
	 * @param applicant The applicant submitting the enquiry
	 * @param project The project the enquiry is about
	 * @param content The content of the enquiry
	 * @return The created enquiry if successful, null otherwise
	 */
	public Enquiry submitEnquiry(Applicant applicant, Project project, String content) {
		if (applicant == null || project == null || content == null || content.trim().isEmpty()) {
			return null;
		}
		
		// Create a new enquiry
		Enquiry enquiry = new Enquiry();
		enquiry.setSubmittedBy(applicant);
		enquiry.setProject(project);
		enquiry.setContent(content);
		enquiry.setDateTime(LocalDate.now());
		enquiry.setStatus(EnquiryStatusEnum.PENDING);
		
		// Add to database and applicant's list
		boolean added = Enquiry.addToDatabase(enquiry);
		if (added) {
			applicant.addEnquiry(enquiry);
			return enquiry;
		}
		
		return null;
	}

	/**
	 * Retrieves all enquiries submitted by an applicant.
	 * 
	 * @param applicant The applicant whose enquiries to retrieve
	 * @return List of enquiries submitted by the applicant
	 */
	public ArrayList<Enquiry> getEnquiries(Applicant applicant) {
		ArrayList<Enquiry> allEnquiries = Enquiry.getAllEnquiries();
		ArrayList<Enquiry> applicantEnquiries = new ArrayList<>();
		
		for (Enquiry enquiry : allEnquiries) {
			if (enquiry.getSubmittedBy() != null && 
					enquiry.getSubmittedBy().getNric().equals(applicant.getNric())) {
				applicantEnquiries.add(enquiry);
			}
		}
		
		return applicantEnquiries;
	}

	/**
	 * Edits the content of a pending enquiry.
	 * Only allows editing of enquiries with PENDING status.
	 * 
	 * @param enquiry The enquiry to edit
	 * @param newContent The new content for the enquiry
	 * @return True if edit was successful, false otherwise
	 */
	public boolean editEnquiry(Enquiry enquiry, String newContent) {
		// Can only edit pending enquiries
		if (enquiry == null || enquiry.getStatus() != EnquiryStatusEnum.PENDING || 
				newContent == null || newContent.trim().isEmpty()) {
			return false;
		}
		
		enquiry.setContent(newContent);
		return true;
	}

	/**
	 * Deletes an enquiry from the system.
	 * 
	 * @param enquiry The enquiry to delete
	 * @return True if deletion was successful, false otherwise
	 */
	public boolean deleteEnquiry(Enquiry enquiry) {
		if (enquiry == null) {
			return false;
		}
		
		return Enquiry.removeFromDatabase(enquiry);
	}

	/**
	 * Changes an applicant's password.
	 * Validates the new password meets requirements.
	 * 
	 * @param applicant The applicant whose password to change
	 * @param password The new password
	 * @return True if password change was successful, false otherwise
	 */
	public boolean changePassword(Applicant applicant, String password) {
		if (applicant == null || !isValidPassword(password)) {
			return false;
		}
		
		applicant.setPassword(password);
		return true;
	}

	/**
	 * Checks if an applicant is eligible for a specific project.
	 * 
	 * <p>This method verifies multiple eligibility criteria:</p>
	 * <ol>
	 *   <li>Project must be visible to applicants</li>
	 *   <li>Project application period must be open</li>
	 *   <li>Applicant must meet minimum age requirement ({@value utils.ApplicationConstants#MIN_AGE_MARRIED_APPLICANT} years)</li>
	 *   <li>Applicant must be eligible for at least one flat type in the project</li>
	 * </ol>
	 * 
	 * @param applicant the applicant to check eligibility for
	 * @param project   the project to check eligibility for
	 * @return {@code true} if the applicant is eligible, {@code false} otherwise
	 * @see #isEligibleForFlatType(Applicant, FlatTypeEnum)
	 */
	public static boolean isEligibleForProject(Applicant applicant, Project project) {
		// Check if project is visible
        if (project.getVisibility() != VisibilityEnum.VISIBLE) {
            return false;
        }
        
        // Check if application period is open
        if (!project.isApplicationOpen()) {
            return false;
        }
        
        // Age check - minimum age for any application is 21
        if (applicant.getAge() < ApplicationConstants.MIN_AGE_MARRIED_APPLICANT) {
            return false;
        }

		// Check if applicant is eligible for at least one flat type in the project
		for (FlatType flatType : project.getFlatTypes()){
			if(isEligibleForFlatType(applicant, flatType.getType())){
				return true;
			}
		}
        
        return false;
	}

	/**
	 * Checks if an applicant has previously applied for a project.
	 * 
	 * @param applicant The applicant to check
	 * @param project The project to check
	 * @return True if the applicant has already applied, false otherwise
	 */
	public static boolean hasAppliedForProject(Applicant applicant, Project project) {
		if (applicant == null || project == null) {
			return false;
		}
		
		ArrayList<BTOApplication> allApplications = BTOApplication.getAllApplications();
		
		for (BTOApplication app : allApplications) {
			if (app.getStatus() != BTOApplicationStatusEnum.WITHDRAWN && app.getStatus() != BTOApplicationStatusEnum.UNSUCCESSFUL){
				if (app.getApplicant().getNric().equals(applicant.getNric()) && app.getProject().getProjectName().equals(project.getProjectName())) {
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * Checks if an applicant has any active applications.
	 * This includes pending, successful, or booked applications
	 * that are not withdrawn.
	 * 
	 * @param applicant The applicant to check
	 * @return True if the applicant has active applications, false otherwise
	 */
	public boolean hasActiveApplications(Applicant applicant) {
		ArrayList<BTOApplication> applications = applicant.getApplications();
        for (BTOApplication app : applications) {
            // Consider an application active if it's pending or successful and not withdrawn
            if ((app.getStatus() == BTOApplicationStatusEnum.PENDING || 
                app.getStatus() == BTOApplicationStatusEnum.SUCCESSFUL||
				app.getStatus() == BTOApplicationStatusEnum.BOOKED) && 
                (app.getWithdrawalStatus() == WithdrawalStatusEnum.NA||
				app.getWithdrawalStatus() == WithdrawalStatusEnum.PENDING ||
				app.getWithdrawalStatus() == WithdrawalStatusEnum.REJECTED)) {
                return true;
            }
        }
        
        return false;
	}

	/**
	 * Checks if an applicant is eligible for a specific flat type.
	 * 
	 * <p>Eligibility rules based on marital status and age:</p>
	 * <ul>
	 *   <li><strong>Singles:</strong> Must be at least {@value utils.ApplicationConstants#MIN_AGE_SINGLE_APPLICANT}
	 *       years old and can only apply for 2-room flats</li>
	 *   <li><strong>Married:</strong> Must be at least {@value utils.ApplicationConstants#MIN_AGE_MARRIED_APPLICANT}
	 *       years old, can apply for any flat type</li>
	 * </ul>
	 * 
	 * @param applicant the applicant to check eligibility for
	 * @param flatType  the flat type to check eligibility for
	 * @return {@code true} if the applicant is eligible, {@code false} otherwise
	 * @see #isEligibleForProject(Applicant, Project)
	 */
	public static boolean isEligibleForFlatType(Applicant applicant, FlatTypeEnum flatType) {
		// Singles must be 35+ and can only apply for 2-room flats
        if (applicant.getMaritalStatus() == MarriageStatusEnum.SINGLE) {
            if (applicant.getAge() < ApplicationConstants.MIN_AGE_SINGLE_APPLICANT) {
                return false;
            }
            
            if (flatType != FlatTypeEnum.TWO_ROOM) {
                return false;
            }
        }
		else if (applicant.getMaritalStatus() == MarriageStatusEnum.MARRIED){
			if (applicant.getAge() < ApplicationConstants.MIN_AGE_MARRIED_APPLICANT) {
				return false;
			}
		}
        
        return true; // Eligible for flat type
	}

	/**
	 * Validates that a password meets the system's requirements.
	 * 
	 * <p>This method delegates to {@link ValidationUtils#isValidPassword(String)}
	 * to ensure consistent password validation across the application.</p>
	 * 
	 * <p>Current requirements:</p>
	 * <ul>
	 *   <li>Minimum length of {@value utils.ApplicationConstants#MIN_PASSWORD_LENGTH} characters</li>
	 *   <li>Not null or empty</li>
	 * </ul>
	 * 
	 * @param password the password to validate
	 * @return {@code true} if the password is valid, {@code false} otherwise
	 * @see ValidationUtils#isValidPassword(String)
	 * @see ValidationUtils#validatePasswordStrength(String)
	 */
	public static boolean isValidPassword(String password) {
		return ValidationUtils.isValidPassword(password);
	}
}