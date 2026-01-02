package controller;

import entity.*;
import enums.*;
import utils.ValidationUtils;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Controller class for Manager-specific operations in the BTO Management System.
 * 
 * <p>This controller handles all manager-related business logic including project
 * management, application processing, officer assignments, enquiry handling,
 * and various reporting functions that managers are responsible for.</p>
 * 
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li><strong>Project Management:</strong> Create, edit, delete, and manage project visibility</li>
 *   <li><strong>Application Processing:</strong> Approve or reject BTO applications and withdrawals</li>
 *   <li><strong>Officer Management:</strong> Process officer assignment applications</li>
 *   <li><strong>Enquiry Handling:</strong> View and respond to project enquiries</li>
 *   <li><strong>Reporting:</strong> Generate applicant reports with filtering capabilities</li>
 * </ul>
 * 
 * <h2>Project Creation Rules:</h2>
 * <ul>
 *   <li>Managers cannot manage overlapping projects (same application period)</li>
 *   <li>Project names must be unique within the system</li>
 *   <li>Application dates must be valid (start before end)</li>
 * </ul>
 * 
 * <h2>Design Pattern:</h2>
 * <p>This class follows the Controller pattern from MVC/EBC architecture,
 * encapsulating manager-specific business logic and coordinating between
 * views and entities.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * ManagerController controller = new ManagerController();
 * 
 * // Create a new project
 * Project project = controller.createProject(manager, "New Project", "Tampines",
 *     LocalDate.of(2026, 1, 1), LocalDate.of(2026, 3, 31), 5, VisibilityEnum.VISIBLE);
 * 
 * // Process an application
 * controller.processApplication(application, true); // Approve
 * 
 * // Generate report
 * ArrayList<Applicant> report = controller.generateApplicantReport();
 * }</pre>
 * 
 * @author BTO Management System Team
 * @version 2.0
 * @since 1.0
 * @see Manager
 * @see Project
 * @see BTOApplication
 * @see OfficerApplication
 */
public class ManagerController {

	/**
	 * Default constructor for the manager controller.
	 */
	public ManagerController() {
		// Default constructor
	}

	/**
	 * Retrieves all projects in the system regardless of visibility status.
	 * Managers have full access to view all projects, including hidden ones.
	 * 
	 * @return Complete list of all projects in the database
	 */
	public ArrayList<Project> getAllProjects() {
		// Manager can see all projects regardless of visibility
		return Project.getAllProjects();
	}

	/**
	 * Retrieves projects that match the specified filter criteria.
	 * Allows managers to search for projects based on various attributes.
	 * 
	 * @param filter The search criteria to apply when filtering projects
	 * @return List of projects that match the filter criteria
	 */
	public ArrayList<Project> getFilteredProjects(Filter filter) {
		ArrayList<Project> allProjects = Project.getAllProjects();
		ArrayList<Project> filteredProjects = new ArrayList<>();
		
		for (Project project : allProjects) {
			if (filter.matchesProject(project)) {
				filteredProjects.add(project);
			}
		}
		
		return filteredProjects;
	}

	/**
	 * Retrieves all projects managed by a specific manager.
	 * Used to show managers only the projects they're responsible for.
	 * 
	 * @param manager The manager whose projects should be retrieved
	 * @return List of projects assigned to the specified manager
	 */
	public ArrayList<Project> getManagedProjects(Manager manager) {
		if (manager == null) {
			return new ArrayList<>();
		}
		
		return manager.getManagedProjects();
	}
	
