package controller;

import interfaces.IAuth;
import boundary.applicantview.*;
import entity.*;
import utils.Auth;
import utils.DisplayMenu;

public class ApplicantAuth implements IAuth {

     /**
     * Attempts to authenticate a user as an applicant.
     * Validates NRIC format and checks credentials against applicant records.
     * 
     * @param nric The NRIC to look up in officer database
     * @param password The password to verify
     * @return The authenticated user object if successful, null otherwise
     */
    @Override
    public User authenticate(String nric, String password) {
        Applicant user = (Applicant)Auth.authenticate(Applicant.getAllApplicants(), nric, password);
            if (user != null) {
            DisplayMenu.displayMenu(new ApplicantView(user));
        }
        return user;
    }
}
