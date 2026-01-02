package controller;

import interfaces.IAuth;
import boundary.officerview.*;
import entity.*;
import utils.Auth;
import utils.DisplayMenu;

public class OfficerAuth implements IAuth {

    /**
     * Attempts to authenticate a user as an officer.
     * Validates NRIC format and checks credentials against officer records.
     * 
     * @param nric The NRIC to look up in officer database
     * @param password The password to verify
     * @return The authenticated Officer object if successful, null otherwise
     */
    @Override
    public User authenticate(String nric, String password) {
        Officer user = (Officer)Auth.authenticate(Officer.getAllOfficers(), nric, password);
        if (user != null) {
            DisplayMenu.displayMenu(new OfficerView(user));
        }
        return user;
    }
}