	/**
	 * Updates an existing project with new information.
	 * Allows managers to edit project details as needed.
	 * 
	 * @param project The project to be modified
	 * @param projectName Updated name for the project
	 * @param neighborhood Updated neighborhood location
	 * @param applicationStartDate Updated application start date
	 * @param applicationEndDate Updated application end date
	 * @param flatTypes Updated list of flat types available in the project
	 * @return true if the update was successful, false otherwise
	 */
	public boolean editProject(Project project, String projectName, String neighborhood, LocalDate applicationStartDate, LocalDate applicationEndDate, ArrayList<FlatType> flatTypes) {
		if (project == null) {
			return false;
		}
		
		project.setProjectName(projectName);
		project.setNeighborhood(neighborhood);
		project.setApplicationStartDate(applicationStartDate);
		project.setApplicationEndDate(applicationEndDate);
		project.setFlatTypes(flatTypes);
		
		return true;
	}

	/**
	 * Removes a project from the system.
	 * Should only be used for projects with no active applications.
	 * 
	 * @param project The project to be deleted
	 * @return true if deletion was successful, false otherwise
	 */
	public boolean deleteProject(Project project) {
		if (project == null) {
			return false;
		}
		
		return Project.removeFromDatabase(project);
	}

	/**
	 * Changes the visibility state of a project.
	 * Controls whether the project appears in applicant search results.
	 * 
	 * @param project The project whose visibility should be modified
	 * @param visibility The new visibility state to set
	 * @return true if update was successful, false otherwise
	 */
	public boolean updateProjectVisibility(Project project, VisibilityEnum visibility) {
		if (project == null || visibility == null) {
			return false;
		}
		
		project.setVisibility(visibility);
		return true;
	}

	/**
	 * Creates a new housing project in the system.
	 * Validates project parameters and manager availability before creation.
	 * 
	 * @param manager The manager who will oversee this project
	 * @param projectName Name of the new project
	 * @param neighborhood Location/area of the project
	 * @param applicationStartDate Date when applications will begin
	 * @param applicationEndDate Date when applications will close
	 * @param officerSlots Number of officers needed for this project
	 * @param visibility Initial visibility status for the project
	 * @return The newly created Project object, or null if creation failed
	 */
	public Project createProject(Manager manager, String projectName, String neighborhood, LocalDate applicationStartDate, LocalDate applicationEndDate, int officerSlots, VisibilityEnum visibility) {
		// Basic validation
		if (manager == null || projectName == null || neighborhood == null || 
				applicationStartDate == null || applicationEndDate == null) {
			return null;
		}
		
		// First check if the manager can manage this project (no date conflicts)
		if (!canManageProject(manager, applicationStartDate, applicationEndDate)) {
			System.out.println("Cannot create project: Manager is already handling a project in the same application period.");
			return null;
		}
		
		// Create project
		Project project = new Project();
		project.setProjectName(projectName);
		project.setNeighborhood(neighborhood);
		project.setApplicationStartDate(applicationStartDate);
		project.setApplicationEndDate(applicationEndDate);
		project.setOfficerSlots(officerSlots);
		project.setVisibility(visibility);
		
		// Add to database
		boolean added = Project.addToDatabase(project);
		
		if (added) {
			// Set the manager and add project to manager's list
			project.setManager(manager);
			manager.addProject(project);
			return project;
		}
		
		return null;
	}

