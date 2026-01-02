package controller;

import entity.*;
import enums.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Controller class for managing all officer-related business logic in the BTO Management System.
 * 
 * <p>This controller handles the dual role of officers who can both manage projects
 * as staff members and apply for housing as applicants. It provides comprehensive
 * functionality for project registration, booking processing, enquiry handling,
 * and applicant-like operations.</p>
 * 
 * <h2>Officer Roles:</h2>
 * <ul>
 *   <li><strong>As Staff:</strong> Process bookings, handle enquiries, manage assigned projects</li>
 *   <li><strong>As Applicant:</strong> Apply for BTO flats (except in assigned projects)</li>
 * </ul>
 * 
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li><strong>Project Registration:</strong> Apply to be assigned to projects</li>
 *   <li><strong>Booking Processing:</strong> Confirm pending bookings for assigned projects</li>
 *   <li><strong>Enquiry Handling:</strong> View and respond to project enquiries</li>
 *   <li><strong>Application Management:</strong> Submit BTO applications as an applicant</li>
 *   <li><strong>Receipt Generation:</strong> Generate receipts for confirmed bookings</li>
 * </ul>
 * 
 * <h2>Conflict of Interest Rules:</h2>
 * <ul>
 *   <li>Officers cannot apply for flats in projects they are assigned to</li>
 *   <li>Officers cannot be assigned to projects where they have active applications</li>
 * </ul>
 * 
 * <h2>Design Pattern:</h2>
 * <p>This class follows the Controller pattern from MVC/EBC architecture,
 * encapsulating officer-specific business logic. It also demonstrates the
 * Template Method pattern by sharing eligibility logic with ApplicantController.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * OfficerController controller = new OfficerController();
 * 
 * // Register for a project
 * OfficerApplication app = controller.createOfficerApplication(officer, project);
 * 
 * // Process a booking
 * controller.processBooking(officer, booking);
 * 
 * // Reply to an enquiry
 * controller.replyToEnquiry(officer, enquiry, "Thank you for your question...");
 * }</pre>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see Officer
 * @see OfficerApplication
 * @see Booking
 * @see Enquiry
 */
public class OfficerController {

	/**
	 * Default constructor for the OfficerController.
	 */
	public OfficerController() {
		// Default constructor
	}

	/**
	 * Retrieves all projects with visible status.
	 * 
	 * @return List of visible projects in the system
	 */
	public ArrayList<Project> getAllVisibleProjects() {
		ArrayList<Project> allProjects = Project.getAllProjects();
		ArrayList<Project> visibleProjects = new ArrayList<>();
		
		for (Project project : allProjects) {
			if (project.getVisibility() == VisibilityEnum.VISIBLE) {
				visibleProjects.add(project);
			}
		}
		
		return visibleProjects;
	}

	/**
	 * Retrieves all projects that an officer can view.
	 * Includes visible projects, projects with officer's applications,
	 * and projects the officer is assigned to.
	 * 
	 * @param officer The officer to get viewable projects for
	 * @return List of projects the officer can view
	 */
	public ArrayList<Project> getViewableProjects(Officer officer) {
		ArrayList<Project> viewableProjects = getAllVisibleProjects();
		for (BTOApplication application : officer.getApplications()) {
			if (!viewableProjects.contains(application.getProject())){
				viewableProjects.add(application.getProject());
			}
		}
		for (Project project : officer.getAssignedProjects()) {
			if (!viewableProjects.contains(project)){
				viewableProjects.add(project);
			}
		}

		return viewableProjects;
	}

	/**
	 * Filters eligible projects based on specified criteria.
	 * 
	 * @param officer The officer whose eligible projects should be filtered
	 * @param filter The filter criteria to apply
	 * @return List of projects matching the filter criteria
	 */
	public ArrayList<Project> getFilteredProjects(Officer officer, Filter filter) {
		ArrayList<Project> allProjects = getEligibleProjects(officer);
		ArrayList<Project> filteredProjects = new ArrayList<>();
		
		for (Project project : allProjects) {
			if (project.getVisibility() == VisibilityEnum.VISIBLE && filter.matchesProject(project)) {
				filteredProjects.add(project);
			}
		}
		
		return filteredProjects;
	}

