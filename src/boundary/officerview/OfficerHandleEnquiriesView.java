package boundary.officerview;

import controller.*;
import entity.*;
import enums.*;
import interfaces.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Boundary class for handling and responding to enquiries for officers.
 * This view allows officers to view enquiries for their assigned projects,
 * filter pending enquiries, and respond to enquiries from applicants.
 * 
 * @implements IDisplayable
 */
public class OfficerHandleEnquiriesView implements IDisplayable {

    private Scanner scanner;
    private Officer currentOfficer;
    private OfficerController officerController;
    
    /**
     * Constructor with parameters to initialize the view with a specific officer.
     * 
     * @param officer The Officer entity whose account is being managed
     */
    public OfficerHandleEnquiriesView(Officer officer) {
        this.scanner = new Scanner(System.in);
        this.officerController = new OfficerController();
        this.currentOfficer = officer;
    }

    /**
     * Displays the main menu for handling enquiries and handles user interaction.
     * Provides options to view project enquiries, view pending enquiries,
     * reply to enquiries, or return to the main officer menu.
     */
    @Override
    public void display() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n===== HANDLE ENQUIRIES =====");
            System.out.println("1. View Project Enquiries");
            System.out.println("2. View Pending Enquiries");
            System.out.println("3. Reply to Enquiries");
            System.out.println("0. Return to Officer Menu");
            System.out.print("Enter your choice: ");
            