	/**
	 * Checks if a manager can take on a new project with the given dates.
	 * Prevents managers from being overloaded with overlapping projects.
	 * 
	 * @param manager The manager to check availability for
	 * @param startDate The new project's application start date
	 * @param endDate The new project's application end date
	 * @return true if the manager can handle this project, false if there's a conflict
	 */
	private boolean canManageProject(Manager manager, LocalDate startDate, LocalDate endDate) {
		ArrayList<Project> managedProjects = getManagedProjects(manager);
		
		// Check for date conflicts
		for (Project managedProject : managedProjects) {
			// Check if application periods overlap
			LocalDate existingStart = managedProject.getApplicationStartDate();
			LocalDate existingEnd = managedProject.getApplicationEndDate();
			
			// Check for overlap
			if ((startDate.isBefore(existingEnd) || startDate.isEqual(existingEnd)) && 
				(endDate.isAfter(existingStart) || endDate.isEqual(existingStart))) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Retrieves applications for a specific project.
	 * Helps managers focus on applications for projects they oversee.
	 * 
	 * @param project The project to get applications for
	 * @return List of BTO applications for the specified project
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
	 * Processes a BTO application approval or rejection.
	 * Managers use this to determine which applicants qualify for flats.
	 * 
	 * @param application The application to process
	 * @param approve Whether to approve or reject the application
	 * @return true if processing was successful, false otherwise
	 */
	public boolean processApplication(BTOApplication application, boolean approve) {
		if (application == null) {
			return false;
		}
		
		// Check if application is pending
		if (application.getStatus() != BTOApplicationStatusEnum.PENDING) {
			return false;
		}
		
		// Update status
		if (approve) {
			application.setStatus(BTOApplicationStatusEnum.SUCCESSFUL);
		} else {
			application.setStatus(BTOApplicationStatusEnum.UNSUCCESSFUL);
		}
		
		return true;
	}

	/**
	 * Processes a withdrawal request from an applicant.
	 * Allows managers to approve or reject requested withdrawals.
	 * 
	 * @param application The application with a withdrawal request
	 * @param approved Whether to approve or reject the withdrawal
	 * @return true if processing was successful, false otherwise
	 */
	public boolean processWithdrawal(BTOApplication application, boolean approved) {
		if (application == null) {
			return false;
		}
		
		// Check if application has a pending withdrawal request
		if (application.getWithdrawalStatus() != WithdrawalStatusEnum.PENDING) {
			return false;
		}
		
		// Update withdrawal status
		if (approved) {
			application.setWithdrawalStatus(WithdrawalStatusEnum.APPROVED);
			
			// If the application is booked, increase available units
			if (application.getStatus() == BTOApplicationStatusEnum.BOOKED) {
				// Find the flat type in the project
				for (FlatType flatType : application.getProject().getFlatTypes()) {
					if (flatType.getType() == application.getFlatType()) {
						flatType.increaseAvailableUnits();
						break;
					}
				}

				// Update application status to withdrawn
				application.setStatus(BTOApplicationStatusEnum.WITHDRAWN);
			}
		} else {
			application.setWithdrawalStatus(WithdrawalStatusEnum.REJECTED);
		}
		
		return true;
	}

	/**
	 * Retrieves officer applications for a specific project.
	 * Shows which officers have applied to work on a particular project.
	 * 
	 * @param project The project to get officer applications for
	 * @return List of officer applications for the specified project
	 */
	public ArrayList<OfficerApplication> getOfficerApplicationsByProject(Project project) {
		ArrayList<OfficerApplication> allApplications = OfficerApplication.getAllApplications();
		ArrayList<OfficerApplication> projectApplications = new ArrayList<>();
		
		for (OfficerApplication app : allApplications) {
			if (app.getProject().getProjectName().equals(project.getProjectName())) {
				projectApplications.add(app);
			}
		}
		
		return projectApplications;
	}

	/**
	 * Processes an officer's application to work on a project.
	 * Managers use this to build their project teams.
	 * 
	 * @param application The officer application to process
	 * @param approved Whether to approve or reject the application
	 * @return true if processing was successful, false otherwise
	 */
	public boolean processOfficerApplication(OfficerApplication application, boolean approved) {
	    // Check if application is pending
	    if (application.getStatus() != OfficerApplicationStatusEnum.PENDING) {
	        return false;
	    }
	    
	    // Check if project has available officer slots
	    Project project = application.getProject();
	    if (approved && project.getOfficerSlots() <= project.getAssignedOfficers().size()) {
	        return false;
	    }
	    
	    Officer officer = application.getOfficer();
	    
	    if (approved) {
	        // Set status to approved
	        application.setStatus(OfficerApplicationStatusEnum.APPROVED);
	        
	        // Check if officer is already assigned to this project
	        if (officer.isAssignedToProject(project)) {
	            application.setStatus(OfficerApplicationStatusEnum.PENDING);
	            return false;
	        }
	        
	        // Assign officer to project
	        boolean assigned = officer.assignToProject(project);
	        if (!assigned) {
	            // If assignment fails, revert status
	            application.setStatus(OfficerApplicationStatusEnum.PENDING);
	            return false;
	        }
	    } else {
	        // Set status to rejected
	        application.setStatus(OfficerApplicationStatusEnum.REJECTED);
	    }
	    
	    return true;
	}

	public ArrayList<Applicant> generateApplicantReport() {
		ArrayList<Applicant> applicants = Applicant.getAllApplicants();
		ArrayList<Officer> allOfficers = Officer.getAllOfficers();
		
		// We need to check if each officer already exists in the applicant list
		// to avoid duplicates (as Officer extends Applicant)
		for (Officer officer : allOfficers) {
			// Only add officers who have actually applied for BTO projects (as applicants)
			// Check if this officer is already in the applicants list
			boolean exists = false;
			for (Applicant applicant : applicants) {
				if (applicant.getNric().equals(officer.getNric())) {
					exists = true;
					break;
				}
			}
			
			// Add officer to the report if not already included
			if (!exists) {
				applicants.add(officer);
			}
		}
		
		return applicants;
	}

	/**
	 * 
	 * @param filter
	 */
	public ArrayList<Applicant> generateApplicantReportWithFilter(BookingFilter filter) {
		// Get all applicants including officers who applied as applicants
		ArrayList<Applicant> allApplicants = generateApplicantReport();
		ArrayList<Applicant> filteredApplicants = new ArrayList<>();
		
		for (Applicant applicant : allApplicants) {
			boolean matchesFilter = true;
			
			// Filter by age if specified
			if (filter.getAge() != null && !filter.getAge().isEmpty()) {
				boolean ageMatches = false;
				for (Integer age : filter.getAge()) {
					if (applicant.getAge() == age) {
						ageMatches = true;
						break;
					}
				}
				if (!ageMatches) {
					matchesFilter = false;
				}
			}
			
			// Filter by marital status if specified
			if (matchesFilter && filter.getMaritalStatus() != null && !filter.getMaritalStatus().isEmpty()) {
				boolean statusMatches = false;
				for (MarriageStatusEnum status : filter.getMaritalStatus()) {
					if (applicant.getMaritalStatus() == status) {
						statusMatches = true;
						break;
					}
				}
				if (!statusMatches) {
					matchesFilter = false;
				}
			}
			
			// Check application details if needed for project name and flat type filters
			if (matchesFilter && (filter.getProjectName() != null || filter.getFlatType() != null) && 
				applicant.getApplications() != null && !applicant.getApplications().isEmpty()) {
				boolean applicationMatches = false;
				
				for (BTOApplication application : applicant.getApplications()) {
					boolean currentAppMatches = true;
					
					// Filter by project name if specified
					if (filter.getProjectName() != null && !filter.getProjectName().isEmpty()) {
						if (application.getProject() == null || 
							!application.getProject().getProjectName().equals(filter.getProjectName())) {
							currentAppMatches = false;
						}
					}
					
					// Filter by flat type if specified
					if (currentAppMatches && filter.getFlatType() != null && !filter.getFlatType().isEmpty()) {
						boolean flatTypeMatches = false;
						for (FlatTypeEnum flatType : filter.getFlatType()) {
							if (application.getFlatType() == flatType) {
								flatTypeMatches = true;
								break;
							}
						}
						if (!flatTypeMatches) {
							currentAppMatches = false;
						}
					}
					
					if (currentAppMatches) {
						applicationMatches = true;
						break;
					}
				}
				
				if (!applicationMatches) {
					matchesFilter = false;
				}
			}
			
			// Add to filtered list if all criteria match
			if (matchesFilter) {
				filteredApplicants.add(applicant);
			}
		}
		
		return filteredApplicants;
	}

	/**
	 * 
	 * @param manager
	 */
	public ArrayList<Enquiry> getPendingEnquiriesByManagedProjects(Manager manager) {
		ArrayList<Enquiry> allEnquiries = getEnquiriesByManagedProjects(manager);
		ArrayList<Enquiry> pendingEnquiries = new ArrayList<>();
		
		// Get all projects managed by this manager
		ArrayList<Project> managedProjects = manager.getManagedProjects();
		
		for (Enquiry enquiry : allEnquiries) {
			if (enquiry.getStatus() == EnquiryStatusEnum.PENDING) {
				// Check if enquiry is for a project managed by this manager
				for (Project project : managedProjects) {
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
	 * 
	 * @param manager
	 */
	public ArrayList<Enquiry> getEnquiriesByManagedProjects(Manager manager) {
		ArrayList<Enquiry> allEnquiries = Enquiry.getAllEnquiries();
		ArrayList<Enquiry> managerEnquiries = new ArrayList<>();
		
		// Get all projects managed by this manager
		ArrayList<Project> managedProjects = manager.getManagedProjects();
		
		for (Enquiry enquiry : allEnquiries) {
			// Check if enquiry is for a project managed by this manager
			for (Project project : managedProjects) {
				if (enquiry.getProject().getProjectName().equals(project.getProjectName())) {
					managerEnquiries.add(enquiry);
					break;
				}
			}
		}
		
		return managerEnquiries;
	}

	/**
	 * 
	 * @param manager
	 * @param enquiry
	 * @param response
	 */
	public boolean replyToEnquiry(Manager manager, Enquiry enquiry, String response) {
		// Check if enquiry exists and is pending
		if (enquiry == null || enquiry.getStatus() != EnquiryStatusEnum.PENDING || 
				response == null || response.trim().isEmpty()) {
			return false;
		}
		
		// Check if manager is managing this project
		boolean isManagingProject = false;
		ArrayList<Project> managedProjects = manager.getManagedProjects();
		for (Project project : managedProjects) {
			if (enquiry.getProject().getProjectName().equals(project.getProjectName())) {
				isManagingProject = true;
				break;
			}
		}
		
		if (!isManagingProject) {
			return false;
		}
		
		// Update enquiry
		enquiry.setReply(response);
		enquiry.setRespondent(manager);
		enquiry.setReplyDate(LocalDate.now());
		enquiry.setStatus(EnquiryStatusEnum.REPLIED);
		
		return true;
	}
	
	/**
	 * Verify manager's password
	 * @param manager
	 * @param password
	 */
	public boolean verifyPassword(Manager manager, String password) {
		if (manager == null || password == null) {
			return false;
		}
		
		return manager.authenticate(password);
	}
	
	/**
	 * Change manager's password
	 * @param manager
	 * @param oldPassword
	 * @param newPassword
	 */
	public boolean changePassword(Manager manager, String oldPassword, String newPassword) {
		if (manager == null || !isValidPassword(newPassword)) {
			return false;
		}
		
		// Verify old password
		if (!manager.authenticate(oldPassword)) {
			return false;
		}
		
		manager.setPassword(newPassword);
		return true;
	}
	
	/**
	 * Update manager's profile
	 * @param manager
	 * @param name
	 * @param nric
	 * @param age
	 * @param maritalStatus
	 */
	public void editProfile(Manager manager, String name, String nric, int age, MarriageStatusEnum maritalStatus) {
		if (manager != null) {
			manager.setName(name);
			manager.setNric(nric);
			manager.setAge(age);
			manager.setMaritalStatus(maritalStatus);
		}
	}

	/**
	 * Validates that a password meets the system's requirements.
	 * 
	 * <p>This method delegates to {@link ValidationUtils#isValidPassword(String)}
	 * to ensure consistent password validation across the application.</p>
	 * 
	 * @param password the password to validate
	 * @return {@code true} if the password is valid, {@code false} otherwise
	 * @see ValidationUtils#isValidPassword(String)
	 */
	public static boolean isValidPassword(String password) {
		return ValidationUtils.isValidPassword(password);
	}
}