	/**
	 * Retrieves all projects that an officer has applied for as an applicant.
	 * Includes projects with applications in various states.
	 * 
	 * @param officer The officer whose applications to check
	 * @return List of projects the officer has applied for
	 */
	public ArrayList<Project> getAppliedProjects(Officer officer) {
		ArrayList<BTOApplication> applications = officer.getApplications();
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
	 * Retrieves all projects that an officer is assigned to.
	 * 
	 * @param officer The officer to check assignments for
	 * @return List of projects the officer is assigned to
	 */
	public ArrayList<Project> getAssignedProjects(Officer officer) {
	    // No need to create a new ArrayList since the Officer class now directly returns an ArrayList
	    return officer.getAssignedProjects();
	}

	/**
	 * Creates a new application for an officer to be assigned to a project.
	 * Verifies eligibility and availability before creating the application.
	 * 
	 * @param officer The officer applying for project assignment
	 * @param project The project to apply for assignment to
	 * @return The created officer application if successful, null otherwise
	 */
	public OfficerApplication createOfficerApplication(Officer officer, Project project) {
		// Check if officer is eligible to register for the project
		if (!isEligibleForProjectRegistration(officer, project)) {
			return null;
		}
		
		// Check if there are available officer slots
		if (project.getOfficerSlots() <= project.getAssignedOfficers().size()) {
			return null;
		}
		
		// Create a new officer application
		OfficerApplication application = new OfficerApplication();
		application.setOfficer(officer);
		application.setProject(project);
		application.setApplicationDate(LocalDate.now());
		application.setStatus(OfficerApplicationStatusEnum.PENDING);
		
		// Add to database
		officer.addOfficerApplication(application);
		boolean added = OfficerApplication.addToDatabase(application);
		
		if (added) {
			return application;
		}
		
		return null;
	}

	/**
	 * Processes a booking request submitted by an applicant.
	 * Verifies officer authorization, booking status, flat availability,
	 * and updates related entities.
	 * 
	 * @param officer The officer processing the booking
	 * @param booking The booking to process
	 * @return True if the booking was processed successfully, false otherwise
	 */
	public boolean processBooking(Officer officer, Booking booking) {
	    // Check if officer and booking are valid
	    if (officer == null || booking == null) {
	        return false;
	    }
	    
	    // Check if officer is assigned to the project
	    Project project = booking.getApplication().getProject();
	    if (!officer.isAssignedToProject(project)) {
	        return false;
	    }
	    
	    // Check if booking is pending
	    if (booking.getStatus() != BookingStatusEnum.PENDING) {
	        return false;
	    }

		// Check if flatType has enoughh units
		boolean flatTypeCheck = false;
		for (FlatType flatType : project.getFlatTypes()){
			if (booking.getFlatType() == flatType.getType() && flatType.getAvailableUnits() > 0) flatTypeCheck = true;
		}
		if (!flatTypeCheck) return false;

	    // Set processing officer and confirm booking
	    booking.setProcessingOfficer(officer);
	    booking.setStatus(BookingStatusEnum.CONFIRMED);
	    
	    // Update application status to BOOKED
	    BTOApplication application = booking.getApplication();
	    application.setStatus(BTOApplicationStatusEnum.BOOKED);
	    
	    // Update flat type availability
	    FlatType flatType = null;
	    for (FlatType ft : project.getFlatTypes()) {
	        if (ft.getType() == booking.getFlatType()) {
	            flatType = ft;
	            break;
	        }
	    }
	    
	    if (flatType != null) {
	        flatType.decreaseAvailableUnits();
	    }
	    
	    return true;
	}

	/**
	 * Creates a new BTO application for an officer acting as an applicant.
	 * Checks for conflicts of interest, eligibility, and active applications
	 * before creating the application.
	 * 
	 * @param officer The officer applying for a BTO flat
	 * @param project The project being applied for
	 * @param flatType The type of flat being applied for
	 * @return The created application if successful, null otherwise
	 */
	public BTOApplication createApplication(Officer officer, Project project, FlatTypeEnum flatType) {
	    // First check if officer is attached to this project - if so, they cannot apply for it
	    if (officer.isAssignedToProject(project)) {
	        return null; // Cannot apply for a project they're handling
	    }
	    
	    // Then check general eligibility requirements
	    if (!isEligibleForProject(officer, project)) {
	        return null;
	    }
	    
	    // Check if officer has already applied for any project
	    if (hasActiveApplications(officer)) {
	        return null;
	    }
	    
	    // Create a new application
	    BTOApplication application = new BTOApplication(officer, project, flatType);
	    application.setApplicationDate(LocalDate.now());
	    application.setStatus(BTOApplicationStatusEnum.PENDING);
	    application.setWithdrawalStatus(WithdrawalStatusEnum.NA);
	    
	    // Add the application to the database
	    boolean added = BTOApplication.addToDatabase(application);
	    if (added) {
	        return application;
	    }
	    
	    return null;
	}

	/**
	 * Retrieves all applications submitted by an officer acting as an applicant.
	 * 
	 * @param officer The officer whose applications to retrieve
	 * @return List of all applications submitted by the officer
	 */
	public ArrayList<BTOApplication> getApplications(Officer officer) {
		return officer.getApplications();
	}

	/**
	 * Requests withdrawal of an application submitted by an officer.
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
	 * Books a flat for a successful application submitted by an officer.
	 * Verifies eligibility, application status, flat availability,
	 * and updates related entities.
	 * 
	 * @param application The application to book a flat for
	 * @param flatType The type of flat to book
	 * @return True if booking was successful, false otherwise
	 */
	public boolean bookFlat(BTOApplication application, FlatType flatType) {
		// Check if application is successful and eligible for booking
		if (application == null || application.getStatus() != BTOApplicationStatusEnum.SUCCESSFUL) {
			return false;
		}
		
		// Check if flat type is available
		if (flatType == null || flatType.getAvailableUnits() <= 0) {
			return false;
		}
		
		 // Check if officer already has an active booking
		Officer officer = (Officer)application.getApplicant();
		ArrayList<Booking> officerBookings = officer.getBooking();
		if (officerBookings != null) {
			for (Booking existingBooking : officerBookings) {
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
		booking.setFlatType(application.getFlatType());
		booking.setStatus(BookingStatusEnum.PENDING);
		booking.setBookingDateTime(LocalDate.now());
		
		// Add booking to database
		boolean added = Booking.addToDatabase(booking);
		
		if (added) {
			// Add booking to officer's bookings list
			officer.addBooking(booking);
			
			// Update flat type availability
			boolean updated = flatType.decreaseAvailableUnits();
			if (!updated) {
				return false;
			}
			
			// Update application status
			application.setStatus(BTOApplicationStatusEnum.BOOKED);
			
			return true;
		}
		
		return false;
	}

	/**
	 * Submits an enquiry about a project.
	 * Creates and stores a new enquiry with pending status.
	 * 
	 * @param officer The officer submitting the enquiry
	 * @param project The project the enquiry is about
	 * @param content The content of the enquiry
	 * @return The created enquiry if successful, null otherwise
	 */
	public Enquiry submitEnquiry(Officer officer, Project project, String content) {
		if (officer == null || project == null || content == null || content.trim().isEmpty()) {
			return null;
		}
		
		// Create a new enquiry
		Enquiry enquiry = new Enquiry();
		enquiry.setSubmittedBy(officer);
		enquiry.setProject(project);
		enquiry.setContent(content);
		enquiry.setDateTime(LocalDate.now());
		enquiry.setStatus(EnquiryStatusEnum.PENDING);
		
		// Add to database
		boolean added = Enquiry.addToDatabase(enquiry);
		
		if (added) {
			return enquiry;
		}
		
		return null;
	}

	/**
	 * Retrieves all enquiries submitted by an officer.
	 * 
	 * @param officer The officer whose enquiries to retrieve
	 * @return List of enquiries submitted by the officer
	 */
	public ArrayList<Enquiry> getEnquiries(Officer officer) {
		ArrayList<Enquiry> allEnquiries = Enquiry.getAllEnquiries();
		ArrayList<Enquiry> officerEnquiries = new ArrayList<>();
		
		for (Enquiry enquiry : allEnquiries) {
			if (enquiry.getSubmittedBy() != null && 
					enquiry.getSubmittedBy().getNric().equals(officer.getNric())) {
				officerEnquiries.add(enquiry);
			}
		}
		
		return officerEnquiries;
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
	 * Registers an officer for a project.
	 * Delegates to createOfficerApplication method.
	 * 
	 * @param officer The officer to register
	 * @param project The project to register for
	 * @return True if registration was successful, false otherwise
	 */
	public boolean registerAsProjectOfficer(Officer officer, Project project) {
		// Delegate to createOfficerApplication method
		OfficerApplication application = createOfficerApplication(officer, project);
		return application != null;
	}

	/**
	 * Retrieves all BTO applications for a specific project.
	 * 
	 * @param project The project to retrieve applications for
	 * @return List of all applications for the project
	 */
	public ArrayList<BTOApplication> getApplicationsByProject(Project project) {
		ArrayList<BTOApplication> allApplications = BTOApplication.getAllApplications();
		ArrayList<BTOApplication> projectApplications = new ArrayList<>();
		
		for (BTOApplication app : allApplications) {
			if (app.getProject().getProjectName().equals(project.getProjectName())) {
				projectApplications.add(app);
			}
		}
		
		return projectApplications;
	}
	
	/**
	 * Retrieves applications for a specific project with a specific status.
	 * 
	 * @param project The project to retrieve applications for
	 * @param status The status to filter by
	 * @return List of applications matching the project and status
	 */
	public ArrayList<BTOApplication> getApplicationsByProjectAndStatus(Project project, BTOApplicationStatusEnum status) {
		ArrayList<BTOApplication> allApplications = BTOApplication.getAllApplications();
		ArrayList<BTOApplication> filteredApplications = new ArrayList<>();
		
		for (BTOApplication app : allApplications) {
			if (app.getProject().getProjectName().equals(project.getProjectName()) &&
					app.getStatus() == status) {
				filteredApplications.add(app);
			}
		}
		
		return filteredApplications;
	}

	/**
	 * Retrieves all receipts for a specific project.
	 * 
	 * @param project The project to retrieve receipts for
	 * @return List of all receipts for the project
	 */
	public ArrayList<Receipt> getReceiptsByProject(Project project) {
		ArrayList<Receipt> allReceipts = Receipt.getAllReceipts();
		ArrayList<Receipt> projectReceipts = new ArrayList<>();
		
		for (Receipt receipt : allReceipts) {
			if (receipt.getBooking() != null && 
					receipt.getBooking().getApplication() != null &&
					receipt.getBooking().getApplication().getProject().getProjectName().equals(project.getProjectName())) {
				projectReceipts.add(receipt);
			}
		}
		
		return projectReceipts;
	}

	/**
	 * Retrieves all enquiries for a specific project.
	 * 
	 * @param project The project to retrieve enquiries for
	 * @return List of all enquiries for the project
	 */
	public ArrayList<Enquiry> getEnquiriesByProject(Project project) {
		ArrayList<Enquiry> allEnquiries = Enquiry.getAllEnquiries();
		ArrayList<Enquiry> projectEnquiries = new ArrayList<>();
		
		for (Enquiry enquiry : allEnquiries) {
			if (enquiry.getProject() != null && 
					enquiry.getProject().getProjectName().equals(project.getProjectName())) {
				projectEnquiries.add(enquiry);
			}
		}
		
		return projectEnquiries;
	}
	
	/**
	 * Retrieves pending enquiries for a specific project.
	 * 
	 * @param project The project to retrieve pending enquiries for
	 * @return List of pending enquiries for the project
	 */
	public ArrayList<Enquiry> getPendingEnquiriesByProject(Project project) {
		ArrayList<Enquiry> allEnquiries = Enquiry.getAllEnquiries();
		ArrayList<Enquiry> pendingEnquiries = new ArrayList<>();
		
		for (Enquiry enquiry : allEnquiries) {
			if (enquiry.getProject() != null && 
					enquiry.getProject().getProjectName().equals(project.getProjectName()) &&
					enquiry.getStatus() == EnquiryStatusEnum.PENDING) {
				pendingEnquiries.add(enquiry);
			}
		}
		
		return pendingEnquiries;
	}

	/**
	 * Retrieves pending enquiries for projects managed by an officer.
	 * 
	 * @param officer The officer whose projects to check
	 * @return List of pending enquiries for projects managed by the officer
	 */
	public ArrayList<Enquiry> getPendingEnquiriesByOfficerProjects(Officer officer) {
		ArrayList<Enquiry> allEnquiries = Enquiry.getAllEnquiries();
		ArrayList<Enquiry> pendingEnquiries = new ArrayList<>();
		
		ArrayList<Project> assignedProjects = officer.getAssignedProjects();
		if (assignedProjects == null || assignedProjects.isEmpty()) {
			return pendingEnquiries;
		}
		
		for (Enquiry enquiry : allEnquiries) {
			if (enquiry.getProject() != null && 
				enquiry.getStatus() == EnquiryStatusEnum.PENDING) {
				// Check if the enquiry is for any of the officer's assigned projects
				for (Project project : assignedProjects) {
					if (enquiry.getProject().getProjectName().equals(project.getProjectName())) {
						pendingEnquiries.add(enquiry);
						break;
					}
				}
			}
		}
		
		return pendingEnquiries;
	}

	/**
	 * Retrieves all enquiries for projects managed by an officer.
	 * 
	 * @param officer The officer whose projects to check
	 * @return List of all enquiries for projects managed by the officer
	 */
	public ArrayList<Enquiry> getEnquiriesByOfficerProjects(Officer officer) {
		ArrayList<Enquiry> allEnquiries = Enquiry.getAllEnquiries();
		ArrayList<Enquiry> officerProjectEnquiries = new ArrayList<>();
		
		ArrayList<Project> assignedProjects = officer.getAssignedProjects();
		if (assignedProjects == null || assignedProjects.isEmpty()) {
			return officerProjectEnquiries;
		}
		
		for (Enquiry enquiry : allEnquiries) {
			if (enquiry.getProject() != null) {
				// Check if the enquiry is for any of the officer's assigned projects
				for (Project project : assignedProjects) {
					if (enquiry.getProject().getProjectName().equals(project.getProjectName())) {
						officerProjectEnquiries.add(enquiry);
						break;
					}
				}
			}
		}
		
		return officerProjectEnquiries;
	}

	public ArrayList<Booking> findPendingBookingsByProject(Project project) {
		return Booking.findPendingBookingsByProject(project);
	}
	
	/**
	 * Allows an officer to reply to an enquiry.
	 * Verifies that the officer is assigned to the project
	 * and the enquiry is in pending status.
	 * 
	 * @param officer The officer replying to the enquiry
	 * @param enquiry The enquiry to reply to
	 * @param response The response content
	 * @return True if the reply was successful, false otherwise
	 */
	public boolean replyToEnquiry(Officer officer, Enquiry enquiry, String response) {
		// Check if enquiry exists and is pending
		if (enquiry == null || enquiry.getStatus() != EnquiryStatusEnum.PENDING || 
				response == null || response.trim().isEmpty()) {
			return false;
		}
		
		// Check if officer is assigned to the project
		if (!officer.isAssignedToProject(enquiry.getProject())) {
			return false;
		}
		
		// Update enquiry
		enquiry.setReply(response);
		enquiry.setRespondent(officer);
		enquiry.setReplyDate(LocalDate.now());
		enquiry.setStatus(EnquiryStatusEnum.REPLIED);
		
		return true;
	}

	/**
	 * Edits an officer's profile information.
	 * 
	 * @param officer The officer whose profile to edit
	 * @param name The new name
	 * @param nric The new NRIC
	 * @param age The new age
	 * @param maritalStatus The new marital status
	 */
	public void editProfile(Officer officer, String name, String nric, int age, MarriageStatusEnum maritalStatus) {
		if (officer != null) {
			officer.setName(name);
			officer.setNric(nric);
			officer.setAge(age);
			officer.setMaritalStatus(maritalStatus);
		}
	}

	/**
	 * Changes an officer's password.
	 * Verifies the current password and validates the new password.
	 * 
	 * @param officer The officer whose password to change
	 * @param oldPassword The current password for verification
	 * @param newPassword The new password
	 * @return True if password change was successful, false otherwise
	 */
	public boolean changePassword(Officer officer, String oldPassword, String newPassword) {
		if (officer == null || !isValidPassword(newPassword)) {
			return false;
		}
		
		// Verify old password
		if (!officer.authenticate(oldPassword)) {
			return false;
		}
		
		officer.setPassword(newPassword);
		return true;
	}
	
	/**
	 * Verifies an officer's password.
	 * 
	 * @param officer The officer to verify password for
	 * @param password The password to verify
	 * @return True if password is correct, false otherwise
	 */
	public boolean verifyPassword(Officer officer, String password) {
		if (officer == null || password == null) {
			return false;
		}
		
		return officer.authenticate(password);
	}

	/**
	 * Checks if an officer is eligible for a specific project as an applicant.
	 * Verifies application period, conflicts of interest, age requirements,
	 * and eligibility for at least one flat type.
	 * 
	 * @param officer The officer to check eligibility for
	 * @param project The project to check eligibility for
	 * @return True if the officer is eligible, false otherwise
	 */
	public static boolean isEligibleForProject(Officer officer, Project project) {
	    if (officer == null || project == null) {
	        return false;
	    }
	    
	    // Check if application period is open
	    if (!project.isApplicationOpen()) {
	        return false;
	    }
	    
	    // Check if officer is attached to this project - if so, they cannot apply
	    if (officer.isAssignedToProject(project)) {
	        return false;
	    }
	    
	    // Already applied for this project
	    if (hasAppliedForProject(officer, project)) {
	        return true; // Can still view it
	    }
	    
	    // Check age requirements (must be at least 21)
	    if (officer.getAge() < 21) {
	        return false;
	    }

	    // Check if eligible for at least one flat type in the project
	    for (FlatType flatType : project.getFlatTypes()) {
	        if (isEligibleForFlatType(officer, flatType.getType())) {
	            return true; // Eligible for at least one flat type
	        }
	    }
	    
	    return false;
	}

	/**
	 * Checks if an officer has previously applied for a project.
	 * 
	 * @param officer The officer to check
	 * @param project The project to check
	 * @return True if the officer has already applied, false otherwise
	 */
	public static boolean hasAppliedForProject(Officer officer, Project project) {
		if (officer == null || project == null) {
			return false;
		}
		
		ArrayList<BTOApplication> allApplications = BTOApplication.getAllApplications();
		
		for (BTOApplication app : allApplications) {
			if (app.getStatus() != BTOApplicationStatusEnum.WITHDRAWN && app.getStatus() != BTOApplicationStatusEnum.UNSUCCESSFUL){
				if (app.getApplicant().getNric().equals(officer.getNric()) && app.getProject().getProjectName().equals(project.getProjectName())) {
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * Checks if an officer has any active BTO applications.
	 * This includes pending, successful, or booked applications
	 * that are not withdrawn.
	 * 
	 * @param officer The officer to check
	 * @return True if the officer has active applications, false otherwise
	 */
	public static boolean hasActiveApplications(Officer officer) {
		ArrayList<BTOApplication> applications = officer.getApplications();
		
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
	 * Checks if an officer is eligible for a specific flat type.
	 * Applies rules based on marital status and age:
	 * - Singles must be 35+ and can only apply for 2-room flats
	 * - Married applicants must be 21+
	 * 
	 * @param officer The officer to check eligibility for
	 * @param flatType The flat type to check eligibility for
	 * @return True if the officer is eligible, false otherwise
	 */
	public static boolean isEligibleForFlatType(Officer officer, FlatTypeEnum flatType) {
		// Singles must be 35+ and can only apply for 2-room flats
        if (officer.getMaritalStatus() == MarriageStatusEnum.SINGLE) {
            if (officer.getAge() < 35) {
                return false;
            }
            
            if (flatType != FlatTypeEnum.TWO_ROOM) {
                return false;
            }
        }
		else if (officer.getMaritalStatus() == MarriageStatusEnum.MARRIED){
			if (officer.getAge() < 21) {
				return false;
			}
		}
		return true; // Eligible for the flat type
	}

	/**
	 * Validates that a password meets the required criteria.
	 * Currently checks for minimum length of 8 characters.
	 * 
	 * @param password The password to validate
	 * @return True if the password is valid, false otherwise
	 */
	public static boolean isValidPassword(String password) {
		// Basic password validation
		return password != null && password.length() >= 8;
	}


	/**
	 * Generates a receipt for a confirmed booking.
	 * 
	 * @param officer The officer generating the receipt
	 * @param booking The booking to generate a receipt for
	 * @return The generated receipt, or null if generation failed
	 */
	public Receipt generateReceipt(Officer officer, Booking booking) {
	    // Check if officer and booking are valid
	    if (officer == null || booking == null) {
	        return null;
	    }
	    
	    // Check if booking is confirmed
	    if (booking.getStatus() != BookingStatusEnum.CONFIRMED) {
	        return null;
	    }
	    
	    // Create receipt
	    Receipt receipt = new Receipt();
	    receipt.setBooking(booking);
	    receipt.setDate(java.time.LocalDate.now());
	    
	    // Add receipt to database
	    boolean success = Receipt.addToDatabase(receipt);
	    
	    return success ? receipt : null;
	}

	/**
     * Checks if an officer is eligible to register for a project.
     * Verifies the officer is not already assigned and the project has slots.
     * 
     * @param officer The officer to check
     * @param project The project to register for
     * @return true if eligible, false otherwise
     */
    public boolean isEligibleForProjectRegistration(Officer officer, Project project) {
		// Check if officer is already assigned to this project
		if (officer.isAssignedToProject(project)) {
			return false;
		}

		// Check if project has available officer slots
		if (project.getOfficerSlots() <= project.getAssignedOfficers().size()) {
			return false;
		}

		// Add additional eligibility criteria as needed

		return true;
	}

	/**
	 * Retrieves all projects that an officer is eligible to apply for.
	 * This includes visible projects that match eligibility criteria or 
	 * projects the officer has already applied for.
	 * 
	 * @param officer The officer to check eligibility for
	 * @return List of projects the officer is eligible to apply for
	 */
	public ArrayList<Project> getEligibleProjects(Officer officer) {
		ArrayList<Project> allProjects = Project.getAllProjects();
		ArrayList<Project> eligibleProjects = new ArrayList<>();
		
		for (Project project : allProjects) {
			// Check if project is visible or if officer has already applied for it
			if ((project.getVisibility() == VisibilityEnum.VISIBLE && isEligibleForProject(officer, project)) || hasAppliedForProject(officer, project)) {
				eligibleProjects.add(project);
			}
		}
		
		return eligibleProjects;
	}
	
	public ArrayList<Project> getEligibleProjectForRegistration(Officer officer) {
		// Get available projects
		ArrayList<Project> availableProjects = getAllVisibleProjects();
		ArrayList<Project> eligibleProjects = new ArrayList<>();
		
		for (Project project : availableProjects) {
			// Skip projects the officer is already assigned to
			if (officer.isAssignedToProject(project)) {
				continue;
			}
			
			int usedSlots = project.getAssignedOfficers().size();
			int availableSlots = project.getOfficerSlots() - usedSlots;
			
			// Only show projects with available slots
			if (availableSlots > 0 && isEligibleForProjectRegistration(officer, project)) {
				eligibleProjects.add(project);
			}
		}
		return eligibleProjects;
	}
	

}