            String input = scanner.nextLine();
            exit = getUserInput(input);
        }
    }

    /**
     * Processes user input from the main menu and calls the appropriate method.
     * 
     * @param input The user's menu selection as a string
     * @return boolean True if the user wants to exit the menu, false otherwise
     */
    private boolean getUserInput(String input) {
        switch (input) {
            case "1":
                viewProjectEnquiries();
                return false;
            case "2":
                viewPendingEnquiries();
                return false;
            case "3":
                replyToEnquiry();
                return false;
            case "0":
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
    }

    /**
     * Displays all enquiries for a selected project that the officer is assigned to.
     * The officer selects a project from their list of assigned projects.
     */
    private void viewProjectEnquiries() {
        ArrayList<Project> assignedProjects = currentOfficer.getAssignedProjects();
        
        if (assignedProjects == null || assignedProjects.isEmpty()) {
            System.out.println("You are not assigned to any project yet.");
            return;
        }
        
        System.out.println("\n===== YOUR ASSIGNED PROJECTS =====");
        for (int i = 0; i < assignedProjects.size(); i++) {
            Project project = assignedProjects.get(i);
            System.out.println((i + 1) + ". " + project.getProjectName() + " - " + project.getNeighborhood());
        }
        
        System.out.print("Select a project to view enquiries (1-" + assignedProjects.size() + ") or 0 to cancel: ");
        String selection = scanner.nextLine();
        
        try {
            int choice = Integer.parseInt(selection);
            
            if (choice == 0) {
                return;
            }
            
            if (choice < 1 || choice > assignedProjects.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Project selectedProject = assignedProjects.get(choice - 1);
            ArrayList<Enquiry> enquiries = officerController.getEnquiriesByProject(selectedProject);
            
            if (enquiries.isEmpty()) {
                System.out.println("No enquiries found for project: " + selectedProject.getProjectName());
                return;
            }
            
            System.out.println("\n===== PROJECT ENQUIRIES: " + selectedProject.getProjectName() + " =====");
            displayEnquiries(enquiries);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
    
    /**
     * Displays only the pending enquiries for a selected project.
     * Helps the officer focus on enquiries that need a response.
     */
    private void viewPendingEnquiries() {
        ArrayList<Project> assignedProjects = currentOfficer.getAssignedProjects();
        
        if (assignedProjects == null || assignedProjects.isEmpty()) {
            System.out.println("You are not assigned to any project yet.");
            return;
        }
        
        System.out.println("\n===== YOUR ASSIGNED PROJECTS =====");
        for (int i = 0; i < assignedProjects.size(); i++) {
            Project project = assignedProjects.get(i);
            System.out.println((i + 1) + ". " + project.getProjectName() + " - " + project.getNeighborhood());
        }
        
        System.out.print("Select a project to view pending enquiries (1-" + assignedProjects.size() + ") or 0 to cancel: ");
        String selection = scanner.nextLine();
        
        try {
            int choice = Integer.parseInt(selection);
            
            if (choice == 0) {
                return;
            }
            
            if (choice < 1 || choice > assignedProjects.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Project selectedProject = assignedProjects.get(choice - 1);
            ArrayList<Enquiry> pendingEnquiries = officerController.getPendingEnquiriesByProject(selectedProject);
            
            if (pendingEnquiries.isEmpty()) {
                System.out.println("No pending enquiries found for project: " + selectedProject.getProjectName());
                return;
            }
            
            System.out.println("\n===== PENDING ENQUIRIES FOR PROJECT: " + selectedProject.getProjectName() + " =====");
            displayEnquiries(pendingEnquiries);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
    
    /**
     * Allows the officer to respond to pending enquiries for their assigned projects.
     * The officer selects a project, then an enquiry, and provides a response.
     */
    private void replyToEnquiry() {
        ArrayList<Project> assignedProjects = currentOfficer.getAssignedProjects();
        
        if (assignedProjects == null || assignedProjects.isEmpty()) {
            System.out.println("You are not assigned to any project yet.");
            return;
        }
        
        System.out.println("\n===== YOUR ASSIGNED PROJECTS =====");
        for (int i = 0; i < assignedProjects.size(); i++) {
            Project project = assignedProjects.get(i);
            System.out.println((i + 1) + ". " + project.getProjectName() + " - " + project.getNeighborhood());
        }
        
        System.out.print("Select a project to handle enquiries (1-" + assignedProjects.size() + ") or 0 to cancel: ");
        String projectSelection = scanner.nextLine();
        
        try {
            int projectChoice = Integer.parseInt(projectSelection);
            
            if (projectChoice == 0) {
                return;
            }
            
            if (projectChoice < 1 || projectChoice > assignedProjects.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Project selectedProject = assignedProjects.get(projectChoice - 1);
            ArrayList<Enquiry> pendingEnquiries = officerController.getPendingEnquiriesByProject(selectedProject);
            
            if (pendingEnquiries.isEmpty()) {
                System.out.println("No pending enquiries found for project: " + selectedProject.getProjectName());
                return;
            }
            
            System.out.println("\n===== REPLY TO ENQUIRIES FOR PROJECT: " + selectedProject.getProjectName() + " =====");
            System.out.println("Pending Enquiries:");
            
            for (int i = 0; i < pendingEnquiries.size(); i++) {
                Enquiry enquiry = pendingEnquiries.get(i);
                System.out.println((i + 1) + ". From: " + enquiry.getSubmittedBy().getName() + 
                        ", Subject: " + (enquiry.getContent().length() > 30 ? 
                                enquiry.getContent().substring(0, 27) + "..." : 
                                enquiry.getContent()));
            }
            
            System.out.print("Select an enquiry to reply (1-" + pendingEnquiries.size() + ") or 0 to cancel: ");
            String enquirySelection = scanner.nextLine();
            
            int enquiryChoice = Integer.parseInt(enquirySelection);
            
            if (enquiryChoice == 0) {
                return;
            }
            
            if (enquiryChoice < 1 || enquiryChoice > pendingEnquiries.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Enquiry selectedEnquiry = pendingEnquiries.get(enquiryChoice - 1);
            
            // Display enquiry details
            System.out.println("\nEnquiry Details:");
            System.out.println("From: " + selectedEnquiry.getSubmittedBy().getName());
            System.out.println("Date: " + selectedEnquiry.getDateTime());
            System.out.println("Content: " + selectedEnquiry.getContent());
            
            // Get reply from officer
            System.out.println("\nEnter your reply:");
            String reply = scanner.nextLine();
            
            if (reply.trim().isEmpty()) {
                System.out.println("Reply cannot be empty.");
                return;
            }
            
            // Process reply
            boolean success = officerController.replyToEnquiry(currentOfficer, selectedEnquiry, reply);
            
            if (success) {
                System.out.println("Reply sent successfully!");
            } else {
                System.out.println("Failed to send reply.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    /**
     * Displays a formatted list of enquiries with their details.
     * Shows submitter, status, date, and content for each enquiry.
     * 
     * @param enquiries ArrayList of Enquiry objects to display
     */
    private void displayEnquiries(ArrayList<Enquiry> enquiries) {
        System.out.println("Total Enquiries: " + enquiries.size());
        
        for (int i = 0; i < enquiries.size(); i++) {
            Enquiry enquiry = enquiries.get(i);
            System.out.println("\nEnquiry #" + (i + 1) + ":");
            System.out.println("ID: " + enquiry.getEnquiryID());
            System.out.println("From: " + enquiry.getSubmittedBy().getName());
            System.out.println("Subject: " + enquiry.getContent());
            System.out.println("Description: " + enquiry.getContent());
            System.out.println("Date Submitted: " + enquiry.getDateTime());
            System.out.println("Status: " + enquiry.getStatus());
            
            if (enquiry.getStatus() == EnquiryStatusEnum.REPLIED) {
                System.out.println("Resolved By: " + (enquiry.getRespondent() != null ? enquiry.getRespondent().getName() : "System"));
                System.out.println("Response: " + enquiry.getReply());
            }
        }
    }

    /**
     * Sets the current officer for this view.
     * 
     * @param currentOfficer The Officer entity to set as the current user
     */
    public void setCurrentOfficer(Officer currentOfficer) {
        this.currentOfficer = currentOfficer;
    }

    /**
     * Sets the officer controller for this view.
     * 
     * @param officerController The OfficerController to handle business logic
     */
    public void setOfficerController(OfficerController officerController) {
        this.officerController = officerController;
    }

    /**
     * Gets the current officer entity.
     * 
     * @return The Officer entity who is currently using this view
     */
    public Officer getCurrentOfficer() {
        return this.currentOfficer;
    }

    /**
     * Gets the current officer controller.
     * 
     * @return The OfficerController used by this view
     */
    public OfficerController getOfficerController() {
        return this.officerController;
    }
}