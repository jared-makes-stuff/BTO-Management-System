package controller;

import interfaces.IAuth;
import utils.Auth;
import utils.DisplayMenu;
import boundary.managerview.*;
import entity.*;

public class ManagerAuth implements IAuth {
    
    /**
     * Attempts to authenticate a user as a manager.
     * Validates NRIC format and checks credentials against manager records.
     * 
     * @param nric The NRIC to look up in manager database
     * @param password The password to verify
     * @return The authenticated Manager object if successful, null otherwise
     */
    @Override
    public User authenticate(String nric, String password) {
        Manager user = (Manager)Auth.authenticate(Manager.getAllManagers(), nric, password);
                if (user != null) {
            DisplayMenu.displayMenu(new ManagerView(user));
        }
        return user;
    }
}
